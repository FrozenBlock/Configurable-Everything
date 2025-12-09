package net.frozenblock.configurableeverything.scripting.util

import blue.endless.jankson.JsonArray
import blue.endless.jankson.JsonElement
import blue.endless.jankson.JsonObject
import blue.endless.jankson.JsonPrimitive


inline val JsonElement.asInt: Int?
    get() = (this as? JsonPrimitive)?.asInt(0)

inline val JsonElement.asString: String?
    get() = (this as? JsonPrimitive)?.asString()

inline val JsonElement.asJsonObject: JsonObject?
    get() = this as? JsonObject

inline val JsonElement.asJsonArray: JsonArray?
    get() = this as? JsonArray
