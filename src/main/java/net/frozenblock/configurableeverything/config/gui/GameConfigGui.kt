@file:Environment(EnvType.CLIENT)

package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.clothconfig2.api.Requirement
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.GameConfig
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.configurableeverything.util.tooltip
import net.frozenblock.lib.config.api.client.gui.EntryBuilder

object GameConfigGui {

    private inline val mainToggleReq: Requirement
        get() = Requirement.isTrue(MainConfigGui.INSTANCE!!.game)

    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        val config = GameConfig.get(real = true)
        val defaultConfig = GameConfig.defaultInstance()
        category.background = id("textures/config/game.png")

        category.addEntry(EntryBuilder(text("window_title"), config.windowTitle,
            defaultConfig.windowTitle,
            { newValue -> config.windowTitle = newValue },
            tooltip("window_title"),
            true,
            requirement = mainToggleReq
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("version_series"), config.versionSeries,
            defaultConfig.versionSeries,
            { newValue -> config.versionSeries = newValue },
            tooltip("version_series"),
            true,
            requirement = mainToggleReq
        ).build(entryBuilder))
    }

}
