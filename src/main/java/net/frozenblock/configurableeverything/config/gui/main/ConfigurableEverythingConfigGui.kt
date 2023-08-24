package net.frozenblock.configurableeverything.config.gui.main

import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.EntityConfig
import net.frozenblock.configurableeverything.config.GameConfig
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.config.SplashTextConfig
import net.frozenblock.configurableeverything.config.WorldConfig
import net.frozenblock.configurableeverything.config.gui.EntityConfigGui
import net.frozenblock.configurableeverything.config.gui.GameConfigGui
import net.frozenblock.configurableeverything.config.gui.MainConfigGui
import net.frozenblock.configurableeverything.config.gui.SplashTextConfigGui
import net.frozenblock.configurableeverything.config.gui.WorldConfigGui
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
            EntityConfig.getConfigInstance().save()
            GameConfig.getConfigInstance().save()
            SplashTextConfig.getConfigInstance().save()
            WorldConfig.getConfigInstance().save()
        }

        val main = configBuilder.getOrCreateCategory(text("main"))
        MainConfigGui.setupEntries(main, entryBuilder)

        val entity = configBuilder.getOrCreateCategory(text("entity"))
        EntityConfigGui.setupEntries(entity, entryBuilder)

        val game = configBuilder.getOrCreateCategory(text("game"))
        GameConfigGui.setupEntries(game, entryBuilder)

        val splashText = configBuilder.getOrCreateCategory(text("splash_text"))
        SplashTextConfigGui.setupEntries(splashText, entryBuilder)

        val world = configBuilder.getOrCreateCategory(text("world"))
        WorldConfigGui.setupEntries(world, entryBuilder)

        return configBuilder.build()
    }
}
