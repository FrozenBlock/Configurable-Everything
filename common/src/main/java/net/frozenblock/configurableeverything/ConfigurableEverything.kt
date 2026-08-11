package net.frozenblock.configurableeverything

import net.frozenblock.configurableeverything.biome.util.BiomeConfigUtil
import net.frozenblock.configurableeverything.biome_placement.util.BiomePlacementUtil
import net.frozenblock.configurableeverything.block.util.BlockConfigUtil
import net.frozenblock.configurableeverything.config.BiomeConfig
import net.frozenblock.configurableeverything.config.BiomePlacementConfig
import net.frozenblock.configurableeverything.config.BlockConfig
import net.frozenblock.configurableeverything.config.DataFixerConfig
import net.frozenblock.configurableeverything.config.EntityConfig
import net.frozenblock.configurableeverything.config.FluidConfig
import net.frozenblock.configurableeverything.config.GameConfig
import net.frozenblock.configurableeverything.config.GravityConfig
import net.frozenblock.configurableeverything.config.ItemConfig
import net.frozenblock.configurableeverything.config.LootConfig
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.config.ModProtocolConfig
import net.frozenblock.configurableeverything.config.RecipeConfig
import net.frozenblock.configurableeverything.config.RegistryConfig
import net.frozenblock.configurableeverything.config.ScreenShakeConfig
import net.frozenblock.configurableeverything.config.ScriptingConfig
import net.frozenblock.configurableeverything.config.SculkSpreadingConfig
import net.frozenblock.configurableeverything.config.SplashTextConfig
import net.frozenblock.configurableeverything.config.StructureConfig
import net.frozenblock.configurableeverything.config.SurfaceRuleConfig
import net.frozenblock.configurableeverything.config.TagConfig
import net.frozenblock.configurableeverything.config.WorldConfig
import net.frozenblock.configurableeverything.datafixer.util.DataFixerUtil
import net.frozenblock.configurableeverything.scripting.util.ScriptingUtil
import net.frozenblock.configurableeverything.util.DATAPACKS_PATH
import net.frozenblock.configurableeverything.util.KOTLIN_CLIENT_SCRIPT_PATH
import net.frozenblock.configurableeverything.util.KOTLIN_SCRIPT_PATH
import net.frozenblock.configurableeverything.util.MAPPINGS_PATH
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.ORIGINAL_SOURCES_CACHE
import net.frozenblock.configurableeverything.util.REMAPPED_SOURCES_CACHE
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.ifClient
import net.frozenblock.configurableeverything.util.ifExtended
import net.frozenblock.configurableeverything.util.ifScriptingEnabled
import net.frozenblock.configurableeverything.util.log
import net.frozenblock.lib.platform.api.registry.FrozenDeferredRegister
import net.frozenblock.lib.platform.api.registry.FrozenHolder
import net.minecraft.core.registries.Registries
import net.minecraft.sounds.SoundEvent
import net.minecraft.util.FileUtil
import java.io.IOException
import java.util.function.Supplier
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
            GameConfig
            GravityConfig
            ItemConfig
            LootConfig
            ModProtocolConfig
            RecipeConfig
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

            // run functionality AFTER scripts have run
            BiomeConfigUtil.init()
            BiomePlacementUtil.init()
            BlockConfigUtil.init()
            DataFixerUtil.applyDataFixes()
        }
        log("Configurable Everything init took $time nanoseconds")
    }

    fun setup() {}

    private val REGISTER = FrozenDeferredRegister.create(
        Registries.SOUND_EVENT,
        MOD_ID
    )

    @JvmField
    val ARROW_FLYBY: FrozenHolder<SoundEvent, SoundEvent> = register("flyby.arrow") {
        SoundEvent.createVariableRangeEvent(
            id("flyby.arrow")
        )
    }

    @JvmField
    val TIPPED_ARROW_FLYBY: FrozenHolder<SoundEvent, SoundEvent> = register("flyby.tipped_arrow") {
        SoundEvent.createVariableRangeEvent(id("flyby.tipped_arrow"))
    }

    @JvmField
    val SPECTRAL_ARROW_FLYBY: FrozenHolder<SoundEvent, SoundEvent> = register("flyby.spectral_arrow") {
        SoundEvent.createVariableRangeEvent(id("flyby.spectral_arrow"))
    }

    @JvmField
    val TRIDENT_FLYBY: FrozenHolder<SoundEvent, SoundEvent> = register("flyby.trident") {
        SoundEvent.createVariableRangeEvent(id("flyby.trident"))
    }

    @JvmField
    val EGG_FLYBY: FrozenHolder<SoundEvent, SoundEvent> = register("flyby.egg") {
        SoundEvent.createVariableRangeEvent(id("flyby.egg"))
    }

    @JvmField
    val SNOWBALL_FLYBY: FrozenHolder<SoundEvent, SoundEvent> = register("flyby.snowball") {
        SoundEvent.createVariableRangeEvent(id("flyby.snowball"))
    }

    @JvmField
    val FIREBALL_FLYBY: FrozenHolder<SoundEvent, SoundEvent> = register("flyby.fireball") {
        SoundEvent.createVariableRangeEvent(id("flyby.fireball"))
    }

    @JvmField
    val POTION_FLYBY: FrozenHolder<SoundEvent, SoundEvent> = register("flyby.potion") {
        SoundEvent.createVariableRangeEvent(id("flyby.potion"))
    }

    @JvmField
    val EXPERIENCE_BOTTLE_FLYBY: FrozenHolder<SoundEvent, SoundEvent> = register("flyby.experience_bottle") {
        SoundEvent.createVariableRangeEvent(id("flyby.experience_bottle"))
    }

    init {
        REGISTER.register()
    }

        private fun register(key: String, sound: Supplier<SoundEvent>): FrozenHolder<SoundEvent, SoundEvent> =
            REGISTER.register(key, sound)
}
