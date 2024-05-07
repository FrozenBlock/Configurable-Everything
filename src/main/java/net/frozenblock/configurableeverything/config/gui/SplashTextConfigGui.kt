@file:Environment(EnvType.CLIENT)

package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.clothconfig2.api.Requirement
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.SplashTextConfig
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.configurableeverything.util.tooltip
import net.frozenblock.lib.config.api.client.gui.Color
import net.frozenblock.lib.config.api.client.gui.EntryBuilder
import net.frozenblock.lib.config.api.client.gui.StringList

object SplashTextConfigGui {

    private inline val mainToggleReq: Requirement
        get() = Requirement.isTrue(MainConfigGui.INSTANCE!!.splashText)

    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        val config = SplashTextConfig.get(real = true)
        val defaultConfig = SplashTextConfig.defaultInstance()
        category.background = id("textures/config/splash_text.png")

        val added = EntryBuilder(text("added_splashes"), StringList(config.addedSplashes),
            StringList(defaultConfig.addedSplashes),
            { newValue -> config.addedSplashes = newValue.list.toMutableList() },
            tooltip("added_splashes"),
            true,
            requirement = mainToggleReq,
        ).build(entryBuilder).apply {
            category.addEntry(this)
        }

        val removed = EntryBuilder(text("removed_splashes"), StringList(config.removedSplashes),
            StringList(defaultConfig.removedSplashes),
            { newValue -> config.removedSplashes = newValue.list.toMutableList() },
            tooltip("removed_splashes"),
            true,
            requirement = mainToggleReq,
        ).build(entryBuilder).apply {
            category.addEntry(this)
        }

        val splashColor = EntryBuilder(text("splash_color"), Color(config.splashColor),
            Color(defaultConfig.splashColor),
            { newValue -> config.splashColor = newValue.color },
            tooltip("splash_color"),
            requirement = mainToggleReq,
        ).build(entryBuilder).apply {
            category.addEntry(this)
        }

        val removeVanilla = EntryBuilder(text("remove_vanilla"), config.removeVanilla,
            defaultConfig.removeVanilla,
            { newValue -> config.removeVanilla = newValue },
            tooltip("remove_vanilla"),
            requirement = mainToggleReq,
        ).build(entryBuilder).apply {
            category.addEntry(this)
        }
    }
}
