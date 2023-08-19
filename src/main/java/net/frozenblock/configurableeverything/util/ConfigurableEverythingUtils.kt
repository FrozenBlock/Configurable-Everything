package net.frozenblock.configurableeverything.util

import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.block.Block
import java.nio.file.Path

// CONFIG
fun makeConfigPath(name: String?, json5: Boolean): Path {
    return Path.of("./config/$MOD_ID/$name.${if (json5) "json5" else "json"}")
}

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

// MEASURING
private val INSTANT_MAP: MutableMap<Any, Long> = HashMap()

fun startMeasuring(`object`: Any) {
    val started = System.nanoTime()
    val name: String = `object`.javaClass.name
    LOGGER?.info("Started measuring {}", name.substring(name.lastIndexOf(".") + 1))
    INSTANT_MAP[`object`] = started
}

fun stopMeasuring(`object`: Any) {
    if (INSTANT_MAP.containsKey(`object`)) {
        val name = `object`.javaClass.name
        LOGGER?.info("{} took {} nanoseconds", name.substring(name.lastIndexOf(".") + 1), System.nanoTime() - INSTANT_MAP[`object`]!!)
        INSTANT_MAP.remove(`object`)
    }
}

// IDENTIFIERS

fun id(path: String?): ResourceLocation? {
    return path?.let { ResourceLocation(MOD_ID, it) }
}

fun vanillaId(path: String?): ResourceLocation? {
    return path?.let { ResourceLocation(ResourceLocation.DEFAULT_NAMESPACE, it) }
}

fun string(path: String?): String {
    return id(path).toString()
}
