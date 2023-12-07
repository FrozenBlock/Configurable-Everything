package net.frozenblock.configurableeverything.scripting.util.remap

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.MappingResolver
import net.fabricmc.mappingio.MappingReader
import net.fabricmc.mappingio.MappingWriter
import net.fabricmc.mappingio.adapter.MappingNsCompleter
import net.fabricmc.mappingio.adapter.MappingSourceNsSwitch
import net.fabricmc.mappingio.format.MappingFormat
import net.fabricmc.mappingio.format.proguard.ProGuardFileReader
import net.fabricmc.mappingio.tree.MemoryMappingTree
import net.fabricmc.tinyremapper.IMappingProvider
import net.fabricmc.tinyremapper.InputTag
import net.fabricmc.tinyremapper.NonClassCopyMode
import net.fabricmc.tinyremapper.OutputConsumerPath
import net.fabricmc.tinyremapper.TinyRemapper
import net.fabricmc.tinyremapper.TinyUtils
import net.frozenblock.configurableeverything.config.ScriptingConfig
import net.frozenblock.configurableeverything.scripting.util.remap.fabric.*
import net.frozenblock.configurableeverything.util.*
import java.io.*
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers
import java.nio.file.*
import java.util.*
import java.util.regex.Pattern
import kotlin.io.path.Path
import kotlin.io.path.writeBytes

object Remapping {
    @PublishedApi
    internal const val OBFUSCATED = "official"
    @PublishedApi
    internal const val INTERMEDIARY = "intermediary"
    @PublishedApi
    internal const val MOJANG = "named"

    private val VERSION: MCVersion = MCVersion.fromClasspath
    private val MANIFEST: URI = URI.create("https://piston-meta.mojang.com/mc/game/version_manifest_v2.json")
    private val GSON: Gson = Gson()
    private val CLIENT: HttpClient = HttpClient.newHttpClient()
    @PublishedApi
    internal val MOJANG_MAPPINGS_PATH: Path = MAPPINGS_PATH.resolve("mojang_${VERSION.id}.tiny")

    private val SYNTHETIC_PATTERN = Pattern.compile("^(access|this|val\\\$this|lambda\\\$.*)\\\$[0-9]+\$")

    private var initialized: Boolean = false

    inline val intToMojRemapper: TinyRemapper
        get() {
            initialize()
            return buildRemapper(mappingProvider(INTERMEDIARY, MOJANG))
        }

    inline val mojToIntRemapper: TinyRemapper
        get() {
            initialize()
            return buildRemapper(mappingProvider(MOJANG, INTERMEDIARY))
        }

    inline val intToMojResolver: MappingResolver
        get() {
            initialize()
            return CEMappingResolver(mappingTree, MOJANG)
        }

    inline val mojToIntResolver: MappingResolver
        get() {
            initialize()
            return CEMappingResolver(mappingTree, INTERMEDIARY)
        }

    inline val mappingTree: MemoryMappingTree
        get() {
            initialize()
            val mappings = MemoryMappingTree()
            InputStreamReader(MOJANG_MAPPINGS_PATH.toFile().inputStream().buffered()).buffered().use { reader ->
                MappingReader.read(
                    reader,
                    MappingFormat.TINY_2_FILE,
                    mappings
                )
            }
            return mappings
        }

    private val intermediaryUri: URI =
        URI.create("https://maven.fabricmc.net/net/fabricmc/intermediary/${VERSION.id}/intermediary-${VERSION.id}-v2.jar")

    private inline val mojangUri: URI?
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
            val versions: JsonArray =
                manifestJson["versions"]?.asJsonArray ?: error("Manifest versions is not a JSON array")

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
                val mappings: JsonObject =
                    infoJson["downloads"]?.asJsonObject?.get("$`ENVIRONMENT`_mappings")?.asJsonObject
                        ?: error("Mappings JsonObject is null")
                return URI.create(mappings["url"]?.asString ?: error("Mappings URL is not null"))
            }
            return null
        }

    @Throws(RuntimeException::class, IllegalStateException::class)
    private fun downloadMappings() {
        log("Downloading Intermediary")
        val intermediaryResponse = httpReponse(intermediaryUri)

        val temp = Files.createTempFile(null, ".jar")
        val intermediaryBytes: ByteArray? = try {
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

        log("Downloading Mojang's Official Mappings")
        val uri: URI = mojangUri ?: error("Mappings URI is null")
        val response = httpReponse(uri)
        parseAndSaveMappings(intermediaryBytes, response.body())
    }

    private fun parseAndSaveMappings(
        intermediaryBytes: ByteArray?,
        mojangBytes: ByteArray?,
    ) {
        val mappings = MemoryMappingTree()
        val intMappings = MemoryMappingTree()

        // populate intMappings
        InputStreamReader(ByteArrayInputStream(intermediaryBytes)).buffered().use { reader ->
            MappingReader.read(
                reader,
                MappingFormat.TINY_2_FILE,
                intMappings
            )
        }
        // modify intMappings
        val intCompleter = MappingNsCompleter(mappings, Collections.singletonMap(MOJANG, INTERMEDIARY), true)
        intMappings.accept(intCompleter)

        // modify mojMaps

        // Filter out synthetic
        val nameFilter = DstNameFilterMappingVisitor(mappings, SYNTHETIC_PATTERN)

        // make "official" the source namespace
        val switched = MappingSourceNsSwitch(nameFilter, OBFUSCATED)

        // insert mojang mappings
        InputStreamReader(ByteArrayInputStream(mojangBytes)).buffered().use { reader ->
            ProGuardFileReader.read(
                reader,
                MOJANG, OBFUSCATED,
                switched
            )
        }

        // update mappings with mojmaps
        mappings.accept(MappingWriter.create(MOJANG_MAPPINGS_PATH, MappingFormat.TINY_2_FILE))
    }

    @PublishedApi
    internal fun mappingProvider(from: String, to: String): IMappingProvider =
        TinyUtils.createTinyMappingProvider(MOJANG_MAPPINGS_PATH, from, to)

    @PublishedApi
    internal fun buildRemapper(
        vararg mappings: IMappingProvider,
        rebuildSourceFileNames: Boolean = true,
        fixPackageAccess: Boolean = false,
        skipLocalVariableMapping: Boolean = true,
        keepInputData: Boolean = false,
    ): TinyRemapper {
        val builder = TinyRemapper.newRemapper()
            .rebuildSourceFilenames(rebuildSourceFileNames)
            .fixPackageAccess(fixPackageAccess)
            .skipLocalVariableMapping(skipLocalVariableMapping)
            .keepInputData(keepInputData)
            .extension(KotlinMetadataTinyRemapperExtension)
        mappings.forEach { builder.withMappings(it) }
        return builder.build()
    }

    fun remap(
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
                    file.copyRecursively(newFile.toFile(), onError = { file, _ -> OnErrorAction.SKIP })
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

        if (buildJar)
            finalizeRemapper(remapper, consumers)
        else {
            val consumer: OutputConsumerPath = OutputConsumerPath.Builder(newDir)
                .build()
            try {
                remapper.apply(consumer)
            } catch (e: Exception) {
                logError("Error while applying remapper", e)
            } finally {
                finalizeRemapper(remapper, listOf(consumer))
            }
        }

        filterPackageInfo(newDir.toFile())
    }

    private fun filterPackageInfo(dir: File) {
        try {
            for (file in dir.walk()) {
                if (file.exists() && file.name.contains("package-info")) file.deleteRecursively()
            }
        } catch (e: Exception) {
            logError("Could not delete all package-info classes", e)
        }
    }

    private fun finalizeRemapper(
        remapper: TinyRemapper,
        consumers: Collection<OutputConsumerPath?>? = null
    ) {
        remapper.finish()
        if (consumers == null) return
        for (consumer in consumers) {
            consumer?.close()
        }
    }

    private fun finalizeRemapper(
        remapper: TinyRemapper,
        consumers: Map<OutputConsumerPath?, InputTag?>? = null
    ) = finalizeRemapper(remapper, consumers?.keys)

    fun remap(
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

    fun remap(
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

    private val REMAPPED_SCRIPTS = Path(".$MOD_ID/remapped_scripts/").apply {
        this.toFile().recreateDir()
    }

    /**
     * @param originalFile The original script file
     * @return The remapped script file
     * @since 1.1
     */
    fun remapScript(originalFile: File): File {
        initialize()

        val intermediaryFile = REMAPPED_SCRIPTS.resolve(originalFile.name).toFile()

        try {
            remap(
                mojToIntRemapper,
                originalFile,
                intermediaryFile,
                "jar",
                buildJar = true,
                REMAPPED_SOURCES_CACHE.asFileList!!, ORIGINAL_SOURCES_CACHE.asFileList!!
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

    /**
     * @since 1.1
     */
    fun remapCodebase() {
        experimentalOrThrow()
        if (ScriptingConfig.get().remapping != true) return

        log("Attempting to remap the current codebase")
        try {
            initialize()

            remapGameJars()
            remapMods()

            log("Successfully remapped the current codebase")
        } catch (e: Exception) {
            logError("Failed to remap codebase", e)
        }
    }

    private fun remapGameJars() {
        log("Remapping game jars")

        if (DEV_ENV) {
            remap(
                mojToIntRemapper,
                INPUT_GAME_JARS.map { it.toFile() }.toTypedArray(),
                ORIGINAL_SOURCES_CACHE,
                "jar",
                true
            )
        } else {
            for (file in INPUT_GAME_JARS.map { it.toFile() }) {
                file.copyRecursively(ORIGINAL_SOURCES_CACHE.resolve(file.name).toFile(), true)
            }
        }
        remap(
            intToMojRemapper,
            ORIGINAL_SOURCES_CACHE.asDir!!,
            REMAPPED_SOURCES_CACHE,
            "jar",
            true
        )
        filterRemappedJars()
    }

    private fun remapMods() {
        val config = ScriptingConfig.get()
        val filter = config.filter ?: return
        val modsToRemap = config.modsToRemap ?: return
        if (modsToRemap.isEmpty()) return

        log("Remapping mods")
        try {
            for (mod in FabricLoader.getInstance().allMods) {
                val id = mod.metadata.id

                when (filter) {
                    ScriptingConfig.FilterOption.INCLUDED -> if (!modsToRemap.contains(id)) continue
                    ScriptingConfig.FilterOption.EXCLUDED -> if (modsToRemap.contains(id)) continue
                }

                val file = getModFile(id)
                if (file == null) {
                    logError("File for mod id $id is null")
                    continue
                }
                val remappedFile = REMAPPED_SOURCES_CACHE.resolve(file.name).toFile()

                if (!DEV_ENV) remap(
                    intToMojRemapper,
                    file,
                    remappedFile,
                    "jar",
                    buildJar = true,
                    ORIGINAL_SOURCES_CACHE.asFileList!!,
                    REMAPPED_SOURCES_CACHE.asFileList!!,
                )
            }
        } catch (e: Exception) {
            logError("Failed to remap mods", e)
        }
    }

    private inline val File.isGameJar: Boolean
        get() = this.extension == "jar" && (this.path.contains("loom.mappings") || this.path.contains("intermediary"))

    private fun filterRemappedJars() {
        for (file in REMAPPED_SOURCES_CACHE.asDir!!) {
            if (file.isGameJar) {
                file.removeFromJar { entry ->
                    val name = entry.name
                    if (name.contains("META-INF")) return@removeFromJar false
                    if (name.contains(Regex("assets/|data/"))) return@removeFromJar true
                    if (name.contains("com/mojang/")) return@removeFromJar false
                    entry.name.endsWith(".class") && (entry.name.contains("class_") || !entry.name.contains("net/minecraft/"))
                }
            }
        }
    }

    private fun httpReponse(uri: URI): HttpResponse<ByteArray> {
        return CLIENT.sendAsync(makeHttpRequest(uri), BodyHandlers.ofByteArray()).get()
    }

    private fun makeHttpRequest(uri: URI): HttpRequest = HttpRequest.newBuilder(uri).build()

}
