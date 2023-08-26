package net.frozenblock.configurableeverything.util

import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.block.Block
import java.nio.file.Path

// CONFIG
/**
 * @return The path of the config file.
 */
fun makeConfigPath(name: String?, json5: Boolean): Path {
    return Path.of("./config/$MOD_ID/$name.${if (json5) "json5" else "json"}")
}

// could use default parameters (json5: Boolean = true) but this solution is java-compatible
fun makeConfigPath(name: String?): Path = makeConfigPath(name, true)

fun text(key: String?): Component = Component.translatable("option.$MOD_ID.$key")

fun tooltip(key: String?): Component = Component.translatable("tooltip.$MOD_ID.$key")

// LOGGING
fun log(string: String?, shouldLog: Boolean) {
    if (shouldLog) {
        LOGGER?.info(string)
    }
}

fun log(entity: Entity, string: String, shouldLog: Boolean) {
    if (shouldLog) {
        LOGGER?.info(entity.toString() + " : " + string + " : " + entity.position())
    }
}

fun log(block: Block, string: String, shouldLog: Boolean) {
    if (shouldLog) {
        LOGGER?.info("$block : $string : ")
    }
}

fun log(block: Block, pos: BlockPos, string: String, shouldLog: Boolean) {
    if (shouldLog) {
        LOGGER?.info("$block : $string : $pos")
    }
}

fun logMod(string: String, shouldLog: Boolean) {
    if (shouldLog) {
        LOGGER?.info("$string $MOD_ID")
    }
}

fun error(string: String?, shouldLog: Boolean) {
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
