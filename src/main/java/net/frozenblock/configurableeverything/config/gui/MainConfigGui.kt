package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.configurableeverything.util.tooltip
import net.frozenblock.lib.config.clothconfig.FrozenClothConfig

@Environment(EnvType.CLIENT)
object MainConfigGui {
    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        val config = MainConfig.get()
        category.background = id("textures/config/main.png")

        category.addEntry(entryBuilder.startBooleanToggle(text("biome"), config.biome == true)
            .setDefaultValue(false)
            .setSaveConsumer { newValue: Boolean? -> config.biome = newValue }
            .setTooltip(tooltip("biome"))
            .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
            .build()
        )

        category.addEntry(entryBuilder.startBooleanToggle(text("biome_placement"), config.biome_placement == true)
            .setDefaultValue(false)
            .setSaveConsumer { newValue: Boolean? -> config.biome_placement = newValue }
            .setTooltip(tooltip("biome_placement"))
            .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
            .build()
        )

        category.addEntry(entryBuilder.startBooleanToggle(text("datafixer"), config.datafixer == true)
            .setDefaultValue(false)
            .setSaveConsumer { newValue: Boolean? -> config.datafixer = newValue }
            .setTooltip(tooltip("datafixer"))
            .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
            .build()
        )

        category.addEntry(entryBuilder.startBooleanToggle(text("entity"), config.entity == true)
            .setDefaultValue(false)
            .setSaveConsumer { newValue: Boolean? -> config.entity = newValue }
            .setTooltip(tooltip("entity"))
            .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
            .build()
        )

        category.addEntry(entryBuilder.startBooleanToggle(text("fluid"), config.fluid == true)
            .setDefaultValue(false)
            .setSaveConsumer { newValue: Boolean? -> config.fluid = newValue }
            .setTooltip(tooltip("fluid"))
            .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
            .build()
        )

        category.addEntry(entryBuilder.startBooleanToggle(text("game"), config.game == true)
            .setDefaultValue(false)
            .setSaveConsumer { newValue: Boolean? -> config.game = newValue }
            .setTooltip(tooltip("game"))
            .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
            .build()
        )

        category.addEntry(entryBuilder.startBooleanToggle(text("screen_shake"), config.screen_shake == true)
            .setDefaultValue(false)
            .setSaveConsumer { newValue: Boolean? -> config.screen_shake = newValue }
            .setTooltip(tooltip("screen_shake"))
            .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
            .build()
        )

        category.addEntry(entryBuilder.startBooleanToggle(text("splash_text"), config.splash_text == true)
            .setDefaultValue(false)
            .setSaveConsumer { newValue: Boolean? -> config.splash_text = newValue }
            .setTooltip(tooltip("splash_text"))
            .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
            .build()
        )

        category.addEntry(entryBuilder.startBooleanToggle(text("surface_rule"), config.surface_rule == true)
            .setDefaultValue(false)
            .setSaveConsumer { newValue: Boolean? -> config.surface_rule = newValue }
            .setTooltip(tooltip("surface_rule"))
            .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
            .build()
        )

        category.addEntry(entryBuilder.startBooleanToggle(text("world"), config.world == true)
            .setDefaultValue(false)
            .setSaveConsumer { newValue: Boolean? -> config.world = newValue }
            .setTooltip(tooltip("world"))
            .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
            .build()
        )

        val applyDatapacksFolder = entryBuilder.startBooleanToggle(text("apply_datapacks_folder"), config.datapack?.applyDatapacksFolder == true)
            .setDefaultValue(true)
            .setSaveConsumer { newValue: Boolean? -> config.datapack?.applyDatapacksFolder = newValue }
            .setTooltip(tooltip("apply_datapacks_folder"))
            .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
            .build()

        val datapackBiome = entryBuilder.startBooleanToggle(text("datapack_biome"), config.datapack?.biome == true)
                .setDefaultValue(true)
                .setSaveConsumer { newValue: Boolean? -> config.datapack?.biome = newValue }
                .setTooltip(tooltip("datapack_biome"))
                .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
                .build()

        val datapackBiomePlacement = entryBuilder.startBooleanToggle(text("datapack_biome_placement"), config.datapack?.biome_placement == true)
            .setDefaultValue(true)
            .setSaveConsumer { newValue: Boolean? -> config.datapack?.biome_placement = newValue }
            .setTooltip(tooltip("datapack_biome_placement"))
            .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
            .build()

        val json5Support = entryBuilder.startBooleanToggle(text("json5_support"), config.datapack?.json5Support == true)
            .setDefaultValue(true)
            .setSaveConsumer { newValue: Boolean? -> config.datapack?.json5Support = newValue }
            .setTooltip(tooltip("json5_support"))
            .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
            .build()

        FrozenClothConfig.createSubCategory(
            entryBuilder, category, text("datapack"), false, tooltip("datapack"),
            applyDatapacksFolder, datapackBiome, datapackBiomePlacement, json5Support
        )
    }
}
