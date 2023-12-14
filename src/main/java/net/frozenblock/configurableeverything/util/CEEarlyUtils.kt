package net.frozenblock.configurableeverything.util

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.ModContainer
import net.fabricmc.loader.api.ObjectShare
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.jvm.optionals.getOrNull

// dont initialize minecraft classes here

// cool kotlin stuff

fun <T : Any?> T.discard(): Unit = Unit

// config

/**
 * @return The path of the config file.
 */
fun makeConfigPath(name: String?, json5: Boolean): Path
    = Path("./config/$MOD_ID/$name.${if (json5) "json5" else "json"}")

// could use default parameters (json5: Boolean = true) but this solution is java-compatible
fun makeConfigPath(name: String?): Path = makeConfigPath(name, true)

// extended features

inline fun <T : Any?> ifExtended(crossinline value: () -> T): T? {
    return if (HAS_EXTENSIONS)
        value()
    else null
}

// experimental features

@PublishedApi
internal inline val EXPERIMENTAL_EXCEPTION: Exception
    get() = UnsupportedOperationException("Experimental features are disabled")

fun experimentalOrThrow(): Nothing? = if (ENABLE_EXPERIMENTAL_FEATURES) null
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

fun modContainer(mod: String): ModContainer? = FabricLoader.getInstance().getModContainer(mod).getOrNull()

inline val ModContainer?.version: String
    get() = this?.metadata?.version.toString()
