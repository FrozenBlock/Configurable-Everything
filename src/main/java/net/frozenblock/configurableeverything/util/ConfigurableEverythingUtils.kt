package net.frozenblock.configurableeverything.util

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

// CONFIG
fun text(key: String?): Component = Component.translatable("option.$MOD_ID.$key")

fun tooltip(key: String?): Component = Component.translatable("tooltip.$MOD_ID.$key")

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

fun logDebug(string: String?, shouldLog: Boolean = true) {
    if (shouldLog) LOGGER?.debug(string)
}

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
