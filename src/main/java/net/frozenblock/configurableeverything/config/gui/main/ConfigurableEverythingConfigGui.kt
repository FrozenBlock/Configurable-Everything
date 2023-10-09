package net.frozenblock.configurableeverything.config.gui.main

import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.*
import net.frozenblock.configurableeverything.config.gui.*
import net.frozenblock.configurableeverything.util.text
import net.minecraft.client.gui.screens.Screen

@Environment(EnvType.CLIENT)
object ConfigurableEverythingConfigGui {

    @JvmStatic
    fun buildScreen(parent: Screen?): Screen {
        val configBuilder = ConfigBuilder.create().setParentScreen(parent).setTitle(text("component.title"))
        val entryBuilder = configBuilder.entryBuilder()

        configBuilder.setSavingRunnable {
            MainConfig.INSTANCE.save()
            BiomeConfig.INSTANCE.save()
            BiomePlacementConfig.INSTANCE.save()
            DataFixerConfig.INSTANCE.save()
            EntityConfig.INSTANCE.save()
            FluidConfig.INSTANCE.save()
            GameConfig.INSTANCE.save()
            MixinsConfig.INSTANCE.save()
            ScreenShakeConfig.INSTANCE.save()
            SplashTextConfig.INSTANCE.save()
            WorldConfig.INSTANCE.save()
        }

        val main = configBuilder.getOrCreateCategory(text("main"))
        MainConfigGui.setupEntries(main, entryBuilder)

        //val biome = configBuilder.getOrCreateCategory(text("biome"))
        //BiomeConfigGui.setupEntries(biome, entryBuilder)

        val biomePlacement = configBuilder.getOrCreateCategory(text("biome_placement"))
        BiomePlacementConfigGui.setupEntries(biomePlacement, entryBuilder)

        val datafixer = configBuilder.getOrCreateCategory(text("datafixer"))
        DataFixerConfigGui.setupEntries(datafixer, entryBuilder)

        val entity = configBuilder.getOrCreateCategory(text("entity"))
        EntityConfigGui.setupEntries(entity, entryBuilder)

        //val fluid = configBuilder.getOrCreateCategory(text("fluid"))
        //FluidConfigGui.setupEntries(fluid, entryBuilder)

        val game = configBuilder.getOrCreateCategory(text("game"))
        GameConfigGui.setupEntries(game, entryBuilder)

        val mixins = configBuilder.getOrCreateCategory(text("mixins"))
        MixinsConfigGui.setupEntries(mixins, entryBuilder)

        val screenShake = configBuilder.getOrCreateCategory(text("screen_shake"))
        ScreenShakeConfigGui.setupEntries(screenShake, entryBuilder)

        val splashText = configBuilder.getOrCreateCategory(text("splash_text"))
        SplashTextConfigGui.setupEntries(splashText, entryBuilder)

        val world = configBuilder.getOrCreateCategory(text("world"))
        WorldConfigGui.setupEntries(world, entryBuilder)

        return configBuilder.build()
    }
}
