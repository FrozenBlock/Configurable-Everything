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
import net.fabricmc.tinyremapper.*
import net.frozenblock.configurableeverything.config.ScriptingConfig
import net.frozenblock.configurableeverything.scripting.util.remap.fabric.DstNameFilterMappingVisitor
import net.frozenblock.configurableeverything.scripting.util.remap.fabric.KotlinMetadataTinyRemapperExtension
import net.frozenblock.configurableeverything.util.*
import java.io.*
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers
import java.nio.channels.FileChannel
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
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

    private val SYNTHETIC_PATTERN = Pattern.compile("^(access|this|val\\$this|lambda\\$.*)\\$[0-9]+$")

    private var initialized: Boolean = false

    // Caching constants
    private val CACHE_ROOT: Path = Path(".$MOD_ID/remap-cache/")
    private val CACHE_INPUTS: Path = CACHE_ROOT.resolve("inputs")
    private val CACHE_MANIFEST: Path = CACHE_ROOT.resolve("manifest.json")
    private val CACHE_LOCK: Path = CACHE_ROOT.resolve("manifest.lock")
    private const val CACHE_TMP_SUFFIX = ".tmp"

    // Ensure cache directories exist
    private fun ensureCacheDirs() {
        try {
            Files.createDirectories(CACHE_INPUTS)
            Files.createDirectories(CACHE_ROOT)
        } catch (e: Exception) {
            logError("Failed to create remap cache directories", e)
        }
    }

    // Compute SHA-256 for a file path
    private fun computeSha256(path: Path?): String? {
        if (path == null) return null
        try {
            val digest = java.security.MessageDigest.getInstance("SHA-256")
            Files.newInputStream(path).use { input ->
                val buffer = ByteArray(8192)
                var read: Int
                while (input.read(buffer).also { read = it } > 0) {
                    digest.update(buffer, 0, read)
                }
            }
            return digest.digest().joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            logError("Failed to compute sha256 for $path", e)
            return null
        }
    }

    // Read manifest (thread-safe via file lock when writing)
    private fun loadCacheManifest(): JsonObject {
        ensureCacheDirs()
        if (!Files.exists(CACHE_MANIFEST)) return JsonObject()

        return try {
            Files.newInputStream(CACHE_MANIFEST).use { input ->
                InputStreamReader(input).use { reader ->
                    GSON.fromJson(reader, JsonObject::class.java) ?: JsonObject()
                }
            }
        } catch (e: Exception) {
            logError("Failed to read cache manifest, starting fresh", e)
            JsonObject()
        }
    }

    // Atomically write manifest with a lock
    private fun saveCacheManifest(manifest: JsonObject) {
        ensureCacheDirs()
        // write to temp file then atomically move
        val tmp = CACHE_MANIFEST.resolveSibling("manifest.json$CACHE_TMP_SUFFIX-${UUID.randomUUID()}")
        try {
            Files.newOutputStream(tmp).use { output ->
                OutputStreamWriter(output).use { writer ->
                    writer.write(GSON.toJson(manifest))
                }
            }
            try {
                Files.move(tmp, CACHE_MANIFEST, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING)
            } catch (e: Exception) {
                // fallback to non-atomic
                Files.move(tmp, CACHE_MANIFEST, StandardCopyOption.REPLACE_EXISTING)
            }
        } catch (e: Exception) {
            logError("Failed to write cache manifest", e)
            try { Files.deleteIfExists(tmp) } catch (_: Exception) {}
        }
    }

    // Acquire manifest file lock (blocking with retries)
    private fun <T> withManifestLock(action: () -> T): T {
        ensureCacheDirs()
        var raf: RandomAccessFile? = null
        var channel: FileChannel? = null
        var lock: java.nio.channels.FileLock? = null
        try {
            raf = RandomAccessFile(CACHE_LOCK.toFile(), "rw")
            channel = raf.channel
            var attempts = 0
            while (true) {
                try {
                    lock = channel.lock()
                    break
                } catch (e: Exception) {
                    attempts++
                    if (attempts > 5) throw e
                    Thread.sleep(150)
                }
            }
            return action()
        } finally {
            try { lock?.release() } catch (_: Exception) {}
            try { channel?.close() } catch (_: Exception) {}
            try { raf?.close() } catch (_: Exception) {}
        }
    }

    // Lookup cached remapped file by input hash + remapper hash
    private fun getCachedByHashes(inputHash: String, remapperHash: String): Path? {
        val manifest = loadCacheManifest()
        val key = "${inputHash}_$remapperHash"
        if (!manifest.has(key)) return null
        try {
            val el = manifest.getAsJsonObject(key)
            val cached = el["cached"]?.asString ?: return null
            val cachedPath = CACHE_INPUTS.resolve(cached)
            if (Files.exists(cachedPath) && Files.size(cachedPath) > 0) return cachedPath
        } catch (e: Exception) {
            logError("Failed to read manifest entry for $key", e)
        }
        return null
    }

    // Commit a cached file for inputHash+remapperHash. Assumes src exists and is final file to move into cache.
    private fun commitCache(inputHash: String, remapperHash: String, src: Path) {
        ensureCacheDirs()
        val key = "${inputHash}_$remapperHash"
        val targetName = src.fileName.toString().let { name ->
            // include hashes in filename to avoid collisions
            "${name.removeSuffix(".jar")}-${inputHash.substring(0,8)}-${remapperHash.substring(0,8)}.jar"
        }
        val target = CACHE_INPUTS.resolve(targetName)
        // write to temp then move into place
        val tmp = CACHE_INPUTS.resolve(targetName + CACHE_TMP_SUFFIX + "-${UUID.randomUUID()}")
        try {
            Files.copy(src, tmp, StandardCopyOption.REPLACE_EXISTING)
            try {
                Files.move(tmp, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING)
            } catch (e: Exception) {
                Files.move(tmp, target, StandardCopyOption.REPLACE_EXISTING)
            }

            // update manifest under lock
            withManifestLock {
                val manifest = loadCacheManifest()
                val entry = JsonObject()
                entry.addProperty("inputHash", inputHash)
                entry.addProperty("remapperHash", remapperHash)
                entry.addProperty("cached", targetName)
                manifest.add(key, entry)
                saveCacheManifest(manifest)
                Unit
            }
        } catch (e: Exception) {
            logError("Failed to commit cache for $src -> $target", e)
            try { Files.deleteIfExists(tmp) } catch (_: Exception) {}
        }
    }

    @Suppress("MemberVisibilityCanBePrivate", "SpellCheckingInspection")
    inline val intToMojRemapper: TinyRemapper
        get() {
            initialize()
            return buildRemapper(mappingProvider(INTERMEDIARY, MOJANG))
        }

    @Suppress("MemberVisibilityCanBePrivate", "SpellCheckingInspection")
    inline val obfToMojRemapper: TinyRemapper
        get() {
            initialize()
            return buildRemapper(mappingProvider(OBFUSCATED, MOJANG))
        }

    @Suppress("MemberVisibilityCanBePrivate", "SpellCheckingInspection")
    inline val mojToIntRemapper: TinyRemapper
        get() {
            initialize()
            return buildRemapper(mappingProvider(MOJANG, INTERMEDIARY))
        }

    @Suppress("unused")
    inline val intToMojResolver: MappingResolver
        get() {
            initialize()
            return CEMappingResolver(mappingTree, MOJANG)
        }

    @Suppress("unused")
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

        // write mappings
        mappings.accept(MappingWriter.create(MOJANG_MAPPINGS_PATH, MappingFormat.TINY_2_FILE))
        saveLicense()
    }

    private val LICENSE_FILE: File = MAPPINGS_PATH.resolve("README.txt").toFile()

    private const val LICENSE: String =
"""# (c) 2020 Microsoft Corporation. These mappings are provided "as-is" and you bear the risk of using them.
You may copy and use the mappings for development purposes, but you may not redistribute the mappings complete and unmodified.
Microsoft makes no warranties, express or implied, with respect to the mappings provided here.
Use and modification of this document or the source code (in any form) of Minecraft: Java Edition is governed by the
Minecraft End User License Agreement available at https://account.mojang.com/documents/minecraft_eula."""

    private fun saveLicense() {
        if (LICENSE_FILE.exists()) return

        try {
            OutputStreamWriter(FileOutputStream(LICENSE_FILE)).buffered().use { writer ->
                writer.write(LICENSE)
            }
        } catch (e: IOException) {
            logError("Writing license README failed.", e)
            log(LICENSE) // log it in case writing the file fails lol
        }
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
                    file.copyRecursively(newFile.toFile(), onError = { _, _ -> OnErrorAction.SKIP })
                    files[newFile] = remapper.createInputTag()
                }
            } catch (e: IOException) {
                logError("Error while copying $file", e)
            }
        }

        for (dir in referenceDirs) {
            for (file in dir) {
                log("Adding $file to remapping reference", DEV_ENV)
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

    internal val ORIGINAL_SCRIPTS = Path(".$MOD_ID/original_scripts/").apply {
        this.toFile().recreateDir()
    }

    internal val REMAPPED_SCRIPTS = Path(".$MOD_ID/remapped_scripts/").apply {
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
                REMAPPED_SOURCES_CACHE.asFileList!!
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
        if (initialized) return

        try {
            // If the mojang mappings file already exists and appears valid (non-zero length),
            // skip downloading mappings to avoid unnecessary network usage.
            try {
                if (Files.exists(MOJANG_MAPPINGS_PATH) && Files.size(MOJANG_MAPPINGS_PATH) > 0L) {
                    // Ensure license/readme exists next to mappings as well
                    try { saveLicense() } catch (_: Exception) {}
                    initialized = true
                    log("Mappings found locally; skipping download")
                    return
                }
            } catch (e: Exception) {
                // If any filesystem check fails, fall back to attempting a download
                logError("Failed to inspect existing mappings file, will attempt download", e)
            }

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
        if (!ScriptingConfig.get().remapping) return

        log("Attempting to remap the current codebase")
        try {
            initialize()
            // prune cache entries with missing sources early
            pruneOrphanedCache()

            remapGameJars()
            remapMods()

            log("Successfully remapped the current codebase")
        } catch (e: Exception) {
            logError("Failed to remap codebase", e)
        }
    }

    // Remove cached remapped files whose source input no longer exists (or whose cached file is missing)
    private fun pruneOrphanedCache() {
        ensureCacheDirs()

        // gather current source hashes (game jars + mods)
        val currentHashes = mutableSetOf<String>()

        try {
            for (p in INPUT_GAME_JARS) {
                try {
                    val h = computeSha256(p)
                    if (h != null) currentHashes.add(h)
                } catch (_: Exception) {}
            }
        } catch (_: Exception) {}

        try {
            for (mod in FabricLoader.getInstance().allMods) {
                // skip the 'java' pseudo-mod (we don't remap/cache it)
                if (mod.metadata.id.equals("java", ignoreCase = true)) continue
                try {
                    val file = getModFile(mod.metadata.id) ?: continue
                    val h = computeSha256(file.toPath())
                    if (h != null) currentHashes.add(h)
                } catch (_: Exception) {}
            }
        } catch (_: Exception) {}

        // Acquire manifest lock and remove entries that are orphaned or whose cached file is missing
        try {
            withManifestLock {
                val manifest = loadCacheManifest()
                val toRemove = mutableListOf<String>()
                for ((key, value) in manifest.entrySet()) {
                    try {
                        val entry = value.asJsonObject
                        val inputHash = entry["inputHash"]?.asString
                        val cachedName = entry["cached"]?.asString
                        val cachedPath = if (cachedName != null) CACHE_INPUTS.resolve(cachedName) else null

                        var remove = false
                        if (inputHash == null) remove = true
                        else if (!currentHashes.contains(inputHash)) remove = true
                        if (cachedPath != null && !Files.exists(cachedPath)) remove = true

                        if (remove) {
                            // delete cached file if present
                            try { if (cachedPath != null) Files.deleteIfExists(cachedPath) } catch (_: Exception) {}
                            toRemove.add(key)
                        }
                    } catch (_: Exception) {
                        // malformed entry -> remove
                        toRemove.add(key)
                    }
                }

                if (toRemove.isNotEmpty()) {
                    for (k in toRemove) manifest.remove(k)
                    saveCacheManifest(manifest)
                }
                Unit
            }
        } catch (e: Exception) {
            logError("Failed to prune remap cache", e)
        }
    }

    private fun remapGameJars() {
        log("Remapping game jars")

        // Try to use cache for game jars: require cached remapped jars for all inputs.
        try {
            val allInputs = INPUT_GAME_JARS.map { it.toFile().toPath() }
            val remapperHash = computeSha256(MOJANG_MAPPINGS_PATH) ?: VERSION.id
            var allCached = true
            val cachedPaths = mutableListOf<Pair<Path, Path>>()
            for (p in allInputs) {
                val inHash = computeSha256(p)
                if (inHash == null) { allCached = false; break }
                val cached = getCachedByHashes(inHash, remapperHash)
                if (cached == null) { allCached = false; break }
                cachedPaths.add(p to cached)
            }
            if (allCached) {
                // copy all cached remapped jars into REMAPPED_SOURCES_CACHE
                for ((input, cached) in cachedPaths) {
                    val target = REMAPPED_SOURCES_CACHE.resolve(input.fileName.toString())
                    try {
                        Files.copy(cached, target, StandardCopyOption.REPLACE_EXISTING)
                    } catch (e: Exception) {
                        logError("Failed to copy cached game jar $cached", e)
                    }
                }
                return
            }
        } catch (_: Exception) {}

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
            obfToMojRemapper,
            ORIGINAL_SOURCES_CACHE.asDir!!,
            REMAPPED_SOURCES_CACHE,
            "jar",
            true
        )

        // commit remapped game jars to cache (per input)
        try {
            val remapperHash = computeSha256(MOJANG_MAPPINGS_PATH) ?: VERSION.id
            for (file in REMAPPED_SOURCES_CACHE.asDir!!) {
                val inputCandidate = INPUT_GAME_JARS.map { it.toFile().name }.find { it == file.name }
                if (inputCandidate != null) {
                    val originalPath = INPUT_GAME_JARS.find { it.toFile().name == inputCandidate }?.toFile()?.toPath()
                    val inHash = computeSha256(originalPath)
                    if (inHash != null) commitCache(inHash, remapperHash, file.toPath())
                }
            }
        } catch (_: Exception) {}

        filterRemappedJars()
    }

    private fun remapMods() {
        val config = ScriptingConfig.get()
        val filter = config.filter
        val modsToRemap = config.modsToRemap
        if (modsToRemap.isEmpty()) return

        // Expand configured mod ids to include all nested children recursively
        val expandedMods = expandModsWithChildren(modsToRemap)
        log("Remapping mods")
        try {
            for (mod in FabricLoader.getInstance().allMods) {
                val id = mod.metadata.id

                // Skip the special 'java' pseudo-mod entirely: do not remap or cache it
                if (id.equals("java", ignoreCase = true)) continue

                when (filter) {
                    ScriptingConfig.FilterOption.INCLUDED -> if (!expandedMods.contains(id)) continue
                    ScriptingConfig.FilterOption.EXCLUDED -> if (expandedMods.contains(id)) continue
                }

                val file = getModFile(id)
                if (file == null) {
                    logError("File for mod id $id is null")
                    continue
                }
                val remappedFile = REMAPPED_SOURCES_CACHE.resolve(file.name).toFile()

                // try cache
                try {
                    val inHash = computeSha256(file.toPath())
                    val remapperHash = computeSha256(MOJANG_MAPPINGS_PATH) ?: VERSION.id
                    if (inHash != null) {
                        val cached = getCachedByHashes(inHash, remapperHash)
                        if (cached != null) {
                            try {
                                Files.copy(cached, remappedFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
                                continue
                            } catch (e: Exception) {
                                logError("Failed to copy cached mod $id", e)
                                // fall through to remapping
                            }
                        }
                    }
                } catch (e: Exception) {
                    logError("Failed cache lookup for mod $id", e)
                }

                log("Remapping mod $id")
                if (!DEV_ENV) remap(
                    intToMojRemapper,
                    file,
                    remappedFile,
                    "jar",
                    buildJar = true,
                    REMAPPED_SOURCES_CACHE.asFileList!!,
                )

                // commit mod to cache
                try {
                    val inHash2 = computeSha256(file.toPath())
                    val remapperHash2 = computeSha256(MOJANG_MAPPINGS_PATH) ?: VERSION.id
                    if (inHash2 != null) commitCache(inHash2, remapperHash2, remappedFile.toPath())
                } catch (_: Exception) {}
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
