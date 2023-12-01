package net.frozenblock.configurableeverything.scripting.util.remap

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.fabricmc.loader.api.FabricLoader
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
import net.frozenblock.configurableeverything.scripting.util.remap.fabric.*
import net.frozenblock.configurableeverything.util.*
import java.io.*
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers
import java.nio.file.*
import java.util.zip.GZIPOutputStream
import kotlin.io.path.Path
import kotlin.io.path.writeBytes

private const val OBFUSCATED = "official"
private const val INTERMEDIARY = "intermediary"
private const val MOJANG = "named"

private val VERSION: MCVersion = MCVersion.fromClasspath
private val MANIFEST: URI = URI.create("https://piston-meta.mojang.com/mc/game/version_manifest_v2.json")
private val GSON: Gson = Gson()
private val CLIENT: HttpClient = HttpClient.newHttpClient()
private val INTERMEDIARY_MAPPINGS_PATH: Path = MAPPINGS_PATH.resolve("intermediary_${VERSION.id}.gz")
private val MOJANG_MAPPINGS_PATH: Path = MAPPINGS_PATH.resolve("mojang_${VERSION.id}.tiny")

var initialized: Boolean = false

private val intToObfRemapper: TinyRemapper
    get() {
        initialize()
        return buildRemapper(intermediaryProvider(INTERMEDIARY, OBFUSCATED))
    }

private val obfToIntRemapper: TinyRemapper
    get() {
        initialize()
        return buildRemapper(intermediaryProvider(OBFUSCATED, INTERMEDIARY))
    }

private val mojToObfRemapper: TinyRemapper
    get() {
        initialize()
        return buildRemapper(mojangProvider(MOJANG, OBFUSCATED))
    }

private val obfToMojRemapper: TinyRemapper
    get() {
        initialize()
        return buildRemapper(mojangProvider(OBFUSCATED, MOJANG))
    }

private val intermediaryUri: URI =
    URI.create("https://maven.fabricmc.net/net/fabricmc/intermediary/${VERSION.id}/intermediary-${VERSION.id}-v2.jar")

private val mojangUri: URI?
    @Throws(IOException::class, IllegalStateException::class)
    get() {
        val manifestResp = httpReponse(MANIFEST)
        val manifestJson: JsonObject?
        InputStreamReader(
            ByteArrayInputStream(manifestResp.body())
        ).buffered().use { reader ->
            manifestJson = GSON.fromJson(reader, JsonObject::class.java)
        }

        checkNotNull(manifestJson) { "Manifest json is null" }
        val versions: JsonArray = manifestJson["versions"]?.asJsonArray ?: error("Manifest versions is not a JSON array")

        for (element in versions) {
            val elementJson: JsonObject = element as? JsonObject ?: error("Version is not a JSON object")
            if (VERSION.id != elementJson["id"]?.asString) continue

            val infoUri: URI = URI.create(elementJson["url"]?.asString ?: error("Version URL is null"))
            val infoResp = httpReponse(infoUri)
            val infoJson: JsonObject?
            InputStreamReader(
                ByteArrayInputStream(infoResp.body())
            ).buffered().use { reader ->
                infoJson = GSON.fromJson(reader, JsonObject::class.java)
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

    GZIPOutputStream(
        Files.newOutputStream(INTERMEDIARY_MAPPINGS_PATH)
    ).buffered().use { fileOutput ->
        val temp = Files.createTempFile(null, ".jar")
        val mappingsBytes: ByteArray? = try {
            temp.writeBytes(intermediaryResponse.body())
            FileSystems.newFileSystem(temp).use { jar ->
                Files.readAllBytes(jar.getPath("mappings", "mappings.tiny"))
            }
        } catch (e: Exception) {
            logError("Error while downloading intermediary", e)
            null
        } finally {
            temp.toFile().deleteRecursively()
        }
        if (mappingsBytes != null) {
            fileOutput.write(mappingsBytes)
        }
    }

    log("Downloading Mojang's Official Mappings")
    val uri: URI = mojangUri ?: error("Mappings URI is null")
    val response = httpReponse(uri)

    val mappings = MemoryMappingTree()
    InputStreamReader(ByteArrayInputStream(response.body())).buffered().use { reader ->
        MappingReader.read(
            reader,
            MappingFormat.PROGUARD_FILE,
            mappings
        )
    }
    mappings.setSrcNamespace(MOJANG)
    mappings.setDstNamespaces(listOf(OBFUSCATED))
    val switched = MemoryMappingTree()
    mappings.accept(MappingSourceNsSwitch(switched, OBFUSCATED))
    switched.accept(MappingWriter.create(MOJANG_MAPPINGS_PATH, MappingFormat.TINY_2_FILE))
}

private fun intermediaryProvider(from: String, to: String): IMappingProvider
    = TinyUtils.createTinyMappingProvider(INTERMEDIARY_MAPPINGS_PATH, from, to)

private fun mojangProvider(from: String, to: String): IMappingProvider
    = TinyUtils.createTinyMappingProvider(MOJANG_MAPPINGS_PATH, from, to)

private fun buildRemapper(
    vararg mappings: IMappingProvider,
    rebuildSourceFileNames: Boolean = true,
    fixPackageAccess: Boolean = true,
    skipLocalVariableMapping: Boolean = true,
    keepInputData: Boolean = false,
): TinyRemapper {
    val builder = TinyRemapper.newRemapper()
        .rebuildSourceFilenames(rebuildSourceFileNames)
        .fixPackageAccess(fixPackageAccess)
        .skipLocalVariableMapping(skipLocalVariableMapping)
        .keepInputData(keepInputData)
        .propagateBridges(TinyRemapper.LinkedMethodPropagation.COMPATIBLE)
        .extension(KotlinMetadataTinyRemapperExtension)
    mappings.forEach { builder.withMappings(it) }
    return builder.build()
}

private fun remap(
    remapper: TinyRemapper,
    filesArray: Array<File>?,
    newDir: Path,
    fileExtension: String?,
    buildJar: Boolean = false,
    vararg referenceDirs: Iterable<File>,
) {
    val files: MutableMap<Path, InputTag> = mutableMapOf()
    filesArray?.forEach { file ->
        try {
            if (fileExtension == null || file.extension == fileExtension) {
                val name = file.name
                val newFile = newDir.resolve(name)
                file.copyRecursively(newFile.toFile(), onError = { file, e -> OnErrorAction.SKIP })
                files[newFile] = remapper.createInputTag()
            }
        } catch (e: IOException) {
            logError("Error while copying $file", e)
        }
    }

    for (dir in referenceDirs) {
        for (file in dir) {
            log("Adding $file to remapping reference")
            remapper.readInputsAsync(remapper.createInputTag(), file.toPath())
        }
    }

    val consumers: MutableMap<OutputConsumerPath?, InputTag>? = if (!buildJar) null else mutableMapOf()

    for ((file, tag) in files) {
        try {
            val consumer: OutputConsumerPath? = if (!buildJar) null else OutputConsumerPath.Builder(file)
                .assumeArchive(true)
                .build()?.apply { consumers?.put(this, tag) }

            consumer?.addNonClassFiles(file, NonClassCopyMode.FIX_META_INF, remapper)
            remapper.readInputsAsync(tag, file)
        } catch (e: Exception) {
            logError("Error while reading $file", e)
        }
    }
    consumers?.forEach { (consumer, tag) ->
        if (consumer != null) {
            try {
                remapper.apply(consumer, tag)
            } catch (e: Exception) {
                logError("Error while applying remapper", e)
            }
        }
    }

    if (buildJar) {
        remapper.finish()
        for ((consumer, _) in consumers!!) {
            consumer?.close()
        }
    } else {
        val consumer: OutputConsumerPath = OutputConsumerPath.Builder(newDir)
            .build()
        try {
            remapper.apply(consumer)
        } catch (e: Exception) {
            logError("Error while applying remapper", e)
        } finally {
            remapper.finish()
            consumer.close()
        }
    }

    try {
        newDir.toFile().walk().forEach { file ->
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
    vararg referenceDirs: Iterable<File>,
) = remap(
    remapper,
    arrayOf(file),
    newFile.parentFile.toPath(),
    extension,
    buildJar,
    referenceDirs = referenceDirs
)

private fun remap(
    remapper: TinyRemapper,
    file: File,
    newFile: File,
    buildJar: Boolean = false,
    vararg referenceDirs: Iterable<File>,
) = remap(
    remapper,
    file,
    newFile,
    file.extension,
    buildJar,
    referenceDirs = referenceDirs
)

/**
 * @param originalFile The original script file
 * @return The remapped script file
 * @since 1.1
 */
fun remapScript(originalFile: File): File {
    initialize()

    val officialDir = Path(".$MOD_ID/official_scripts/")
    val intermediaryDir = Path(".$MOD_ID/remapped_scripts/")

    officialDir.toFile().recreateDir()
    intermediaryDir.toFile().recreateDir()

    val officialFile = officialDir.resolve(originalFile.name).toFile()
    val intermediaryFile = intermediaryDir.resolve(originalFile.name).toFile()

    try {
        remap(
            mojToObfRemapper,
            originalFile,
            officialFile,
            "jar",
            buildJar = true,
            REMAPPED_SOURCES_CACHE.asFileList!!, OFFICIAL_SOURCES_CACHE.asFileList!!
        )

        remap(
            obfToIntRemapper,
            officialFile,
            intermediaryFile,
            "jar",
            buildJar = true,
            ORIGINAL_SOURCES_CACHE.asFileList!!,
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

fun clearRemappingCache() {
    //OFFICIAL_SOURCES_CACHE.toFile().recreateDir()
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
        ORIGINAL_SOURCES_CACHE.toFile().recreateDir()
        OFFICIAL_SOURCES_CACHE.toFile().recreateDir()

        remapGameJars()
        remapMods()

        log("Successfully remapped the current codebase")
    } catch (e: Exception) {
        logError("Failed to remap codebase", e)
    }
}

private fun remapGameJars() {
    log("Remapping game jars")

    for (file in INPUT_GAME_JARS.map { it.toFile() }) {
        file.copyRecursively(ORIGINAL_SOURCES_CACHE.resolve(file.name).toFile(), true)
    }
    remap(
        intToObfRemapper,
        ORIGINAL_SOURCES_CACHE.asDir!!,
        OFFICIAL_SOURCES_CACHE,
        "jar",
        true
    )

    remap(
        obfToMojRemapper,
        OFFICIAL_SOURCES_CACHE.asDir!!,
        REMAPPED_SOURCES_CACHE,
        "jar",
        true
    )

    for (file in OFFICIAL_SOURCES_CACHE.toFile().listFiles()!!) {
        if (!file.path.contains("loom.mappings") && !file.path.contains("intermediary")) {
            file.deleteRecursively()
        }
    }
}

private fun remapMods() {
    log("Remapping mods")
    try {
        for (mod in FabricLoader.getInstance().allMods) {
            val id = mod.metadata.id

            // TODO: Config options
            val filterOption: FilterOption? = FilterOption.INCLUDED
            val filter = listOf(
                if (DEV_ENV) "frozenlib" else "configurable_everything"
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
                if (DEV_ENV) mojToObfRemapper else intToObfRemapper,
                file,
                officialFile,
                "jar",
                true,
                if (DEV_ENV) REMAPPED_SOURCES_CACHE.asFileList!! else ORIGINAL_SOURCES_CACHE.asFileList!!,
                OFFICIAL_SOURCES_CACHE.asFileList!!
            )
            remap(
                obfToMojRemapper,
                officialFile,
                remappedFile,
                "jar",
                true,
                OFFICIAL_SOURCES_CACHE.asFileList!!,
                REMAPPED_SOURCES_CACHE.asFileList!!
            )
        }
    } catch (e: Exception) {
        logError("Failed to remap mods", e)
    }
}

private fun httpReponse(uri: URI): HttpResponse<ByteArray> {
    return CLIENT.sendAsync(makeHttpRequest(uri), BodyHandlers.ofByteArray()).get()
}
private fun makeHttpRequest(uri: URI): HttpRequest = HttpRequest.newBuilder(uri).build()
