package net.frozenblock.configurableeverything.scripting.util

import net.frozenblock.lib.shadow.blue.endless.jankson.JsonArray
import net.frozenblock.lib.shadow.blue.endless.jankson.JsonElement
import net.frozenblock.lib.shadow.blue.endless.jankson.JsonObject
import net.frozenblock.lib.shadow.blue.endless.jankson.JsonPrimitive


inline val JsonElement.asInt: Int?
    get() = (this as? JsonPrimitive)?.asInt(0)

inline val JsonElement.asString: String?
    get() = (this as? JsonPrimitive)?.asString()

inline val JsonElement.asJsonObject: JsonObject?
    get() = this as? JsonObject

inline val JsonElement.asJsonArray: JsonArray?
    get() = this as? JsonArray
