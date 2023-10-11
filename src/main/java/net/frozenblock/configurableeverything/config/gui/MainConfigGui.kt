@file:Environment(EnvType.CLIENT)

package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.configurableeverything.util.tooltip
import net.frozenblock.lib.config.api.client.gui.EntryBuilder
import net.frozenblock.lib.config.clothconfig.FrozenClothConfig.createSubCategory

@Environment(EnvType.CLIENT)
object MainConfigGui {
    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        val config = MainConfig.get()
        val defaultConfig = MainConfig.INSTANCE.defaultInstance()
        category.background = id("textures/config/main.png")

        category.addEntry(EntryBuilder(text("biome"), config.biome,
            defaultConfig.biome!!,
            { newValue -> config.biome = newValue },
            tooltip("biome"),
            true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("biome_placement"), config.biome_placement,
            defaultConfig.biome_placement!!,
            { newValue -> config.biome_placement = newValue },
            tooltip("biome_placement"),
            true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("datafixer"), config.datafixer,
            defaultConfig.datafixer!!,
            { newValue -> config.datafixer = newValue },
            tooltip("datafixer"),
            true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("entity"), config.entity,
            defaultConfig.entity!!,
            { newValue -> config.entity = newValue },
            tooltip("entity"),
            true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("fluid"), config.fluid,
            defaultConfig.fluid!!,
            { newValue -> config.fluid = newValue },
            tooltip("fluid"),
            true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("game"), config.game,
            defaultConfig.game!!,
            { newValue -> config.game = newValue },
            tooltip("game"),
            true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("screen_shake"), config.screen_shake,
            defaultConfig.screen_shake!!,
            { newValue -> config.screen_shake = newValue },
            tooltip("screen_shake"),
            true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("splash_text"), config.splash_text,
            defaultConfig.splash_text!!,
            { newValue -> config.splash_text = newValue },
            tooltip("splash_text"),
            true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("surface_rule"), config.surface_rule,
            defaultConfig.surface_rule!!,
            { newValue -> config.surface_rule = newValue },
            tooltip("surface_rule"),
            true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("world"), config.world,
            defaultConfig.world!!,
            { newValue -> config.world = newValue },
            tooltip("world"),
            true
        ).build(entryBuilder))

        val applyDatapacksFolder = EntryBuilder(text("apply_datapacks_folder"), config.datapack?.applyDatapacksFolder,
            defaultConfig.datapack!!.applyDatapacksFolder!!,
            { newValue -> config.datapack?.applyDatapacksFolder = newValue },
            tooltip("apply_datapacks_folder"),
            true
        ).build(entryBuilder)

        val datapackBiome = EntryBuilder(text("datapack_biome"), config.datapack?.biome,
            defaultConfig.datapack!!.biome!!,
            { newValue -> config.datapack?.biome = newValue },
            tooltip("datapack_biome"),
            true
        ).build(entryBuilder)

        val datapackBiomePlacement = EntryBuilder(text("datapack_biome_placement"), config.datapack?.biome_placement,
            defaultConfig.datapack!!.biome_placement!!,
            { newValue -> config.datapack?.biome_placement = newValue },
            tooltip("datapack_biome_placement"),
            true
        ).build(entryBuilder)

        val json5Support = EntryBuilder(text("json5_support"), config.datapack?.json5Support,
            defaultConfig.datapack!!.json5Support!!,
            { newValue -> config.datapack?.json5Support = newValue },
            tooltip("json5_support"),
            true
        ).build(entryBuilder)

        createSubCategory(
            entryBuilder, category, text("datapack"), false, tooltip("datapack"),
            applyDatapacksFolder, datapackBiome, datapackBiomePlacement, json5Support
        )
    }
}
