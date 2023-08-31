package net.frozenblock.configurableeverything.config.gui.main

import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.DataFixerConfig
import net.frozenblock.configurableeverything.config.EntityConfig
import net.frozenblock.configurableeverything.config.GameConfig
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.config.ScreenShakeConfig
import net.frozenblock.configurableeverything.config.SplashTextConfig
import net.frozenblock.configurableeverything.config.WorldConfig
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
            MainConfig.getConfigInstance().save()
            DataFixerConfig.getConfigInstance().save()
            EntityConfig.getConfigInstance().save()
            GameConfig.getConfigInstance().save()
            ScreenShakeConfig.getConfigInstance().save()
            SplashTextConfig.getConfigInstance().save()
            WorldConfig.getConfigInstance().save()
        }

        val main = configBuilder.getOrCreateCategory(text("main"))
        MainConfigGui.setupEntries(main, entryBuilder)

        val datafixer = configBuilder.getOrCreateCategory(text("datafixer"))
        DataFixerConfigGui.setupEntries(datafixer, entryBuilder)

        val entity = configBuilder.getOrCreateCategory(text("entity"))
        EntityConfigGui.setupEntries(entity, entryBuilder)

        val game = configBuilder.getOrCreateCategory(text("game"))
        GameConfigGui.setupEntries(game, entryBuilder)

        val screenShake = configBuilder.getOrCreateCategory(text("screen_shake"))
        ScreenShakeConfigGui.setupEntries(screenShake, entryBuilder)

        val splashText = configBuilder.getOrCreateCategory(text("splash_text"))
        SplashTextConfigGui.setupEntries(splashText, entryBuilder)

        val world = configBuilder.getOrCreateCategory(text("world"))
        WorldConfigGui.setupEntries(world, entryBuilder)

        return configBuilder.build()
    }
}
