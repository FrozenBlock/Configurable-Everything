package net.frozenblock.configurableeverything.util

import net.minecraft.resources.ResourceLocation
import java.nio.file.Path

// LOGGING
fun log(string: String?, shouldLog: Boolean = true) {
    if (shouldLog) {
        LOGGER?.info(string)
    }
}

fun logMod(string: String, shouldLog: Boolean = true) {
    if (shouldLog) {
        LOGGER?.info("$string $MOD_ID")
    }
}

fun error(string: String?, shouldLog: Boolean = true) {
    if (shouldLog) {
        LOGGER?.error(string)
    }
}

// IDENTIFIERS

fun id(path: String): ResourceLocation {
    return ResourceLocation(MOD_ID, path)
}

fun vanillaId(path: String): ResourceLocation {
    return ResourceLocation(ResourceLocation.DEFAULT_NAMESPACE, path)
}

fun string(path: String): String {
    return id(path).toString()
}
