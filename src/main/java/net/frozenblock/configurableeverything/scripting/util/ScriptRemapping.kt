package net.frozenblock.configurableeverything.scripting.util

import net.fabricmc.mappingio.adapter.MappingSourceNsSwitch
import net.fabricmc.mappingio.format.MappingFormat
import net.fabricmc.mappingio.tree.MappingTree
import net.fabricmc.mappingio.tree.MemoryMappingTree
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.MAPPINGS_PATH
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.logError
import net.frozenblock.configurableeverything.util.mappingsFile
import net.frozenblock.lib.config.api.instance.ConfigSerialization
import net.frozenblock.lib.shadow.blue.endless.jankson.JsonArray
import net.frozenblock.lib.shadow.blue.endless.jankson.JsonObject
import java.io.ByteArrayInputStream
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers
import java.nio.file.Files
import java.util.zip.GZIPOutputStream
import kotlin.script.experimental.api.SourceCode
import kotlin.script.experimental.host.StringScriptSource

val manifestUri: URI = URI.create(System.getProperty("$MOD_ID.manifest-uri", "https://piston-meta.mojang.com/mc/game/version_manifest_v2.json"))
var remappedDefaultImports: MutableList<String> = mutableListOf()

fun SourceCode.remapMinecraft(): SourceCode {
    var remappedText = this.text
    val mcVersion: MCVersion? = MCVersion.makeFromResource()
    if (mcVersion != null) {
        downloadIntermediary(mcVersion)
        downloadOfficialMojangMappings(mcVersion)
        val holder: MappingsHolder = MappingsHolder(mutableMapOf(), mutableMapOf(), mutableMapOf())
        getMappings(mcVersion)?.let { mappings ->
            mappings.accept(MappingVisitor(mappings, ParentMappingVisitor(holder.classes, holder.methods, holder.fields)))
        }
        val defaultImports: List<String>? = MainConfig.get().kotlinScripting?.defaultImports
        if (defaultImports != null) {
            for (import in defaultImports) {
                holder.remapString(import)?.let { remappedImport ->
                    remappedDefaultImports.add(remappedImport)
                }
            }
        }
    }
    return StringScriptSource(remappedText, this.name)
}

fun downloadIntermediary(mcVersion: MCVersion) {
    mappingsFile(mcVersion, "intermediary")?.let { mappingsFile ->
        if (Files.exists(mappingsFile)) return

        val resp: HttpResponse<ByteArray> = getHttpResponse(intermediaryURI(mcVersion))
        try {
            Files.newOutputStream(mappingsFile).use { fileOutput ->
                val gzipOutput = GZIPOutputStream(fileOutput)
                gzipOutput.write(resp.body())
            }

        } catch (e: Exception) {
            logError("Failed to download Intermediary Mappings")
            e.printStackTrace()
        }
    }
}

fun downloadOfficialMojangMappings(mcVersion: MCVersion) {
    mappingsFile(mcVersion, "mojang")?.let { mappingsFile ->
        if (Files.exists(mappingsFile)) return

        val versionStr: String = mcVersion.id
        val resp: HttpResponse<ByteArray> = getHttpResponse(manifestUri)
        try {
            ByteArrayInputStream(resp.body()).use { input ->
                val json: JsonObject = ConfigSerialization.createJankson(null).load(input)
                val versions = json["versions"] as? JsonArray
                if (versions != null) {
                    for (element in versions.toArray()) {
                        if (element is JsonObject) {
                            if (versionStr != element["id"]!!.getAsString()) continue

                            val mappingsUri = URI.create(element["url"]!!.getAsString()!!)
                            val mappingsResponse: HttpResponse<ByteArray> = getHttpResponse(mappingsUri)

                            Files.newOutputStream(mappingsFile).use { fileOutput ->
                                val gzipOutput = GZIPOutputStream(fileOutput)
                                gzipOutput.write(mappingsResponse.body())
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            logError("Failed to download Official Mojang Mappings")
            e.printStackTrace()
        }
    }
}

fun getMappings(mcVersion: MCVersion): MemoryMappingTree? {
    try {
        val mappings: MemoryMappingTree = MemoryMappingTree()
        Files.newInputStream(mappingsFile(mcVersion, "intermediary")).use { fileInput ->
            val gzipInput = GZIPInputStream(fileInput)
            val reader: Reader = InputStreamReader(gzipInput);
            MappingReader.read(reader, MappingFormat.TINY_2, mappings)
        }
        Files.newInputStream(mappingsFile(mcVersion, "mojang")).use { fileInput ->
            val gzipInput = GZIPInputStream(fileInput)
            val reader: Reader = InputStreamReader(gzipInput)
            MappingReader.read(reader, MappingFormat.PROGUARD, mappings)
        }
        mappings.setSrcNamespace("named")
        mappings.setDstNamespaces(listOf("official"))

        val swapped: MemoryMappingTree = MemoryMappingTree()
        mappings.accept(MappingSourceNsSwitch(swapped, "official"))
        return swapped
    } catch (e: Exception) {
        logError("Unable to get mappings")
        e.printStackTrace()
    }
    return null
}

private class MappingVisitor(private val mappings: MemoryMappingTree, next: MappingVisitor) : ForwardingMappingVisitor(next) {

    private var currentClass: MappingTree.ClassMapping?

    override fun visitClass(srcName: String): Boolean {
        this.currentClass = mappings.getClass(srcName)
        if (this.currentClass == null) return false
        return super.visitClass(this.currentClass.getDstName(0))
    }

    override fun visitMethod(srcName: String, srcDesc: String): Boolean {
        val mapping = this.currentClass.getMethod(srcName, srcDesc)
        if (mapping == null) return false
        return super.visitMethod(mapping.getDstName(0), mapping.getDstDesc(0))
    }

    override fun visitField(srcName: String, srcDesc: String): Boolean {
        val mapping = this.currentClass.getField(srcName, srcDesc)
        if (mapping == null) return false
        return super.visitField(mapping.getDstName(0), mapping.getDstDesc(0))
    }
}

private class ParentMappingVisitor(val classes: Map<Int, String>, val methods: Map<Int, String>, val fields: Map<Int, String) : MappingVisitor {
    private var destinationNamespaceId: Int = 0
    private var srcClass: String?
    private var srcMethod: String?
    private var srcField: String?

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

    override fun visitMethodVar(lvtRowIndex: Int, lvIndex: Int, startOpIdx: Int, srcName: String) {
        return false
    }

    override fun visitDstName(targetKind: MappedElementKind, namespace: Int, name: String) {
        if (this.destinationNamespaceId != namespace) return
        var name1: String = name

        when (targetKind) {
            CLASS -> {
                val srcName: String = this.srcClass
                if (srcName == name1) return
                var classId: String?
                val separatorIndex = srcName.lastIndexOf('$')
                if (separatorIndex != -1) {
                    classId = srcName.substring(separatorIndex + 1)
                    if (!classId.startsWith("class_")) return
                    classId = classId.substring("class_".length())
                } else {
                    classId = srcName.substring("net/minecraft/class_".length())
                }

                separatorIndex = name1.indexOf('$')
                if (separatorIndex != -1) {
                    name1 = name1.substring(separatorIndex + 1)
                } else {
                    name1 = name1.replace('/', '.')
                }
                this.classes.put(Int.parseInt(classId), name1)
            }
            METHOD -> {
                val srcName: String = this.srcMethod
                if (srcName == name1) return
                val methodId = Int.parseInt(srcName.substring("method_".length()))
                this.methods.put(methodId, name1)
            }
            FIELD -> {
                val srcName: String = this.srcField
                if (srcName == name1) return
                val fieldId = Int.parseInt(srcName.substring("field_".length()))
                this.fields.put(fieldId, name1)
            }
        }
    }

    override fun visitComment(targetKind: MappedElementKind, comment: String) {}
}

fun getHttpResponse(uri: URI): HttpResponse<ByteArray> {
    val request = HttpRequest.newBuilder(uri).build()
    val client = HttpClient.newBuilder().build()
    return client.sendAsync(request, BodyHandlers.ofByteArray()).get()
}

fun intermediaryURI(mcVersion: MCVersion): URI
    = URI.create(System.getProperty("$MOD_ID.intermediary", "https://maven.fabricmc.net/net/fabricmc/intermediary/${mcVersion.id}/intermediary-${mcVersion.id}.jar"))
