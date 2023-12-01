package net.frozenblock.configurableeverything

import net.fabricmc.api.ModInitializer
import net.fabricmc.loader.api.FabricLoader
import net.frozenblock.configurableeverything.biome.util.BiomeConfigUtil
import net.frozenblock.configurableeverything.biome_placement.util.BiomePlacementUtil
import net.frozenblock.configurableeverything.block.util.BlockConfigUtil
import net.frozenblock.configurableeverything.config.*
import net.frozenblock.configurableeverything.datafixer.util.DataFixerUtil
import net.frozenblock.configurableeverything.entity.util.EntityConfigUtil
import net.frozenblock.configurableeverything.gravity.util.GravityConfigUtil
import net.frozenblock.configurableeverything.registry.util.RegistryConfigUtil
import net.frozenblock.configurableeverything.scripting.util.ScriptingUtil
import net.frozenblock.configurableeverything.scripting.util.remap.remapCodebase
import net.frozenblock.configurableeverything.splash_text.util.SplashTextConfigUtil
import net.frozenblock.configurableeverything.surface_rule.util.SurfaceRuleConfigUtil
import net.frozenblock.configurableeverything.util.*
import net.frozenblock.configurableeverything.world.util.WorldConfigUtil
import net.minecraft.FileUtil
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import java.io.File
import java.io.IOException
import kotlin.system.measureNanoTime

/**
 * Mod initializer for Configurable Everything.
 */
class ConfigurableEverything : ModInitializer {

    override fun onInitialize() {
        val time = measureNanoTime {
            //ConfigurableEverythingIntegrations.init()

            // init configs
            MainConfig
            BiomeConfig
            BiomePlacementConfig
            BlockConfig
            DataFixerConfig
            EntityConfig
            FluidConfig
            GameConfig
            ifExperimental {
                GravityConfig
                ItemConfig
            }
            RegistryConfig
            ScreenShakeConfig
            ScriptingConfig
            ifClient {
                SplashTextConfig
            }
            SurfaceRuleConfig
            ifExperimental {
                StructureConfig
            }
            WorldConfig

            try {
                FileUtil.createDirectoriesSafe(DATAPACKS_PATH)
                ifExtended {
                    FileUtil.createDirectoriesSafe(KOTLIN_SCRIPT_PATH)
                    ifClient {
                        FileUtil.createDirectoriesSafe(KOTLIN_CLIENT_SCRIPT_PATH)
                    }
                    File(".$MOD_ID/original_scripts/").recreateDir()
                    File(".$MOD_ID/remapped_scripts/").recreateDir()
                }

                ifExperimental {
                    FileUtil.createDirectoriesSafe(MAPPINGS_PATH)
                }
            } catch (e: IOException) {
                throw RuntimeException("Unable to create Configurable Everything folders", e)
            }
            ifExtended {
                ifExperimental {
                    remapCodebase()
                }
                ScriptingUtil.runScripts()
            }

            // run functionality AFTER scripts have run
            BiomeConfigUtil
            BiomePlacementUtil
            BlockConfigUtil
            DataFixerUtil.applyDataFixes(FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow())
            EntityConfigUtil
            ifExperimental {
                GravityConfigUtil
            }
            RegistryConfigUtil
            ifClient {
                SplashTextConfigUtil
            }
            SurfaceRuleConfigUtil
            WorldConfigUtil
        }

        log("Configurable Everything took $time nanoseconds")
    }

    companion object {
        @JvmField
        val ARROW_FLYBY: SoundEvent = register(id("flyby.arrow"), SoundEvent.createVariableRangeEvent(id("flyby.arrow")))

        @JvmField
        val TIPPED_ARROW_FLYBY: SoundEvent = register(id("flyby.tipped_arrow"), SoundEvent.createVariableRangeEvent(id("flyby.tipped_arrow")))

        @JvmField
        val SPECTRAL_ARROW_FLYBY: SoundEvent = register(id("flyby.spectral_arrow"), SoundEvent.createVariableRangeEvent(id("flyby.spectral_arrow")))

        @JvmField
        val TRIDENT_FLYBY: SoundEvent = register(id("flyby.trident"), SoundEvent.createVariableRangeEvent(id("flyby.trident")))

        @JvmField
        val EGG_FLYBY: SoundEvent = register(id("flyby.egg"), SoundEvent.createVariableRangeEvent(id("flyby.egg")))

        @JvmField
        val SNOWBALL_FLYBY: SoundEvent = register(id("flyby.snowball"), SoundEvent.createVariableRangeEvent(id("flyby.snowball")))

        @JvmField
        val FIREBALL_FLYBY: SoundEvent = register(id("flyby.fireball"), SoundEvent.createVariableRangeEvent(id("flyby.fireball")))

        @JvmField
        val POTION_FLYBY: SoundEvent = register(id("flyby.potion"), SoundEvent.createVariableRangeEvent(id("flyby.potion")))

        @JvmField
        val EXPERIENCE_BOTTLE_FLYBY: SoundEvent = register(id("flyby.experience_bottle"), SoundEvent.createVariableRangeEvent(id("flyby.experience_bottle")))

        private fun register(key: ResourceLocation, sound: SoundEvent) =
            Registry.register(BuiltInRegistries.SOUND_EVENT, key, sound)
    }

}
