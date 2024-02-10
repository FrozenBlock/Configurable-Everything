package net.frozenblock.configurableeverything.util

import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

// CONFIG
fun text(key: String?): Component = Component.translatable("option.$MOD_ID.$key")

fun tooltip(key: String?): Component = Component.translatable("tooltip.$MOD_ID.$key")

// LOGGING
@JvmOverloads
fun log(string: String?, shouldLog: Boolean = true) {
    if (shouldLog) {
        LOGGER?.info(string)
    }
}

@JvmOverloads
fun logMod(string: String, shouldLog: Boolean = true) {
    if (shouldLog) {
        LOGGER?.info("$string $MOD_ID")
    }
}

@JvmOverloads
fun logDebug(string: String?, shouldLog: Boolean = true) {
    if (shouldLog) LOGGER?.debug(string)
}

@JvmOverloads
fun logWarn(string: String?, shouldLog: Boolean = true) {
    if (shouldLog) LOGGER?.warn(string)
}

@JvmOverloads
fun logError(string: String?, error: Throwable? = null, shouldLog: Boolean = true) {
    if (shouldLog) {
        LOGGER?.error(string, error)
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
