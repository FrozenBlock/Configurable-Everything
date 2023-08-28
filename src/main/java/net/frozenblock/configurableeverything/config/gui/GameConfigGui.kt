package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.*
import me.shedaniel.clothconfig2.gui.entries.*
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.GameConfig
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.configurableeverything.util.tooltip
import net.frozenblock.lib.config.clothconfig.FrozenClothConfig
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.Vec3

@Environment(EnvType.CLIENT)
object GameConfigGui {
    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        val config = GameConfig.get()
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

        category.addEntry(NestedListListEntry(
            Component.literal("Test"),
            config.testing.value,
            false,
            Optional::empty,
            { newValue -> config.testing.value = newValue},
            { listOf(Vec3(1, 1, 1), Vec3(2, 2, 2), Vec3(69, 420, 5))},
            entryBuilder.getResetButtonKey(),
            true,
            true,
            { (elem, nestedListListEntry) ->
                if (elem == null) {
                    val newDefault = Vec3(1.0, 1.0, 1.0)
                    return MultiElementListEntry(Component.literal("Vec3"), newDefault,
                        listOf(
                            entryBuilder.startDoubleField(Component.literal("x"), newDefault.x).setDefaultValue(1.0).build(),
                            entryBuilder.startDoubleField(Component.literal("y"), newDefault.y).setDefaultValue(1.0).build()
                            entryBuilder.startDoubleField(Component.literal("z"), newDefault.z).setDefaultValue(1.0).build()
                        ),
                        true
                    )
                } else {
                    return MultiElementListEntry(Component.literal("Vec3"), elem,
                        listOf(
                            entryBuilder.startDoubleField(Component.literal("x"), elem.x).setDefaultValue(1.0).build(),
                            entryBuilder.startDoubleField(Component.literal("y"), elem.y).setDefaultValue(1.0).build(),
                            entryBuilder.startDoubleField(Component.literal("z"), elem.z).setDefaultValue(1.0).build()
                        ),
                        true
                    )
                }
            }
        ))
    }
}
