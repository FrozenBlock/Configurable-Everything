package net.frozenblock.configurableeverything

import net.frozenblock.configurableeverything.config.BiomeConfig
import net.frozenblock.configurableeverything.config.BiomePlacementConfig
import net.frozenblock.configurableeverything.config.BlockConfig
import net.frozenblock.configurableeverything.config.DataFixerConfig
import net.frozenblock.configurableeverything.config.EntityConfig
import net.frozenblock.configurableeverything.config.FluidConfig
import net.frozenblock.configurableeverything.config.GravityConfig
import net.frozenblock.configurableeverything.config.ItemConfig
import net.frozenblock.configurableeverything.config.LootConfig
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.config.RegistryConfig
import net.frozenblock.configurableeverything.config.ScreenShakeConfig
import net.frozenblock.configurableeverything.config.ScriptingConfig
import net.frozenblock.configurableeverything.config.SculkSpreadingConfig
import net.frozenblock.configurableeverything.config.SplashTextConfig
import net.frozenblock.configurableeverything.config.StructureConfig
import net.frozenblock.configurableeverything.config.SurfaceRuleConfig
import net.frozenblock.configurableeverything.config.TagConfig
import net.frozenblock.configurableeverything.config.WorldConfig
import net.frozenblock.configurableeverything.scripting.util.ScriptingUtil
import net.frozenblock.configurableeverything.util.DATAPACKS_PATH
import net.frozenblock.configurableeverything.util.KOTLIN_CLIENT_SCRIPT_PATH
import net.frozenblock.configurableeverything.util.KOTLIN_SCRIPT_PATH
import net.frozenblock.configurableeverything.util.MAPPINGS_PATH
import net.frozenblock.configurableeverything.util.ORIGINAL_SOURCES_CACHE
import net.frozenblock.configurableeverything.util.REMAPPED_SOURCES_CACHE
import net.frozenblock.configurableeverything.util.ifClient
import net.frozenblock.configurableeverything.util.ifExtended
import net.frozenblock.configurableeverything.util.ifScriptingEnabled
import net.frozenblock.configurableeverything.util.log
import net.minecraft.util.FileUtil
import java.io.IOException
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.deleteRecursively
import kotlin.system.measureNanoTime

object ConfigurableEverything {

    @OptIn(ExperimentalPathApi::class)
    fun init() {
        val time = measureNanoTime {
            // init configs
            MainConfig
            BiomeConfig
            BiomePlacementConfig
            BlockConfig
            DataFixerConfig
            EntityConfig
            FluidConfig
            //GameConfig
            GravityConfig
            ItemConfig
            LootConfig
            //ModProtocolConfig
            //RecipeConfig
            RegistryConfig
            ScreenShakeConfig
            ScriptingConfig
            SculkSpreadingConfig
            ifClient {
                SplashTextConfig
            }
            SurfaceRuleConfig
            StructureConfig
            TagConfig
            WorldConfig

            try {
                FileUtil.createDirectoriesSafe(DATAPACKS_PATH)
                ifExtended {
                    FileUtil.createDirectoriesSafe(KOTLIN_SCRIPT_PATH)
                    ifClient {
                        FileUtil.createDirectoriesSafe(KOTLIN_CLIENT_SCRIPT_PATH)
                    }

                    // Remove remapping remnants
                    MAPPINGS_PATH.deleteRecursively()
                    ORIGINAL_SOURCES_CACHE.deleteRecursively()
                    REMAPPED_SOURCES_CACHE.deleteRecursively()
                }
            } catch (e: IOException) {
                throw RuntimeException("Unable to create Configurable Everything folders", e)
            }
            ifScriptingEnabled {
                ScriptingUtil.runScripts()
            }
        }
        log("Configurable Everything init took $time nanoseconds")
    }

    fun setup() {}
}
