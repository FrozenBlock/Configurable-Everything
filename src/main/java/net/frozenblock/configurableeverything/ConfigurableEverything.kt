package net.frozenblock.configurableeverything

import net.fabricmc.api.ModInitializer
import net.fabricmc.loader.api.FabricLoader
import net.frozenblock.configurableeverything.biome.util.BiomeConfigUtil
import net.frozenblock.configurableeverything.biome_placement.util.BiomePlacementUtils
import net.frozenblock.configurableeverything.block.util.BlockConfigUtil
import net.frozenblock.configurableeverything.config.*
import net.frozenblock.configurableeverything.datafixer.util.DataFixerUtils
import net.frozenblock.configurableeverything.entity.util.EntityConfigUtil
import net.frozenblock.configurableeverything.mod_compat.ConfigurableEverythingIntegrations
import net.frozenblock.configurableeverything.registry.util.RegistryConfigUtil
import net.frozenblock.configurableeverything.scripting.util.ScriptingUtil
import net.frozenblock.configurableeverything.splash_text.util.SplashTextConfigUtil
import net.frozenblock.configurableeverything.surface_rule.util.SurfaceRuleConfigUtil
import net.frozenblock.configurableeverything.util.*
import net.frozenblock.configurableeverything.world.util.WorldConfigUtil
import net.minecraft.FileUtil
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import java.io.IOException
import kotlin.system.measureNanoTime

class ConfigurableEverything : ModInitializer {

    override fun onInitialize() {
        val time = measureNanoTime {
            ConfigurableEverythingIntegrations.init()

            // init configs
            MainConfig
            BiomeConfig
            BiomePlacementConfig
            BlockConfig
            DataFixerConfig
            EntityConfig
            FluidConfig
            GameConfig
            if (ENABLE_EXPERIMENTAL_FEATURES)
                ItemConfig
            RegistryConfig
            ScreenShakeConfig
            SplashTextConfig
            SurfaceRuleConfig
            WorldConfig

            try {
                FileUtil.createDirectoriesSafe(DATAPACKS_PATH)
                if (HAS_EXTENSIONS) FileUtil.createDirectoriesSafe(KOTLIN_SCRIPT_PATH)
            } catch (e: IOException) {
                throw RuntimeException("Unable to create Configurable Everything folders", e)
            }
            if (HAS_EXTENSIONS) ScriptingUtil.runScripts()

            // run functionality AFTER scripts have run
            BiomeConfigUtil.init()
            BiomePlacementUtils.init()
            BlockConfigUtil.init()
            DataFixerUtils.applyDataFixes(FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow())
            EntityConfigUtil.init()
            RegistryConfigUtil.init()
            SplashTextConfigUtil.init()
            SurfaceRuleConfigUtil.init()
            WorldConfigUtil.init()
        }

        log("Configurable Everything took $time nanoseconds")
    }

    companion object {
        @JvmField
        val ARROW_FLYBY_SOUND_EVENT = register(id("flyby.arrow"), SoundEvent.createVariableRangeEvent(id("flyby.arrow")))

        @JvmField
        val TIPPED_ARROW_FLYBY_SOUND_EVENT = register(id("flyby.tipped_arrow"), SoundEvent.createVariableRangeEvent(id("flyby.tipped_arrow")))

        @JvmField
        val SPECTRAL_ARROW_FLYBY_SOUND_EVENT = register(id("flyby.spectral_arrow"), SoundEvent.createVariableRangeEvent(id("flyby.spectral_arrow")))

        @JvmField
        val TRIDENT_FLYBY_SOUND_EVENT = register(id("flyby.trident"), SoundEvent.createVariableRangeEvent(id("flyby.trident")))

        @JvmField
        val EGG_FLYBY_SOUND_EVENT = register(id("flyby.egg"), SoundEvent.createVariableRangeEvent(id("flyby.egg")))

        @JvmField
        val SNOWBALL_FLYBY_SOUND_EVENT = register(id("flyby.snowball"), SoundEvent.createVariableRangeEvent(id("flyby.snowball")))

        @JvmField
        val FIREBALL_FLYBY_SOUND_EVENT = register(id("flyby.fireball"), SoundEvent.createVariableRangeEvent(id("flyby.fireball")))

        @JvmField
        val POTION_FLYBY_SOUND_EVENT = register(id("flyby.potion"), SoundEvent.createVariableRangeEvent(id("flyby.potion")))

        @JvmField
        val EXPERIENCE_BOTTLE_FLYBY_SOUND_EVENT = register(id("flyby.experience_bottle"), SoundEvent.createVariableRangeEvent(id("flyby.experience_bottle")))

        private fun register(key: ResourceLocation, sound: SoundEvent) =
            Registry.register(BuiltInRegistries.SOUND_EVENT, key, sound)
    }

}
