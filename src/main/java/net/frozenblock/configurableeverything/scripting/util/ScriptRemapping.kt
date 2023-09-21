package net.frozenblock.configurableeverything.scripting.util

import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.mappingio.MappedElementKind
import net.fabricmc.mappingio.MappingReader
import net.fabricmc.mappingio.MappingVisitor
import net.fabricmc.mappingio.adapter.ForwardingMappingVisitor
import net.fabricmc.mappingio.adapter.MappingSourceNsSwitch
import net.fabricmc.mappingio.format.MappingFormat
import net.fabricmc.mappingio.tree.MappingTree
import net.fabricmc.mappingio.tree.MemoryMappingTree
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.logError
import net.frozenblock.configurableeverything.util.mappingsFile
import net.frozenblock.lib.config.api.instance.ConfigSerialization
import net.frozenblock.lib.shadow.blue.endless.jankson.Jankson
import net.frozenblock.lib.shadow.blue.endless.jankson.JsonArray
import net.frozenblock.lib.shadow.blue.endless.jankson.JsonObject
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.Reader
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import kotlin.script.experimental.api.SourceCode
import kotlin.script.experimental.host.StringScriptSource

val manifestUri: URI = URI.create(System.getProperty("$MOD_ID.manifest-uri", "https://piston-meta.mojang.com/mc/game/version_manifest_v2.json"))
var remappedDefaultImports: MutableList<String> = mutableListOf()

private var holder: MappingsHolder? = null
private val environment: String = FabricLoader.getInstance().environmentType.name.lowercase()
private var jankson: Jankson? = ConfigSerialization.createJankson(null)

fun SourceCode.remapMinecraft(): SourceCode {
    val mcVersion: MCVersion? = MCVersion.makeFromResource()
    if (mcVersion != null) {
        return downloadMappings(mcVersion)
            .thenCompose { _ ->
                if (holder == null) {
                    holder = MappingsHolder(mutableMapOf(), mutableMapOf(), mutableMapOf())
                    getMappings(mcVersion)?.let { mappings ->
                        mappings.accept(Visitor(mappings, ParentMappingVisitor(holder!!.classes, holder!!.methods, holder!!.fields)))
                    }
                }
                return@thenCompose CompletableFuture.completedFuture(holder!!)
            }.thenCompose { holder ->
                var remappedText = this.text
                val defaultImports: List<String>? = MainConfig.get().kotlinScripting?.defaultImports
                if (defaultImports != null) {
                    for (import in defaultImports) {
                        holder.remapString(import).let { remappedImport ->
                            remappedDefaultImports.add(remappedImport)
                        }
                    }
                }

                holder.remapString(remappedText).let { remapped ->
                    remappedText = remapped
                }
                return@thenCompose CompletableFuture.completedFuture(StringScriptSource(remappedText, this.name))
            }.get()
    }
    return this
}

fun downloadMappings(mcVersion: MCVersion): CompletableFuture<Void> {
    return CompletableFuture.completedFuture(null)
        .thenComposeAsync {
            downloadIntermediary(mcVersion)
            downloadOfficialMojangMappings(mcVersion)
        }
}

fun downloadIntermediary(mcVersion: MCVersion): CompletableFuture<Void>? {
    mappingsFile(mcVersion, "intermediary")?.let { mappingsFile ->
        if (Files.exists(mappingsFile)) return@let

        val uri = intermediaryURI(mcVersion)
        return getHttpResponse(uri)?.thenAccept { resp ->
            try {
                val jarBytes = getMappingsFromJar(resp.body())
                val stringResp: String = getStrFromJar(resp.body())
                Files.newOutputStream(mappingsFile).use { fileOutput ->
                    val gzipOutput = GZIPOutputStream(fileOutput)
                    gzipOutput.write(jarBytes)
                }

            } catch (e: Exception) {
                logError("Failed to download Intermediary Mappings")
                e.printStackTrace()
            }
        }
    }
    return CompletableFuture.completedFuture(null)
}

fun getMappingsFromJar(jarBytes: ByteArray): ByteArray {
    try {
        val path: Path = Files.createTempFile(null, ".jar")
        try {
            Files.write(path, jarBytes)
            FileSystems.newFileSystem(path).use { jar ->
                return Files.readAllBytes(jar.getPath("/mappings", "mappings.tiny"))
            }
        } finally {
            Files.delete(path)
        }
    } catch (e: IOException) {
        throw RuntimeException(e)
    }
}

fun getStrFromJar(jarBytes: ByteArray): String {
    try {
        val path: Path = Files.createTempFile(null, ".jar")
        try {
            Files.write(path, jarBytes)
            FileSystems.newFileSystem(path).use { jar ->
                return Files.readString(jar.getPath("/mappings", "mappings.tiny"))
            }
        } finally {
            Files.delete(path)
        }
    } catch (e: IOException) {
        throw RuntimeException(e)
    }
}

fun downloadOfficialMojangMappings(mcVersion: MCVersion): CompletableFuture<Void>? {
    mappingsFile(mcVersion, "mojang")?.let { mappingsFile ->
        if (Files.exists(mappingsFile)) return@let

        return getHttpResponse(manifestUri)?.thenCompose { resp ->
            getMojmapsUri(mcVersion, resp)
        }?.thenCompose { resp ->
            getMojmapsDownloadUri(resp)
        }?.thenAccept { resp ->
            try {
                Files.newOutputStream(mappingsFile).use { fileOutput ->
                    val gzipOutput = GZIPOutputStream(fileOutput)
                    gzipOutput.write(resp.body())
                }
            } catch(e: IOException) {
                throw RuntimeException(e)
            }
        }
    }
    return CompletableFuture.completedFuture(null)
}

fun getMojmapsUri(mcVersion: MCVersion, resp: HttpResponse<ByteArray>): CompletableFuture<HttpResponse<ByteArray>>? {
    val versionStr: String = mcVersion.id
    try {
        val json: JsonObject?
        ByteArrayInputStream(resp.body()).use { input ->
            json = jankson?.load(input)
        }
        val versions = json!!["versions"] as? JsonArray
        if (versions != null) {
            for (element in versions.toArray()) {
                if (element is JsonObject) {
                    if (versionStr != element["id"]!!.getAsString()) continue

                    val mappingsUri = URI.create(element["url"]!!.getAsString()!!)
                    return getHttpResponse(mappingsUri)
                }
            }
        }
    } catch (e: Exception) {
        logError("Failed to download Official Mojang Mappings")
        e.printStackTrace()
    }
    return CompletableFuture.completedFuture(null)
}

fun getMojmapsDownloadUri(resp: HttpResponse<ByteArray>): CompletableFuture<HttpResponse<ByteArray>>? {
    var json: JsonObject?
    try {
        ByteArrayInputStream(resp.body()).use { input ->
            json = jankson?.load(input)
        }
    } catch (e: IOException) {
        throw RuntimeException(e)
    }
    val data = json?.getObject("downloads")?.getObject("${environment}_mappings")!!
    val uri: URI = data["url"]?.getAsString()!!.uri()
    return getHttpResponse(uri)
}

fun getMappings(mcVersion: MCVersion): MemoryMappingTree? {
    try {
        val mappings = MemoryMappingTree()
        mappingsFile(mcVersion, "intermediary")?.let { file ->
            Files.newInputStream(file).use { fileInput ->
                val gzipInput = GZIPInputStream(fileInput)
                val reader: Reader = gzipInput.reader()
                MappingReader.read(reader, MappingFormat.TINY_2, mappings)
            }
        }
        mappingsFile(mcVersion, "mojang")?.let { file ->
            Files.newInputStream(file).use { fileInput ->
                val gzipInput = GZIPInputStream(fileInput)
                val reader: Reader = InputStreamReader(gzipInput)
                MappingReader.read(reader, MappingFormat.PROGUARD, mappings)
            }
        }
        mappings.setSrcNamespace("named")
        mappings.setDstNamespaces(listOf("official"))

        val swapped = MemoryMappingTree()
        mappings.accept(MappingSourceNsSwitch(swapped, "official"))
        return swapped
    } catch (e: Exception) {
        logError("Unable to get mappings")
        e.printStackTrace()
    }
    return null
}

private class Visitor(private val mappings: MemoryMappingTree, next: MappingVisitor) : ForwardingMappingVisitor(next) {

    private var currentClass: MappingTree.ClassMapping? = null

    override fun visitClass(srcName: String): Boolean {
        this.currentClass = mappings.getClass(srcName) ?: return false
        return super.visitClass(this.currentClass!!.getDstName(0))
    }

    override fun visitMethod(srcName: String, srcDesc: String): Boolean {
        val mapping = this.currentClass?.getMethod(srcName, srcDesc) ?: return false
        return super.visitMethod(mapping.getDstName(0), mapping.getDstDesc(0))
    }

    override fun visitField(srcName: String, srcDesc: String): Boolean {
        val mapping = this.currentClass?.getField(srcName, srcDesc) ?: return false
        return super.visitField(mapping.getDstName(0), mapping.getDstDesc(0))
    }
}

private class ParentMappingVisitor(val classes: MutableMap<Int, String?>, val methods: MutableMap<Int, String?>, val fields: MutableMap<Int, String?>) : MappingVisitor {
    private var destinationNamespaceId: Int = 0
    private var srcClass: String? = null
    private var srcMethod: String? = null
    private var srcField: String? = null

    override fun visitNamespaces(srcNamespace: String, destinationNamespaces: List<String>) {
        val index = destinationNamespaces.indexOf("named")
        if (index != -1) this.destinationNamespaceId = index
    }

    override fun visitClass(srcName: String): Boolean {
        this.srcClass = srcName
        return true
    }

    override fun visitMethod(srcName: String, srcDesc: String): Boolean {
        this.srcMethod = srcName
        return true
    }

    override fun visitField(srcName: String, srcDesc: String): Boolean {
        this.srcField = srcName
        return true
    }

    override fun visitMethodArg(argPos: Int, lvIndex: Int, srcName: String): Boolean {
        return false
    }

    override fun visitMethodVar(lvtRowIndex: Int, lvIndex: Int, startOpIdx: Int, srcName: String): Boolean {
        return false
    }

    override fun visitDstName(targetKind: MappedElementKind, namespace: Int, name: String) {
        if (this.destinationNamespaceId != namespace) return
        var name1: String = name

        when (targetKind) {
            MappedElementKind.CLASS -> {
                val srcName: String? = this.srcClass
                if (srcName == name1 || srcName == null) return
                var classId: String?
                var separatorIndex: Int = srcName.lastIndexOf('$')
                if (separatorIndex != -1) {
                    classId = srcName.substring(separatorIndex + 1)
                    if (!classId.startsWith("class_")) return
                    classId = classId.substring("class_".length)
                } else {
                    classId = srcName.substring("net/minecraft/class_".length)
                }

                separatorIndex = name1.indexOf('$')
                if (separatorIndex != -1) {
                    name1 = name1.substring(separatorIndex + 1)
                } else {
                    name1 = name1.replace('/', '.')
                }
                this.classes[Integer.parseInt(classId)] = name1
            }
            MappedElementKind.METHOD -> {
                val srcName: String? = this.srcMethod
                if (srcName == name1 || srcName == null) return
                val methodId = Integer.parseInt(srcName.substring("method_".length))
                this.methods[methodId] = name1
            }
            MappedElementKind.FIELD -> {
                val srcName: String? = this.srcField
                if (srcName == name1 || srcName == null) return
                val fieldId = Integer.parseInt(srcName.substring("field_".length))
                this.fields[fieldId] = name1
            }
            else -> {}
        }
    }

    override fun visitComment(targetKind: MappedElementKind, comment: String) {}
}

fun getHttpResponse(uri: URI): CompletableFuture<HttpResponse<ByteArray>>? {
    val request = HttpRequest.newBuilder(uri).build()
    val client = HttpClient.newBuilder().build()
    return client.sendAsync(request, BodyHandlers.ofByteArray())
}

fun intermediaryURI(mcVersion: MCVersion): URI
    = URI.create(System.getProperty("$MOD_ID.intermediary", "https://maven.fabricmc.net/net/fabricmc/intermediary/${mcVersion.id}/intermediary-${mcVersion.id}-v2.jar"))

fun String.uri(): URI = URI.create(this)
