package net.frozenblock.configurableeverything.util

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.ModContainer
import net.fabricmc.loader.api.ObjectShare
import net.frozenblock.lib.config.api.entry.TypedEntry
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.jvm.optionals.getOrNull
import kotlin.reflect.KProperty

// dont initialize minecraft classes here

// cool kotlin stuff

inline fun <T : Any?> T.discard(): Unit = Unit

inline var <T> TypedEntry<T>.value: T
    get() = this.value()
    set(value) = this.setValue(value)

// config

/**
 * @return The path of the config file.
 */
inline fun makeConfigPath(name: String?): Path
        = Path("./config/$MOD_ID/$name.djs")

/**
 * @return The path of the config file.
 */
inline fun makeLegacyConfigPath(name: String?, json5: Boolean = true): Path
    = Path("./config/$MOD_ID/$name.${if (json5) "json5" else "json"}")

inline fun makeThirdPartyConfigPath(name: String?): Path
    = Path("./config/$MOD_ID/thirdparty/$name.djs")

inline  fun makeLegacyThirdPartyConfigPath(name: String?, json5: Boolean = true): Path
    = Path("./config/$MOD_ID/thirdparty/$name.${if (json5) "json5" else "json"}")

// extended features

inline fun <T : Any?> ifExtended(crossinline value: () -> T): T? {
    return if (HAS_EXTENSIONS)
        value()
    else null
}

inline fun <T : Any?> ifScriptingEnabled(crossinline value: () -> T): T? {
    return if (SCRIPTING_ENABLED)
        value()
    else null
}

// experimental features

@PublishedApi
internal inline val EXPERIMENTAL_EXCEPTION: Exception
    get() = UnsupportedOperationException("Experimental features are disabled")

@Suppress("NOTHING_TO_INLINE")
inline fun experimentalOrThrow(): Nothing? = if (ENABLE_EXPERIMENTAL_FEATURES) null
    else throw EXPERIMENTAL_EXCEPTION

inline fun <T> experimental(crossinline value: () -> T): T {
    if (ENABLE_EXPERIMENTAL_FEATURES) return value()
    throw EXPERIMENTAL_EXCEPTION
}

inline fun <T : Any?> ifExperimental(crossinline value: () -> T): T? {
    return if (ENABLE_EXPERIMENTAL_FEATURES)
        value()
    else null
}

operator fun <T> TypedEntry<T>.getValue(thisRef: Any?, property: KProperty<*>): T
    = this.value()

operator fun <T> TypedEntry<T>.setValue(thisRef: Any?, property: KProperty<*>, value: T) {
    this.setValue(value)
}

// environment


// dont mark as inline
@Environment(EnvType.CLIENT)
fun <T : Any?> clientOrThrow(value: () -> T): T = value()

inline fun <T : Any?> ifClient(crossinline value: () -> T): T? {
    if (FabricLoader.getInstance().environmentType == EnvType.CLIENT)
        return value()
    return null
}

// dont mark as inline
@Environment(EnvType.SERVER)
fun <T : Any?> serverOrThrow(value: () -> T): T = value()

inline fun <T : Any?> ifServer(crossinline value: () -> T): T? {
    if (FabricLoader.getInstance().environmentType == EnvType.SERVER)
        return value()
    return null
}

// other fabric stuff

operator fun ObjectShare.set(key: String, value: Any): Any = this.put(key, value)

inline fun modContainer(mod: String): ModContainer? = FabricLoader.getInstance().getModContainer(mod).getOrNull()

inline val ModContainer?.version: String
    get() = this?.metadata?.version.toString()
