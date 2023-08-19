package net.frozenblock.configurableeverything

import net.fabricmc.api.ModInitializer
import net.frozenblock.configurableeverything.biome.util.BiomeConfigUtil
import net.frozenblock.configurableeverything.biome_placement.util.BiomePlacementUtils
import net.frozenblock.configurableeverything.config.DataFixerConfig
import net.frozenblock.configurableeverything.config.FluidConfig
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.config.ScreenShakeConfig
import net.frozenblock.configurableeverything.datafixer.util.DataFixerUtils.applyDataFixes
import net.frozenblock.configurableeverything.entity.util.EntityConfigUtil
import net.frozenblock.configurableeverything.mod_compat.ConfigurableEverythingIntegrations
import net.frozenblock.configurableeverything.splash_text.util.SplashTextConfigUtil
import net.frozenblock.configurableeverything.surface_rule.util.SurfaceRuleConfigUtil
import net.frozenblock.configurableeverything.util.DATAPACKS_PATH
import net.frozenblock.configurableeverything.util.MOD_CONTAINER
import net.frozenblock.configurableeverything.util.startMeasuring
import net.frozenblock.configurableeverything.util.stopMeasuring
import net.frozenblock.configurableeverything.world.util.WorldConfigUtil
import net.minecraft.FileUtil
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import java.io.IOException

class ConfigurableEverything : ModInitializer {

    override fun onInitialize() {
        startMeasuring(this)
        applyDataFixes(MOD_CONTAINER)

        ConfigurableEverythingIntegrations.init()
        // init configs
        MainConfig.get()
        BiomeConfigUtil.init()
        BiomePlacementUtils.init()
        DataFixerConfig.get()
        EntityConfigUtil.init()
        FluidConfig.get()
        ScreenShakeConfig.get()
        SplashTextConfigUtil.init()
        SurfaceRuleConfigUtil.init()
        WorldConfigUtil.init()

        try {
            FileUtil.createDirectoriesSafe(DATAPACKS_PATH)
        } catch (e: IOException) {
            throw RuntimeException("Unable to create Configurable Everything datapacks folder", e)
        }

        registerSounds()

        stopMeasuring(this)
    }

    val ARROW_WHOOSH_SOUND_EVENT = SoundEvent.createVariableRangeEvent(ResourceLocation("configurable_everything", "flyby.arrow"))

    private fun registerSounds() {
        Registry.register(BuiltInRegistries.SOUND_EVENT, ResourceLocation("configurable_everything", "flyby.arrow"), ARROW_WHOOSH_SOUND_EVENT)
    }

}
