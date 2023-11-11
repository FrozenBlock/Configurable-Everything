package net.frozenblock.configurableeverything.scripting.util.remap

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.impl.launch.MappingConfiguration
import net.fabricmc.lorenztiny.TinyMappingsReader
import net.fabricmc.mappingio.MappingReader
import net.fabricmc.mappingio.MappingVisitor
import net.fabricmc.mappingio.adapter.ForwardingMappingVisitor
import net.fabricmc.mappingio.adapter.MappingSourceNsSwitch
import net.fabricmc.mappingio.format.MappingFormat
import net.fabricmc.mappingio.tree.MappingTree.ClassMapping
import net.fabricmc.mappingio.tree.MemoryMappingTree
import net.frozenblock.configurableeverything.util.*
import net.minecraft.FileUtil
import org.cadixdev.mercury.Mercury
import org.cadixdev.mercury.remapper.MercuryRemapper
import org.cadixdev.mercury.shadow.org.eclipse.jdt.core.JavaCore
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStreamReader
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
private val MAPPINGS_FILE_PATH: Path = Path(".$MOD_ID/mappings/mojang_${VERSION.id}.gz")

private lateinit var intermediaryMappings: MemoryMappingTree
private lateinit var mojangMappings: MemoryMappingTree
private lateinit var mappingsHolder: MappingsHolder

private val mojangMercury: Mercury = Mercury()
private val intermediaryMercury: Mercury = Mercury()

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

    Files.newOutputStream(MAPPINGS_FILE_PATH).use { fileOutput ->
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
        MappingReader.read(reader, MappingFormat.TINY_2, mappings)
    }

    //val switched = MemoryMappingTree()
    //mappings.accept(MappingSourceNsSwitch(switched, "official"))

    intermediaryMappings = mappings
}

@Throws(IOException::class)
private fun parseMojang() {
    val mappings = MemoryMappingTree()

    Files.newInputStream(MAPPINGS_FILE_PATH).use { fileInput ->
        GZIPInputStream(fileInput).use { gzipInput ->
            InputStreamReader(gzipInput).use { reader ->
                MappingReader.read(reader, MappingFormat.PROGUARD, mappings)
            }
        }
    }

    mappings.setSrcNamespace("named")
    mappings.setDstNamespaces(listOf("official"))

    val switchedMappings = MemoryMappingTree()
    mappings.accept(MappingSourceNsSwitch(switchedMappings, "official"))
    mojangMappings = switchedMappings
}

private fun visitMappings(visitor: MappingVisitor) {
    mojangMappings.accept(Visitor(visitor))
}

class Visitor(visitor: MappingVisitor) : ForwardingMappingVisitor(visitor) {

    private var clazz: ClassMapping? = null

    @Throws(IOException::class)
    override fun visitClass(srcName: String): Boolean {
        this.clazz = intermediaryMappings.getClass(srcName)
        val clazz = this.clazz ?: return false
        return super.visitClass(clazz.getDstName(0))
    }

    @Throws(IOException::class)
    override fun visitMethod(srcName: String?, srcDesc: String?): Boolean {
        val mapping = this.clazz?.getMethod(srcName, srcDesc) ?: return false
        return super.visitMethod(mapping.getDstName(0), mapping.getDstDesc(0))
    }

    @Throws(IOException::class)
    override fun visitField(srcName: String?, srcDesc: String?): Boolean {
        val mapping = this.clazz?.getMethod(srcName, srcDesc) ?: return false
        return super.visitField(mapping.getDstName(0), mapping.getDstDesc(0))
    }
}

@Throws(IllegalStateException::class)
private fun setupMercury() {
    setupIntMercury()
    setupMojMercury()
}

@Throws(IllegalStateException::class)
private fun setupIntMercury() {
    intermediaryMercury.sourceCompatibility = JavaCore.VERSION_17
    intermediaryMercury.processors.add(MercuryRemapper.create(TinyMappingsReader(intermediaryMappings, "intermediary", "official").read()))
    intermediaryMercury.isFlexibleAnonymousClassMemberLookups = true

    val modJars: List<Path> = INTERMEDIARY_MOD_CACHE_PATH.toFile().listFiles()?.filter { it.extension == "jar" }?.map { it.toPath() } ?: error("Mod jars are null")
    intermediaryMercury.classPath.addAll(modJars)
}

@Throws(IllegalStateException::class)
@Suppress("unchecked")
private fun setupMojMercury() {
    mojangMercury.sourceCompatibility = JavaCore.VERSION_17
    //mojangMercury.processors.add(MercuryRemapper.create(TinyMappingsReader(mojangMappings, "official", "named").read()))
    mojangMercury.isFlexibleAnonymousClassMemberLookups = true

    val gameJars: List<Path> = FabricLoader.getInstance().objectShare["fabric-loader:inputGameJars"] as? List<Path> ?: error("Input game jars is invalid")
    //mojangMercury.classPath.addAll(gameJars)
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
        mappingsHolder = MappingsHolder()
        visitMappings(mappingsHolder)
        setupMercury()

        // actually remap stuff

        //intermediaryMercury.rewrite(INTERMEDIARY_GAME_CACHE_PATH, OFFICIAL_SOURCES_CACHE)
        val randomPath: Path = Path("coolmods/")
        FileUtil.createDirectoriesSafe(randomPath)

        TODO("fix this stupid ass rewriting")
        intermediaryMercury.rewrite(Path(".fabric/"), REMAPPED_SOURCES_CACHE)
        intermediaryMercury.rewrite(INTERMEDIARY_MOD_CACHE_PATH, REMAPPED_SOURCES_CACHE)
        mojangMercury.rewrite(INTERMEDIARY_GAME_CACHE_PATH, REMAPPED_SOURCES_CACHE)
        mojangMercury.rewrite(INTERMEDIARY_MOD_CACHE_PATH, REMAPPED_SOURCES_CACHE)

        log("Successfully remapped the current codebase")
    } catch (e: Exception) {
        log("Failed to remap codebase")
        e.printStackTrace()
    }
}

private fun httpReponse(uri: URI): HttpResponse<ByteArray> {
    return CLIENT.sendAsync(makeHttpRequest(uri), BodyHandlers.ofByteArray()).get()
}
private fun makeHttpRequest(uri: URI): HttpRequest = HttpRequest.newBuilder(uri).build()
