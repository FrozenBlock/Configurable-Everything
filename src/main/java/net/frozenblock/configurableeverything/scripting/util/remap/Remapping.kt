package net.frozenblock.configurableeverything.scripting.util.remap

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.fabricmc.loader.impl.launch.MappingConfiguration
import net.fabricmc.loader.impl.util.mappings.TinyRemapperMappingsHelper
import net.fabricmc.lorenztiny.TinyMappingsLegacyWriter
import net.fabricmc.lorenztiny.TinyMappingsReader
import net.fabricmc.mapping.tree.TinyMappingFactory
import net.fabricmc.mapping.tree.TinyTree
import net.fabricmc.mappingio.MappingReader
import net.fabricmc.mappingio.adapter.MappingSourceNsSwitch
import net.fabricmc.mappingio.format.MappingFormat
import net.fabricmc.mappingio.tree.MemoryMappingTree
import net.fabricmc.tinyremapper.InputTag
import net.fabricmc.tinyremapper.OutputConsumerPath
import net.fabricmc.tinyremapper.TinyRemapper
import net.frozenblock.configurableeverything.util.*
import java.io.*
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import kotlin.io.path.Path

private val VERSION: MCVersion = MCVersion.fromClasspath
private val MANIFEST: URI = URI.create("https://piston-meta.mojang.com/mc/game/version_manifest_v2.json")
private val GSON: Gson = Gson()
private val CLIENT: HttpClient = HttpClient.newHttpClient()
private val RAW_MAPPINGS_FILE_PATH: Path = RAW_MAPPINGS_PATH.resolve("mojang_${VERSION.id}.gz")//Path(".$MOD_ID/mappings/mojang_${VERSION.id}.gz")
private val TINY_MAPPINGS_FILE_PATH: Path = TINY_MAPPINGS_PATH // TODO: use a better file

private lateinit var intermediaryMappings: MemoryMappingTree
private lateinit var mojangMappings: MemoryMappingTree

private val mappingsUri: URI?
    @Throws(IOException::class, IllegalStateException::class)
    get() {
        val manifestResp = httpReponse(MANIFEST)
        val manifestJson: JsonObject?
        ByteArrayInputStream(manifestResp.body()).use { input ->
            InputStreamReader(input).use { reader ->
                manifestJson = GSON.fromJson(reader, JsonObject::class.java)
            }
        }

        checkNotNull(manifestJson) { "Manifest json is null" }
        val versions: JsonArray = manifestJson["versions"]?.asJsonArray ?: error("Manifest versions is not a JSON array")

        for (element in versions) {
            val elementJson: JsonObject = element as? JsonObject ?: error("Version is not a JSON object")
            if (VERSION.id != elementJson["id"]?.asString) continue

            val infoUri: URI = URI.create(elementJson["url"]?.asString ?: error("Version URL is null"))
            val infoResp = httpReponse(infoUri)
            val infoJson: JsonObject?
            ByteArrayInputStream(infoResp.body()).use { input ->
                InputStreamReader(input).use { reader ->
                    infoJson = GSON.fromJson(reader, JsonObject::class.java)
                }
            }

            checkNotNull(infoJson) { "Deserialized version URL is null " }
            val mappings: JsonObject = infoJson["downloads"]?.asJsonObject?.get("$`ENVIRONMENT`_mappings")?.asJsonObject ?: error("Mappings JsonObject is null")
            val mappingsUri: URI = URI.create(mappings["url"]?.asString ?: error("Mappings URL is not null"))
            return mappingsUri
        }
        return null
    }

@Throws(RuntimeException::class, IllegalStateException::class)
private fun downloadMappings() {
    log("Downloading Mojang's Official Mappings")
    val uri: URI = try {
        mappingsUri ?: error("Mappings URI is null")
    } catch (e: IOException) {
        throw RuntimeException(e)
    }
    val response = httpReponse(uri)

    Files.newOutputStream(RAW_MAPPINGS_FILE_PATH).use { fileOutput ->
        GZIPOutputStream(fileOutput).use { gzipOutput ->
            gzipOutput.write(response.body())
        }
    }
}

@Throws(IOException::class)
private fun parseMappings() {
    parseIntermediary()
    parseMojang()
}

@Throws(IOException::class)
private fun parseIntermediary() {
    val mappings = MemoryMappingTree()

    val url = MappingConfiguration::class.java.classLoader.getResource("mappings/mappings.tiny")
    val connection = url?.openConnection() ?: error("Intermediary location null")

    BufferedReader(InputStreamReader(connection.getInputStream())).use { reader ->
        MappingReader.read(reader, MappingFormat.TINY_2_FILE, mappings)
    }

    val switched = MemoryMappingTree()
    mappings.accept(MappingSourceNsSwitch(switched, "official"))

    intermediaryMappings = mappings
}

@Throws(IOException::class)
private fun parseMojang() {
    val mappings = MemoryMappingTree()

    Files.newInputStream(RAW_MAPPINGS_FILE_PATH).use { fileInput ->
        GZIPInputStream(fileInput).use { gzipInput ->
            InputStreamReader(gzipInput).use { reader ->
                MappingReader.read(reader, MappingFormat.PROGUARD_FILE, mappings)
            }
        }
    }

    mappings.setSrcNamespace("official")
    mappings.setDstNamespaces(listOf("named"))

    mojangMappings = mappings
    //mappings.setSrcNamespace("named")
    //mappings.setDstNamespaces(listOf("official"))

    //val switchedMappings = MemoryMappingTree()
    //mappings.accept(MappingSourceNsSwitch(switchedMappings, "official"))
    //mojangMappings = switchedMappings
}


private fun convertMappings() {
    FileWriter(TINY_MAPPINGS_FILE_PATH.toFile()).use { writer ->
        val reader = TinyMappingsReader(mojangMappings, "official", "named")
        val mappings = reader.read()
        TinyMappingsLegacyWriter(writer, "official", "named").write(mappings)
    }
}

private val intermediaryTree: TinyTree
    get() {
        val url = MappingConfiguration::class.java.classLoader.getResource("mappings/mappings.tiny")
        val connection = url?.openConnection() ?: error("Intermediary location null")

        BufferedReader(InputStreamReader(connection.getInputStream())).use { reader ->
            return TinyMappingFactory.loadWithDetection(reader)
        }
    }

private val mojangMappingTree: TinyTree
    get() {
        FileInputStream(TINY_MAPPINGS_FILE_PATH.toFile()).use { input ->
            BufferedReader(InputStreamReader(input)).use { reader ->
                return TinyMappingFactory.loadLegacy(reader)
            }
        }
    }

private fun remap(
    remapper: TinyRemapper,
    filesArray: Array<File>,
    newDir: String,
    fileExtension: String
) {
    val files: MutableMap<Path, InputTag> = mutableMapOf()
    for (file in filesArray) {
        try {
            if (file.extension == fileExtension) {
                val name = file.name
                val newFile = Path("$newDir$name")
                Files.copy(file.toPath(), newFile)
                files[newFile] = remapper.createInputTag()
            }
        } catch (e: IOException) {
            logError("Error while copying $file", e)
            continue
        }
    }

    for ((file, tag) in files) {
        try {
            remapper.readInputsAsync(tag, file)
        } catch (e: IOException) {
            logError("Error while reading $file", e)
        }
    }

    try {
        val consumer: OutputConsumerPath = OutputConsumerPath.Builder(Path(newDir)).build()
        remapper.apply(consumer)
    } catch (e: Exception) {
        logError("Error while applying remapper", e)
    } finally {
        remapper.finish()
    }
}

/**
 * @since 1.1
 */
fun remapCodebase() {
    experimentalOrThrow()

    log("Attempting to remap the current codebase")
    try {
        // setup mappings
        downloadMappings()
        parseMappings()
        convertMappings()

        // actually remap stuff

        val intermediaryRemapper: TinyRemapper = TinyRemapper.newRemapper()
            .withMappings(TinyRemapperMappingsHelper.create(intermediaryTree, "intermediary", "official"))
            .rebuildSourceFilenames(false)
            .build()

        val mojangRemapper: TinyRemapper = TinyRemapper.newRemapper()
            .withMappings(TinyRemapperMappingsHelper.create(mojangMappingTree, "official", "named"))
            .rebuildSourceFilenames(false)
            .build()

        remap(
            intermediaryRemapper,
            INTERMEDIARY_GAME_CACHE_PATH.toFile().listFiles()!! + INTERMEDIARY_MOD_CACHE_PATH.toFile().listFiles()!!,
            ".$MOD_ID/official/",
            "jar"
        )
        remap(
            mojangRemapper,
            File("./.$MOD_ID/official/").listFiles()!!,
            ".$MOD_ID/remapped/",
            "class"
        )

        log("Successfully remapped the current codebase")
    } catch (e: Exception) {
        logError("Failed to remap codebase", e)
    }
}

private fun httpReponse(uri: URI): HttpResponse<ByteArray> {
    return CLIENT.sendAsync(makeHttpRequest(uri), BodyHandlers.ofByteArray()).get()
}
private fun makeHttpRequest(uri: URI): HttpRequest = HttpRequest.newBuilder(uri).build()
