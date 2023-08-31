package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.*
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.GameConfig
import net.frozenblock.configurableeverything.config.gui.api.EntryBuilder
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.configurableeverything.util.tooltip

@Environment(EnvType.CLIENT)
object GameConfigGui {

    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        val config = GameConfig.get()
        val defaultConfig = GameConfig.getConfigInstance().defaultInstance()
        category.background = id("textures/config/game.png")

        category.addEntry(EntryBuilder(text("window_title"), config.windowTitle,
            defaultConfig.windowTitle,
            { newValue: String? -> config.windowTitle = newValue },
            tooltip("window_title")
        ).build(entryBuilder))

        category.addEntry(entryBuilder.startStrField(text("game_series"), config.versionSeries ?: "")
            .setDefaultValue("")
            .setSaveConsumer { newValue: String? -> config.versionSeries = newValue }
            .setTooltip(tooltip("game_series"))
            .build()
        )
    }

}
