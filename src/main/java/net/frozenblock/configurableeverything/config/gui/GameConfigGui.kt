package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.*
import me.shedaniel.clothconfig2.gui.entries.*
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.configurableeverything.config.GameConfig
import net.frozenblock.configurableeverything.config.gui.api.TypedEntryUtils
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.configurableeverything.util.tooltip
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.Vec3
import java.util.Optional
import java.util.function.Supplier

@Environment(EnvType.CLIENT)
object GameConfigGui {

    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        val config = GameConfig.get()
        val defaultConfig = GameConfig.getConfigInstance().defaultInstance()
        category.background = id("textures/config/game.png")

        category.addEntry(entryBuilder.startStrField(text("windowTitle"), config.windowTitle ?: "")
            .setDefaultValue("")
            .setSaveConsumer { newValue: String? -> config.windowTitle = newValue }
            .setTooltip(tooltip("windowTitle"))
            .build()
        )

        category.addEntry(entryBuilder.startStrField(text("versionSeries"), config.versionSeries ?: "")
            .setDefaultValue("")
            .setSaveConsumer { newValue: String? -> config.versionSeries = newValue }
            .setTooltip(tooltip("versionSeries"))
            .build()
        )

        category.addEntry(
            TypedEntryUtils.setupVec3TypedEntries(
                entryBuilder,
                {
                    config.testing
                },
                {
                    newValue -> config.testing = newValue
                },
                Component.literal("Test"),
                Component.literal("Vec3")
            )
        )

    }

}
