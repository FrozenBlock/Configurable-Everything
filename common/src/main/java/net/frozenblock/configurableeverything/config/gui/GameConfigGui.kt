@file:ClientOnly

package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.clothconfig2.api.Requirement
import net.frozenblock.configurableeverything.config.GameConfig
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.configurableeverything.util.tooltip
import net.frozenblock.lib.config.api.client.gui.SimpleEntryBuilder
import net.mehvahdjukaar.candlelight.api.ClientOnly

object GameConfigGui {

    private inline val mainToggleReq: Requirement
        get() = Requirement.isTrue(MainConfigGui.INSTANCE!!.game)

    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        val config = GameConfig.get(real = true)
        val defaultConfig = GameConfig.defaultInstance()

        category.addEntry(SimpleEntryBuilder(text("window_title"), config.windowTitle,
            defaultConfig.windowTitle,
            { newValue -> config.windowTitle = newValue },
            tooltip("window_title"),
            true,
            requirement = mainToggleReq
        ).build(entryBuilder))

        category.addEntry(SimpleEntryBuilder(text("version_series"), config.versionSeries,
            defaultConfig.versionSeries,
            { newValue -> config.versionSeries = newValue },
            tooltip("version_series"),
            true,
            requirement = mainToggleReq
        ).build(entryBuilder))
    }

}
