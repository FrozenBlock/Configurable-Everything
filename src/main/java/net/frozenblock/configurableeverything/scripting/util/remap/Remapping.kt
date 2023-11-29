package net.frozenblock.configurableeverything.scripting.util.remap

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.mapping.tree.TinyMappingFactory
import net.fabricmc.mapping.tree.TinyTree
import net.fabricmc.mappingio.MappingReader
import net.fabricmc.mappingio.MappingWriter
import net.fabricmc.mappingio.adapter.MappingSourceNsSwitch
import net.fabricmc.mappingio.format.MappingFormat
import net.fabricmc.mappingio.tree.MemoryMappingTree
import net.fabricmc.tinyremapper.IMappingProvider
import net.fabricmc.tinyremapper.InputTag
import net.fabricmc.tinyremapper.NonClassCopyMode
import net.fabricmc.tinyremapper.OutputConsumerPath
import net.fabricmc.tinyremapper.TinyRemapper
import net.fabricmc.tinyremapper.TinyUtils
import net.frozenblock.configurableeverything.util.*
import java.io.*
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers
import java.nio.file.*
import java.util.zip.*
import kotlin.io.path.Path

private val VERSION: MCVersion = MCVersion.fromClasspath
private val MANIFEST: URI = URI.create("https://piston-meta.mojang.com/mc/game/version_manifest_v2.json")
private val GSON: Gson = Gson()
private val CLIENT: HttpClient = HttpClient.newHttpClient()
private val RAW_INTERMEDIARY_MAPPINGS_FILE_PATH: Path = MAPPINGS_PATH.resolve("intermediary_${VERSION.id}.gz")
private val TINY_MAPPINGS_FILE_PATH: Path = MAPPINGS_PATH.resolve("mojang_${VERSION.id}.tiny")

var initialized: Boolean = false


private val intToOffRemapper: TinyRemapper get() {
    initialize()
    return buildRemapper(intermediaryProvider("intermediary", "official"))
}

private val offToIntRemapper: TinyRemapper get() {
    initialize()
    return buildRemapper(intermediaryProvider("official", "intermediary"))
}

private val mojToOffRemapper: TinyRemapper get() {
    initialize()
    return buildRemapper(mojangProvider("named", "official"))
}

private val offToMojRemapper: TinyRemapper get() {
    initialize()
    return buildRemapper(mojangProvider("official", "named"))
}

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

    val mappings = MemoryMappingTree()
    BufferedReader(InputStreamReader(ByteArrayInputStream(response.body()))).use { reader ->
        MappingReader.read(
            reader,
            MappingFormat.PROGUARD_FILE,
            mappings
        )
    }
    mappings.setSrcNamespace("named")
    mappings.setDstNamespaces(listOf("official"))
    val switched = MemoryMappingTree()
    mappings.accept(MappingSourceNsSwitch(switched, "official"))
    switched.accept(MappingWriter.create(TINY_MAPPINGS_FILE_PATH, MappingFormat.TINY_2_FILE))
}

private fun intermediaryProvider(from: String, to: String): IMappingProvider
    = TinyUtils.createTinyMappingProvider(RAW_INTERMEDIARY_MAPPINGS_FILE_PATH, from, to)

private fun mojangProvider(from: String, to: String): IMappingProvider
    = TinyUtils.createTinyMappingProvider(TINY_MAPPINGS_FILE_PATH, from, to)

private fun buildRemapper(vararg mappings: IMappingProvider): TinyRemapper {
    val builder = TinyRemapper.newRemapper()
        .rebuildSourceFilenames(true)
        .fixPackageAccess(true)
        .skipLocalVariableMapping(true)
        .keepInputData(false)
    mappings.forEach { builder.withMappings(it) }
    return builder.build()
}

private fun remap(
    remapper: TinyRemapper,
    filesArray: Array<File>?,
    newDir: String,
    fileExtension: String?,
    buildJar: Boolean = false,
) {
    val files: MutableMap<Path, InputTag> = mutableMapOf()
    filesArray?.forEach { file ->
        try {
            if (fileExtension == null || file.extension == fileExtension) {
                val name = file.name
                val newFile = Path("$newDir$name")
                file.copyRecursively(newFile.toFile(), onError = { file, e -> OnErrorAction.SKIP })
                files[newFile] = remapper.createInputTag()
            }
        } catch (e: IOException) {
            logError("Error while copying $file", e)
        }
    }

    val consumers: MutableList<OutputConsumerPath?>? = if (!buildJar) null else mutableListOf()

    for ((file, tag) in files) {
        try {
            val consumer: OutputConsumerPath? = if (!buildJar) null else OutputConsumerPath.Builder(file)
                .assumeArchive(true)
                .build()?.apply { consumers?.add(this) }

            consumer?.addNonClassFiles(file, NonClassCopyMode.FIX_META_INF, remapper)
            remapper.readInputsAsync(tag, file)
        } catch (e: Exception) {
            logError("Error while reading $file", e)
        }
    }
    consumers?.forEach { consumer ->
        if (consumer != null) {
            try {
                remapper.apply(consumer)
            } catch (e: Exception) {
                logError("Error while applying remapper", e)
            } finally {
                remapper.finish()
                consumer.close()
            }
        }
    }

    if (!buildJar) {
        val consumer: OutputConsumerPath = OutputConsumerPath.Builder(Path(newDir))
            .build()
        try {
            remapper.apply(consumer)
        } catch (e: Exception) {
            logError("Error while applying remapper", e)
        } finally {
            remapper.finish()
            consumer.close()
            /*for ((file, _) in files) {
                try {
                    file.toFile().deleteRecursively()
                } catch (e: IOException) {
                    logError("Error while deleting file $file", e)
                }
            }*/
        }
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
    newFile: File,
    extension: String?,
    buildJar: Boolean = false,
) = remap(
    remapper,
    arrayOf(file),
    "${newFile.parent}/",
    extension,
    buildJar,
)

private fun remap(
    remapper: TinyRemapper,
    file: File,
    newFile: File,
    buildJar: Boolean = false,
) = remap(
    remapper,
    file,
    newFile,
    file.extension,
    buildJar,
)

/**
 * @param originalFile The original script file
 * @return The remapped script file
 * @since 1.1
 */
fun remapScript(originalFile: File): File {
    initialize()

    val officialDir = ".$MOD_ID/official_scripts/"
    val intermediaryDir = ".$MOD_ID/remapped_scripts/"

    File(officialDir).recreateDir()
    File(intermediaryDir).recreateDir()

    val officialFile = File("$officialDir/${originalFile.name}")
    val intermediaryFile = File("$intermediaryDir/${originalFile.name}")

    try {
        remap(
            mojToOffRemapper,
            originalFile,
            officialFile,
            "jar",
            true,
        )

        remap(
            offToIntRemapper,
            officialFile,
            intermediaryFile,
            "jar",
            true,
        )

        return intermediaryFile
    } catch (e: Exception) {
        logError("Error while remapping script $originalFile", e)
        return originalFile
    }
}

/**
 * @since 1.1
 */
fun initialize() {
    experimentalOrThrow()
    if (initialized) return

    try {
        downloadMappings()
        initialized = true
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

        // clear official dir before remapping the game jars
        OFFICIAL_SOURCES_CACHE.toFile().recreateDir()

        log("Remapping game jars")

        remap(
            if (DEV_ENV) mojToOffRemapper else intToOffRemapper,
            INPUT_GAME_JARS.map { it.toFile() }.toTypedArray(),
            ".$MOD_ID/official/",
            "jar",
            true
        )

        remap(
            offToMojRemapper,
            OFFICIAL_SOURCES_CACHE.toFile().listFiles()!!,
            ".$MOD_ID/remapped/",
            "jar",
            true
        )

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
                    if (DEV_ENV) mojToOffRemapper else intToOffRemapper,
                    file,
                    officialFile,
                    "jar",
                    true
                )
                remap(
                    offToMojRemapper,
                    officialFile,
                    remappedFile,
                    "jar",
                    true
                )
            }
        } catch (e: Exception) {
            logError("Failed to remap mods", e)
        }

        OFFICIAL_SOURCES_CACHE.toFile().recreateDir()

        log("Successfully remapped the current codebase")
    } catch (e: Exception) {
        logError("Failed to remap codebase", e)
    }
}

private fun httpReponse(uri: URI): HttpResponse<ByteArray> {
    return CLIENT.sendAsync(makeHttpRequest(uri), BodyHandlers.ofByteArray()).get()
}
private fun makeHttpRequest(uri: URI): HttpRequest = HttpRequest.newBuilder(uri).build()
