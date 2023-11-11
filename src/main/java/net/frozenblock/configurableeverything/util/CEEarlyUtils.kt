package net.frozenblock.configurableeverything.util

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.ModContainer
import net.fabricmc.loader.api.Version
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.jvm.optionals.getOrNull

// dont initialize minecraft classes here


// config

/**
 * @return The path of the config file.
 */
fun makeConfigPath(name: String?, json5: Boolean): Path
    = Path("./config/$MOD_ID/$name.${if (json5) "json5" else "json"}")

// could use default parameters (json5: Boolean = true) but this solution is java-compatible
fun makeConfigPath(name: String?): Path = makeConfigPath(name, true)

// extended features

fun <T : Any?> ifExtended(value: () -> T): T? {
    return if (HAS_EXTENSIONS)
        value.invoke()
    else null
}

// experimental features

fun experimentalOrThrow(): Nothing? = if (ENABLE_EXPERIMENTAL_FEATURES)
    null
else throw UnsupportedOperationException("Experimental features are disabled")

fun <T> experimental(value: () -> T): T {
    if (ENABLE_EXPERIMENTAL_FEATURES) return value.invoke()
    throw UnsupportedOperationException("Experimental features are disabled")
}
fun <T : Any?> ifExperimental(value: () -> T): T? {
    return if (ENABLE_EXPERIMENTAL_FEATURES)
        value.invoke()
    else null
}


// environment


@Environment(EnvType.CLIENT)
fun <T : Any?> clientOrThrow(value: () -> T): T = value.invoke()

fun <T : Any?> ifClient(value: () -> T): T? {
    if (FabricLoader.getInstance().environmentType == EnvType.CLIENT)
        return value.invoke()
    return null
}

@Environment(EnvType.SERVER)
fun <T : Any?> serverOrThrow(value: () -> T): T = value.invoke()

fun <T : Any?> ifServer(value: () -> T): T? {
    if (FabricLoader.getInstance().environmentType == EnvType.SERVER)
        return value.invoke()
    return null
}

// other fabric stuff

fun modContainer(mod: String): ModContainer? = FabricLoader.getInstance().getModContainer(mod).getOrNull()

val ModContainer?.version: String
    get() = this?.metadata?.version.toString()
