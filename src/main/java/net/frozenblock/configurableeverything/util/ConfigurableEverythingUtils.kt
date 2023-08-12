package net.frozenblock.configurableeverything.util

import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstants.LOGGER
import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstants.MOD_ID
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.block.Block
import java.nio.file.Path

object ConfigurableEverythingUtils {

    // CONFIG
    @JvmStatic
    fun makePath(name: String, json5: Boolean): Path {
        return Path.of("./config/" + MOD_ID + "/" + name + "." + if (json5) "json5" else "json")
    }

    // LOGGING
    @JvmStatic
    fun log(string: String?, shouldLog: Boolean) {
        if (shouldLog) {
            LOGGER.info(string)
        }
    }

    @JvmStatic
    fun log(entity: Entity, string: String, shouldLog: Boolean) {
        if (shouldLog) {
            LOGGER.info(entity.toString() + " : " + string + " : " + entity.position())
        }
    }

    @JvmStatic
    fun log(block: Block, string: String, shouldLog: Boolean) {
        if (shouldLog) {
            LOGGER.info("$block : $string : ")
        }
    }

    @JvmStatic
    fun log(block: Block, pos: BlockPos, string: String, shouldLog: Boolean) {
        if (shouldLog) {
            LOGGER.info("$block : $string : $pos")
        }
    }

    @JvmStatic
    fun logMod(string: String, shouldLog: Boolean) {
        if (shouldLog) {
            LOGGER.info("$string $MOD_ID")
        }
    }

    @JvmStatic
    fun error(string: String?, shouldLog: Boolean) {
        if (shouldLog) {
            LOGGER.error(string)
        }
    }

    // MEASURING
    private val INSTANT_MAP: MutableMap<Any, Long> = HashMap()

    @JvmStatic
    fun startMeasuring(`object`: Any) {
        val started = System.nanoTime()
        val name = `object`.javaClass.name
        LOGGER.info("Started measuring {}", name.substring(name.lastIndexOf(".") + 1))
        INSTANT_MAP[`object`] = started
    }

    @JvmStatic
    fun stopMeasuring(`object`: Any) {
        if (INSTANT_MAP.containsKey(`object`)) {
            val name = `object`.javaClass.name
            LOGGER.info("{} took {} nanoseconds", name.substring(name.lastIndexOf(".") + 1), System.nanoTime() - INSTANT_MAP[`object`]!!)
            INSTANT_MAP.remove(`object`)
        }
    }

    @JvmStatic
    fun id(path: String?): ResourceLocation {
        return ResourceLocation(MOD_ID, path)
    }

    fun vanillaId(path: String?): ResourceLocation {
        return ResourceLocation(ResourceLocation.DEFAULT_NAMESPACE, path)
    }

    fun string(path: String?): String {
        return id(path).toString()
    }
}
