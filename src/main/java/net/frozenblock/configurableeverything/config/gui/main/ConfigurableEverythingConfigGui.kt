package net.frozenblock.configurableeverything.config.gui.main

import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.*
import net.frozenblock.configurableeverything.config.gui.*
import net.frozenblock.configurableeverything.util.text
import net.minecraft.client.gui.screens.Screen

/**
 * A utility object for building Configurable Everything's config GUI.
 */
@Environment(EnvType.CLIENT)
object ConfigurableEverythingConfigGui {

    /**
     * Builds the config screen.
     */
    @JvmStatic
    fun buildScreen(parent: Screen?): Screen {
        checkNotNull(parent) { "Cannot build Configurable Everything config screen as the parent screen is null." }

        val configBuilder = ConfigBuilder.create().setParentScreen(parent).setTitle(text("component.title"))
        val entryBuilder = configBuilder.entryBuilder()

        configBuilder.setSavingRunnable {
            MainConfigGui.INSTANCE = null
            MainConfig.save()
            BiomeConfig.save()
            BiomePlacementConfig.save()
            DataFixerConfig.save()
            EntityConfig.save()
            FluidConfig.save()
            GameConfig.save()
            GravityConfig.save()
            ItemConfig.save()
            LootConfig.save()
            MixinsConfig.save()
            ScreenShakeConfig.save()
            ScriptingConfig.save()
            SculkSpreadingConfig.save()
            SplashTextConfig.save()
            StructureConfig.save()
            TagConfig.save()
            WorldConfig.save()
        }

        val main = configBuilder.getOrCreateCategory(text("main"))
        MainConfigGui.createInstance(entryBuilder, MainConfig.get(true), MainConfig.configWithSync(), MainConfig.defaultInstance()).setupEntries(main, entryBuilder)

        val biome = configBuilder.getOrCreateCategory(text("biome"))
        BiomeConfigGui.setupEntries(biome, entryBuilder)

        val biomePlacement = configBuilder.getOrCreateCategory(text("biome_placement"))
        BiomePlacementConfigGui.setupEntries(biomePlacement, entryBuilder)

        val block = configBuilder.getOrCreateCategory(text("block"))
        BlockConfigGui.setupEntries(block, entryBuilder)

        val datafixer = configBuilder.getOrCreateCategory(text("datafixer"))
        DataFixerConfigGui.setupEntries(datafixer, entryBuilder)

        val entity = configBuilder.getOrCreateCategory(text("entity"))
        EntityConfigGui.setupEntries(entity, entryBuilder)

        val fluid = configBuilder.getOrCreateCategory(text("fluid"))
        FluidConfigGui.setupEntries(fluid, entryBuilder)

        val game = configBuilder.getOrCreateCategory(text("game"))
        GameConfigGui.setupEntries(game, entryBuilder)

        val mixins = configBuilder.getOrCreateCategory(text("mixins"))
        MixinsConfigGui.setupEntries(mixins, entryBuilder)

        val screenShake = configBuilder.getOrCreateCategory(text("screen_shake"))
        ScreenShakeConfigGui.setupEntries(screenShake, entryBuilder)

        val scripting = configBuilder.getOrCreateCategory(text("scripting"))
        ScriptingConfigGui.setupEntries(scripting, entryBuilder)

        val splashText = configBuilder.getOrCreateCategory(text("splash_text"))
        SplashTextConfigGui.setupEntries(splashText, entryBuilder)

        val world = configBuilder.getOrCreateCategory(text("world"))
        WorldConfigGui.setupEntries(world, entryBuilder)

        return configBuilder.build()
    }
}
