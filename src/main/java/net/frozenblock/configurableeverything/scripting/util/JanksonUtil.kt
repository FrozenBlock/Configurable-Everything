package net.frozenblock.configurableeverything.scripting.util

import net.frozenblock.lib.shadow.blue.endless.jankson.JsonArray
import net.frozenblock.lib.shadow.blue.endless.jankson.JsonElement
import net.frozenblock.lib.shadow.blue.endless.jankson.JsonObject
import net.frozenblock.lib.shadow.blue.endless.jankson.JsonPrimitive

val JsonElement.asInt: Int?
    get() {
        return if (this is JsonPrimitive) this.asInt(0) else null
    }

val JsonElement.asString: String?
    get() {
        return if (this is JsonPrimitive) this.asString() else null
    }

val JsonElement.asJsonObject: JsonObject?
    get() {
        return if (this is JsonObject) this else null
    }

val JsonElement.asJsonArray: JsonArray?
    get() {
        return if (this is JsonArray) this else null
    }
