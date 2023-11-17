package net.frozenblock.configurableeverything.scripting.util.remap

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.impl.launch.MappingConfiguration
import net.fabricmc.loader.impl.util.mappings.TinyRemapperMappingsHelper
import net.fabricmc.lorenztiny.TinyMappingsReader
import net.fabricmc.lorenztiny.TinyMappingsWriter
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
import org.cadixdev.lorenz.MappingSet
import java.io.*
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers
import java.nio.file.*
import java.util.zip.*
import kotlin.io.path.Path
import kotlin.io.path.deleteRecursively
import kotlin.io.path.name

private val VERSION: MCVersion = MCVersion.fromClasspath
private val MANIFEST: URI = URI.create("https://piston-meta.mojang.com/mc/game/version_manifest_v2.json")
private val GSON: Gson = Gson()
private val CLIENT: HttpClient = HttpClient.newHttpClient()
private val RAW_INTERMEDIARY_MAPPINGS_FILE_PATH: Path = RAW_MAPPINGS_PATH.resolve("intermediary_${VERSION.id}.gz")
private val RAW_MOJANG_MAPPINGS_FILE_PATH: Path = RAW_MAPPINGS_PATH.resolve("mojang_${VERSION.id}.gz")
private val TINY_MAPPINGS_FILE_PATH: Path = TINY_MAPPINGS_PATH // TODO: use a better file

private lateinit var intermediaryMappings: MemoryMappingTree
private lateinit var mojangMappings: MemoryMappingTree

private lateinit var intToOffRemapper: TinyRemapper
private lateinit var offToIntRemapper: TinyRemapper

private lateinit var mojToOffRemapper: TinyRemapper
private lateinit var offToMojRemapper: TinyRemapper

private val intermediaryUri: URI =
    URI.create("https://maven.fabricmc.net/net/fabricmc/intermediary/${VERSION.id}/intermediary-${VERSION.id}-v2.jar")

private val mojangUri: URI?
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
            val mappings: JsonObject = infoJson["downloads"]?.asJsonObject?.get("$`ENVIRONMENT`_mappings")?.asJsonObject
                ?: error("Mappings JsonObject is null")
            return URI.create(mappings["url"]?.asString ?: error("Mappings URL is not null"))
        }
        return null
    }

@Throws(RuntimeException::class, IllegalStateException::class)
private fun downloadMappings() {
    log("Downloading Intermediary")
    val intermediaryResponse = httpReponse(intermediaryUri)

    Files.newOutputStream(RAW_INTERMEDIARY_MAPPINGS_FILE_PATH).use { fileOutput ->
        GZIPOutputStream(fileOutput).use { gzipOutput ->
            val temp = Files.createTempFile(null, ".jar")
            val mappingsBytes: ByteArray? = try {
                Files.write(temp, intermediaryResponse.body())
                FileSystems.newFileSystem(temp).use { jar ->
                    Files.readAllBytes(jar.getPath("mappings", "mappings.tiny"))
                }
            } catch (e: Exception) {
                logError("Error while downloading intermediary", e)
                null
            } finally {
                Files.delete(temp)
            }
            if (mappingsBytes != null) {
                gzipOutput.write(mappingsBytes)
            }
        }
    }

    log("Downloading Mojang's Official Mappings")
    val uri: URI = try {
        mojangUri ?: error("Mappings URI is null")
    } catch (e: IOException) {
        throw RuntimeException(e)
    }
    val response = httpReponse(uri)

    Files.newOutputStream(RAW_MOJANG_MAPPINGS_FILE_PATH).use { fileOutput ->
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
    log("Parsing Intermediary")
    val mappings = MemoryMappingTree()

    GZIPInputStream(Files.newInputStream(RAW_INTERMEDIARY_MAPPINGS_FILE_PATH)).use { input ->
        BufferedReader(InputStreamReader(input)).use { reader ->
            MappingReader.read(reader, MappingFormat.TINY_2, mappings)
        }
    }

    val switched = MemoryMappingTree()
    mappings.accept(MappingSourceNsSwitch(switched, "official"))

    intermediaryMappings = mappings
}

@Throws(IOException::class)
private fun parseMojang() {
    log("Parsing Mojang's Official Mappings")
    val mappings = MemoryMappingTree()

    Files.newInputStream(RAW_MOJANG_MAPPINGS_FILE_PATH).use { fileInput ->
        GZIPInputStream(fileInput).use { gzipInput ->
            BufferedReader(InputStreamReader(gzipInput)).use { reader ->
                MappingReader.read(reader, MappingFormat.PROGUARD, mappings)
            }
        }
    }

    mappings.setSrcNamespace("official")
    mappings.setDstNamespaces(listOf("named"))

    mappings.setSrcNamespace("named")
    mappings.setDstNamespaces(listOf("official"))

    val switchedMappings = MemoryMappingTree()
    mappings.accept(MappingSourceNsSwitch(switchedMappings, "official"))
    mojangMappings = switchedMappings
}


private fun convertMappings() {
    log("Converting Official Mojang Mappings")
    FileWriter(TINY_MAPPINGS_FILE_PATH.toFile()).use { writer ->
        val reader = TinyMappingsReader(mojangMappings, "official", "named")
        val mappings: MappingSet = reader.read()
        TinyMappingsWriter(writer, "official", "named").use { tinyWriter ->
            tinyWriter.write(mappings)
        }
    }
}

private val intermediaryTree: TinyTree by lazy {
    val url = MappingConfiguration::class.java.classLoader.getResource("mappings/mappings.tiny")
    val connection = url?.openConnection() ?: error("Intermediary location null")

    BufferedReader(InputStreamReader(connection.getInputStream())).use { reader ->
        TinyMappingFactory.loadWithDetection(reader)
    }
}

private val mojangMappingTree: TinyTree by lazy {
    FileInputStream(TINY_MAPPINGS_FILE_PATH.toFile()).use { input ->
        BufferedReader(InputStreamReader(input)).use { reader ->
            TinyMappingFactory.loadWithDetection(reader)
        }
    }
}

private fun remap(
    remapper: TinyRemapper,
    filesArray: Array<File>?,
    newDir: String,
    fileExtension: String?
) {
    val files: MutableMap<Path, InputTag> = mutableMapOf()
    runBlocking {
        filesArray?.forEach { file -> launch {
            try {
                if (fileExtension == null || file.extension == fileExtension) {
                    val name = file.name
                    val newFile = Path("$newDir$name")
                    file.copyRecursively(newFile.toFile())
                    files[newFile] = remapper.createInputTag()
                }
            } catch (e: IOException) {
                logError("Error while copying $file", e)
            }
        } }
    }

    runBlocking {
        for ((file, tag) in files) {
            launch {
                try {
                    remapper.readInputsAsync(tag, file)
                } catch (e: Exception) {
                    logError("Error while reading $file", e)
                }
            }
        }
    }

    try {
        val consumer: OutputConsumerPath = OutputConsumerPath.Builder(Path(newDir)).build()
        remapper.apply(consumer)
    } catch (e: Exception) {
        logError("Error while applying remapper", e)
    } finally {
        /*for ((file, _) in files) {
            try {
                file.toFile().deleteRecursively()
            } catch (e: IOException) {
                logError("Error while deleting file $file", e)
            }
        }*/
        //remapper.finish()
    }

    try {
        File(newDir).walk().forEach { file ->
            if (file.exists() && file.name.contains("package-info")) file.deleteRecursively()
        }
    } catch (e: IOException) {
        logError("Could not delete all package-info classes", e)
    }
}

private fun remap(
    remapper: TinyRemapper,
    file: File,
    newFile: File
) = remap(
    remapper,
    arrayOf(file),
    "${newFile.parent}/",
    file.extension
)

/**
 * @param script The original script file
 * @return The remapped script file
 * @since 1.1
 */
fun remapScript(script: File): File {
    initialize()

    val officialFile: File = File(".$MOD_ID/official_scripts/${script.name}")
    val mojangFile: File = File(".$MOD_ID/remapped_scripts/${script.name}")

    try {

        remap(
            mojToOffRemapper,
            officialFile,
            mojangFile
        )

        remap(
            offToIntRemapper,
            script,
            officialFile
        )

        script.deleteRecursively()
        officialFile.deleteRecursively()
    } catch (e: Exception) {
        logError("Error while remapping script $script", e)
        return script
    }

    return mojangFile
}

/**
 * @since 1.1
 */
fun initialize() {
    experimentalOrThrow()
    if (::offToMojRemapper.isInitialized) return

    try {
        downloadMappings()
        parseMappings()
        convertMappings()

        log("Building remappers")

        intToOffRemapper = TinyRemapper.newRemapper()
            .withMappings(TinyRemapperMappingsHelper.create(intermediaryTree, "intermediary", "official"))
            .rebuildSourceFilenames(true)
            .keepInputData(true)
            .build()

        offToIntRemapper = TinyRemapper.newRemapper()
            .withMappings(TinyRemapperMappingsHelper.create(intermediaryTree, "official", "intermediary"))
            .rebuildSourceFilenames(true)
            .keepInputData(true)
            .build()

        mojToOffRemapper = TinyRemapper.newRemapper()
            .withMappings(TinyRemapperMappingsHelper.create(mojangMappingTree, "named", "official"))
            .rebuildSourceFilenames(true)
            .keepInputData(true)
            .build()

        offToMojRemapper = TinyRemapper.newRemapper()
            .withMappings(TinyRemapperMappingsHelper.create(mojangMappingTree, "official", "named"))
            .rebuildSourceFilenames(true)
            .keepInputData(true)
            .build()
    } catch (e: Exception) {
        logError("Failed to initialize remapping", e)
    }
}

private enum class FilterOption {
    INCLUDED,
    EXCLUDED
}

/**
 * @since 1.1
 */
fun remapCodebase() {
    experimentalOrThrow()

    log("Attempting to remap the current codebase")
    try {
        initialize()

        // remap mods
        log("Remapping mods")
        try {
            for (mod in FabricLoader.getInstance().allMods) {
                val id = mod.metadata.id

                // TODO: Config options
                val filterOption: FilterOption? = FilterOption.INCLUDED
                val filter = listOf(
                    "frozenlib"
                )
                when (filterOption) {
                    FilterOption.INCLUDED -> if (!filter.contains(id)) continue
                    FilterOption.EXCLUDED -> if (filter.contains(id)) continue
                    else -> continue
                }

                val file = getModFile(id)
                if (file == null) {
                    logError("File for mod id $id is null")
                    continue
                }
                val officialFile = File(".$MOD_ID/official/${file.name}")
                val remappedFile = File(".$MOD_ID/remapped/${file.name}")

                remap(
                    if (FabricLoader.getInstance().isDevelopmentEnvironment) mojToOffRemapper else intToOffRemapper,
                    file,
                    officialFile
                )
                remap(
                    if (FabricLoader.getInstance().isDevelopmentEnvironment) mojToOffRemapper else intToOffRemapper,
                    file,
                    remappedFile
                )
            }
        } catch (e: Exception) {
            logError("Failed to remap mods", e)
        }

        remap(
            offToMojRemapper,
            INPUT_GAME_JARS.map { it.toFile() }.toTypedArray(),
            ".$MOD_ID/remapped/",
            "jar"
        )

        REMAPPED_SOURCES_CACHE.toFile().listFiles()?.forEach { file ->
            // should delete the leftover obfuscated files
            if (!file.isDirectory) file.deleteRecursively()
        }
        val jar = File(".$MOD_ID/remapped.jar")
        jar.deleteRecursively()
        REMAPPED_SOURCES_CACHE.toFile().addToJar(jar)

        for (file in OFFICIAL_SOURCES_CACHE.toFile().listFiles()!!) {
            file.deleteRecursively()
        }

        log("Successfully remapped the current codebase")
    } catch (e: Exception) {
        logError("Failed to remap codebase", e)
    }
}

private fun httpReponse(uri: URI): HttpResponse<ByteArray> {
    return CLIENT.sendAsync(makeHttpRequest(uri), BodyHandlers.ofByteArray()).get()
}
private fun makeHttpRequest(uri: URI): HttpRequest = HttpRequest.newBuilder(uri).build()
