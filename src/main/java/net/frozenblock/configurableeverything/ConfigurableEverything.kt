package net.frozenblock.configurableeverything

import net.fabricmc.api.ModInitializer
import net.frozenblock.configurableeverything.biome.util.BiomeConfigUtil
import net.frozenblock.configurableeverything.biome_placement.util.BiomePlacementUtils
import net.frozenblock.configurableeverything.config.DataFixerConfig
import net.frozenblock.configurableeverything.config.FluidConfig
import net.frozenblock.configurableeverything.config.GameConfig;
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.config.ScreenShakeConfig
import net.frozenblock.configurableeverything.datafixer.util.DataFixerUtils.applyDataFixes
import net.frozenblock.configurableeverything.entity.util.EntityConfigUtil
import net.frozenblock.configurableeverything.mod_compat.ConfigurableEverythingIntegrations
import net.frozenblock.configurableeverything.registry.util.RegistryConfigUtil
import net.frozenblock.configurableeverything.splash_text.util.SplashTextConfigUtil
import net.frozenblock.configurableeverything.surface_rule.util.SurfaceRuleConfigUtil
import net.frozenblock.configurableeverything.util.DATAPACKS_PATH
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.MOD_ID
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
            applyDataFixes(MOD_CONTAINER)

            ConfigurableEverythingIntegrations.init()
            // init configs
            MainConfig.get()
            BiomeConfigUtil.init()
            BiomePlacementUtils.init()
            DataFixerConfig.get()
            EntityConfigUtil.init()
            FluidConfig.get()
            GameConfig.get()
            RegistryConfigUtil.init()
            ScreenShakeConfig.get()
            SplashTextConfigUtil.init()
            SurfaceRuleConfigUtil.init()
            WorldConfigUtil.init()

            try {
                FileUtil.createDirectoriesSafe(DATAPACKS_PATH)
            } catch (e: IOException) {
                throw RuntimeException("Unable to create Configurable Everything datapacks folder", e)
            }
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
