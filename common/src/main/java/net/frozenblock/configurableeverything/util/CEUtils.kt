package net.frozenblock.configurableeverything.util

import net.frozenblock.lib.config.v2.registry.ID
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier

// CONFIG
inline fun text(key: String?): Component = Component.translatable("option.$MOD_ID.$key")

inline fun tooltip(key: String?): Component = Component.translatable("tooltip.$MOD_ID.$key")

// LOGGING
@JvmOverloads
inline fun log(string: String?, shouldLog: Boolean = true) {
    if (shouldLog) LOGGER?.info(string)
}

@JvmOverloads
inline fun logMod(string: String, shouldLog: Boolean = true) {
    if (shouldLog) LOGGER?.info("$string $MOD_ID")
}

@JvmOverloads
inline fun logDebug(string: String?, shouldLog: Boolean = true) {
    if (shouldLog) LOGGER?.debug(string)
}

@JvmOverloads
inline fun logWarn(string: String?, shouldLog: Boolean = true) {
    if (shouldLog) LOGGER?.warn(string)
}

@JvmOverloads
inline fun logError(string: String?, error: Throwable? = null, shouldLog: Boolean = true) {
    if (shouldLog) LOGGER?.error(string, error)
}

// IDENTIFIERS

inline fun id(path: String): Identifier
    = Identifier.fromNamespaceAndPath(MOD_ID, path)

inline fun config(path: String): ID
    = ID(MOD_ID, path)

inline fun vanillaId(path: String): Identifier
    = Identifier.fromNamespaceAndPath(Identifier.DEFAULT_NAMESPACE, path)

inline fun string(path: String): String
    = id(path).toString()
