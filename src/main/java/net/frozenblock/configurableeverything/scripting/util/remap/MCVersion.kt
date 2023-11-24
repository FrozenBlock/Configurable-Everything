package net.frozenblock.configurableeverything.scripting.util.remap

import net.frozenblock.configurableeverything.scripting.util.asInt
import net.frozenblock.configurableeverything.scripting.util.asString
import net.frozenblock.lib.config.api.instance.ConfigSerialization
import net.frozenblock.lib.shadow.blue.endless.jankson.Jankson
import net.frozenblock.lib.shadow.blue.endless.jankson.JsonObject
import java.io.IOException
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

/**
 * Represents a Minecraft version in version.json
 * @since 1.1
 */
data class MCVersion(
    val id: String,
    val name: String,
    val worldVersion: Int,
    val protocolVersion: Int,
    val buildTime: OffsetDateTime
) {
    companion object {
        private val JANKSON: Jankson = ConfigSerialization.createJankson("")

        val fromClasspath: MCVersion
            @Throws(IOException::class, IllegalStateException::class)
            get() {
                val json: JsonObject?
                MCVersion::class.java.getResourceAsStream("/version.json").use { input ->
                    checkNotNull(input) { "Cannot find version.json" }
                    json = JANKSON.load(input)
                }

                checkNotNull(json) { "Deserialized version.json is null!" }
                return fromJson(json)
            }

        @Throws(IllegalStateException::class)
        fun fromJson(json: JsonObject): MCVersion {
            val id: String = json["id"]?.asString ?: error("Version id is null")
            val name: String = json["name"]?.asString ?: error("Version name is null")
            val worldVersion: Int = json["world_version"]?.asInt ?: error("Version world_version is null")
            val protocolVersion: Int = json["protocol_version"]?.asInt ?: error("Version protocol_version is null")

            val buildTimeStr: String = json["build_time"]?.asString ?: error("Version build_time is null")
            val buildTime: OffsetDateTime = OffsetDateTime.parse(buildTimeStr, DateTimeFormatter.ISO_OFFSET_DATE_TIME)

            return MCVersion(id, name, worldVersion, protocolVersion, buildTime)
        }
    }
}
