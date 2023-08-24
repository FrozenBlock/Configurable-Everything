package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.GameConfig
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.configurableeverything.util.tooltip
import net.frozenblock.lib.config.clothconfig.FrozenClothConfig

@Environment(EnvType.CLIENT)
object GameConfigGui {
    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        val config = GameConfig.get()
        category.background = id("textures/config/game.png")

        category.addEntry(entryBuilder.startStrField(text("windowTitle"), config.windowTitle ?: "")
            .setDefaultValue("")
            .setSaveConsumer { newValue: String? -> config.windowTitle = newValue }
            .setTooltip(tooltip("windowTitle"))
            .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
            .build()
        )

        category.addEntry(entryBuilder.startStrField(text("versionSeries"), config.versionSeries ?: "")
            .setDefaultValue("")
            .setSaveConsumer { newValue: String? -> config.versionSeries = newValue }
            .setTooltip(tooltip("versionSeries"))
            .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
            .build()
        )
    }
}
