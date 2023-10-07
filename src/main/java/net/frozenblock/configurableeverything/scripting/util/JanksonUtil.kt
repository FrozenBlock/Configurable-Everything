package net.frozenblock.configurableeverything.scripting.util

import net.frozenblock.lib.shadow.blue.endless.jankson.JsonElement
import net.frozenblock.lib.shadow.blue.endless.jankson.JsonPrimitive

fun JsonElement.getAsInt(): Int? {
    if (this is JsonPrimitive) {
        return this.asInt(0)
    }
    return null
}

fun JsonElement.getAsString(): String? {
    if (this is JsonPrimitive) {
        return this.asString()
    }
    return null
}
