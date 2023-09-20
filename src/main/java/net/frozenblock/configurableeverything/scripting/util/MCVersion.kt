package net.frozenblock.configurableeverything.scripting.util

import net.frozenblock.lib.config.api.instance.ConfigSerialization
import net.frozenblock.lib.shadow.blue.endless.jankson.JsonObject
import net.frozenblock.lib.shadow.blue.endless.jankson.JsonPrimitive
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

data class MCVersion(val id: String, val name: String, val worldVersion: Int, val protocolVersion: Int, val buildTime: OffsetDateTime) {
    companion object {
        private const val VERSION_FILE: String = "/version.json"

        @JvmStatic
        fun makeFromResource(): MCVersion? {
            Companion::class.java.getResourceAsStream(VERSION_FILE).use { input ->
                val jsonObject: JsonObject = ConfigSerialization.createJankson(null).load(input)
                return makeFromJson(jsonObject)
            }
            return null
        }
        @JvmStatic
        fun makeFromJson(json: JsonObject): MCVersion? {
            val id: String? = (json["id"] as? JsonPrimitive)?.asString()
            val name: String? = (json["name"] as? JsonPrimitive)?.asString()
            val worldVersion: Int? = (json["world_version"] as? JsonPrimitive)?.asInt(0)
            val protocolVersion: Int? = (json["protocol_version"] as? JsonPrimitive)?.asInt(0)
            val buildTime: String? = (json["build_time"] as? JsonPrimitive)?.asString()

            if (id != null && name != null && worldVersion != null && protocolVersion != null && buildTime != null) {
                return MCVersion(id, name, worldVersion, protocolVersion, OffsetDateTime.parse(buildTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME))
            }
            return null
        }
    }
}
