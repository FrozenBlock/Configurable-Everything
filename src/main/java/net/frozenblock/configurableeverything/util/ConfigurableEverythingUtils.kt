package net.frozenblock.configurableeverything.util

import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

// CONFIG
internal inline fun text(key: String?): Component = Component.translatable("option.$MOD_ID.$key")

internal inline fun tooltip(key: String?): Component = Component.translatable("tooltip.$MOD_ID.$key")

// LOGGING
@JvmOverloads
internal inline fun log(string: String?, shouldLog: Boolean = true) {
    if (shouldLog) LOGGER?.info(string)
}

@JvmOverloads
internal inline fun logMod(string: String, shouldLog: Boolean = true) {
    if (shouldLog) LOGGER?.info("$string $MOD_ID")
}

@JvmOverloads
internal inline fun logDebug(string: String?, shouldLog: Boolean = true) {
    if (shouldLog) LOGGER?.debug(string)
}

@JvmOverloads
internal inline fun logWarn(string: String?, shouldLog: Boolean = true) {
    if (shouldLog) LOGGER?.warn(string)
}

@JvmOverloads
internal inline fun logError(string: String?, error: Throwable? = null, shouldLog: Boolean = true) {
    if (shouldLog) LOGGER?.error(string, error)
}

// IDENTIFIERS

internal inline fun id(path: String): ResourceLocation
    = ResourceLocation.fromNamespaceAndPath(MOD_ID, path)

internal inline fun vanillaId(path: String): ResourceLocation
    = ResourceLocation.fromNamespaceAndPath(ResourceLocation.DEFAULT_NAMESPACE, path)

internal inline fun string(path: String): String
    = id(path).toString()
