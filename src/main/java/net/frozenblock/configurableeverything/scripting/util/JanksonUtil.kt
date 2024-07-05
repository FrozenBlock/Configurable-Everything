package net.frozenblock.configurableeverything.scripting.util

import blue.endless.jankson.JsonArray
import blue.endless.jankson.JsonElement
import blue.endless.jankson.JsonObject
import blue.endless.jankson.JsonPrimitive

inline val JsonElement.asInt: Int?
    get() = if (this is JsonPrimitive) this.asInt(0) else null

inline val JsonElement.asString: String?
    get() = if (this is JsonPrimitive) this.asString() else null

inline val JsonElement.asJsonObject: JsonObject?
    get() = if (this is JsonObject) this else null

inline val JsonElement.asJsonArray: JsonArray?
    get() = if (this is JsonArray) this else null
