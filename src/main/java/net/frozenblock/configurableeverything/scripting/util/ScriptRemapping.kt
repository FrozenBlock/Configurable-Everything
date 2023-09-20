package net.frozenblock.configurableeverything.scripting.util

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

fun SourceCode.remapMinecraft(): SourceCode {
    var remappedText = this.text
    val mcVersion: MCVersion? = MCVersion.makeFromResource()
    if (mcVersion != null) {
        downloadIntermediary(mcVersion)
        downloadOfficialMojangMappings(mcVersion)
        parseMappings(mcVersion)
    }
    return StringScriptSource(remappedText, this.name)
}

/**
 * not functional yet
 */
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

fun parseMappings(mcVersion: MCVersion) {

}

fun getHttpResponse(uri: URI): HttpResponse<ByteArray> {
    val request = HttpRequest.newBuilder(uri).build()
    val client = HttpClient.newBuilder().build()
    return client.sendAsync(request, BodyHandlers.ofByteArray()).get()
}

fun intermediaryURI(mcVersion: MCVersion): URI
    = URI.create(System.getProperty("$MOD_ID.intermediary", "https://maven.fabricmc.net/net/fabricmc/intermediary/${mcVersion.id}/intermediary-${mcVersion.id}.jar"))
