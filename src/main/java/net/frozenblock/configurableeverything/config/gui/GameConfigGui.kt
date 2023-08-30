package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.*
import me.shedaniel.clothconfig2.gui.entries.*
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.configurableeverything.config.GameConfig
import net.frozenblock.configurableeverything.config.api.MutableVec3
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

        category.addEntry(EntryBuilder(text("windowTitle"), config.windowTitle,
            defaultConfig.windowTitle,
            { newValue: String? -> config.windowTitle = newValue }
            tooltip("windowTitle")
        ).build(entryBuilder))

        category.addEntry(entryBuilder.startStrField(text("versionSeries"), config.versionSeries ?: "")
            .setDefaultValue("")
            .setSaveConsumer { newValue: String? -> config.versionSeries = newValue }
            .setTooltip(tooltip("versionSeries"))
            .build()
        )

        category.addEntry(
            TypedEntryUtils.makeTypedEntryList(
                entryBuilder,
                Component.literal("Test"),
                { config.testing },
                { defaultConfig.testing },
                Component.literal("Cool tooltip"),
                { newValue -> config.testing = newValue },
                { element, nestedListListEntry ->
                    val usedValue: MutableVec3 = element ?: MutableVec3(1.0, 1.0, 1.0)
                    TypedEntryUtils.makeMultiElementEntry(
                        Component.literal("Vec3"),
                        usedValue,
                        true,
                        EntryBuilder(Component.literal("x"), usedValue.x,
                            0.0,
                            usedValue::setX,
                            null
                        ).build(entryBuilder),
                        EntryBuilder(Component.literal("y"), usedValue.y
                            0.0,
                            usedValue::setY,
                            null
                        ).build(entryBuilder),
                        EntryBuilder(Component.literal("z"), usedValue.z,
                            0.0,
                            usedValue::setZ,
                            null
                        ).build(entryBuilder)
                    )
                }
            )
        )
    }

}
