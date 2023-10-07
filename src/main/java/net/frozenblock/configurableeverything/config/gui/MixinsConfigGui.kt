package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.MixinsConfig
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.configurableeverything.util.tooltip
import net.frozenblock.lib.config.api.client.gui.EntryBuilder

@Environment(EnvType.CLIENT)
object MixinsConfigGui {
    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        val config = MixinsConfig.get()
        val defaultConfig = MixinsConfig.INSTANCE.defaultInstance()
        category.background = id("textures/config/mixins.png")

        category.addEntry(EntryBuilder(text("biome_placement"), config.biome_placement == true,
            defaultConfig.biome_placement!!,
            { newValue -> config.biome_placement = newValue },
            requiresRestart = true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("datafixer"), config.datafixer == true,
            defaultConfig.datafixer!!,
            { newValue -> config.datafixer = newValue },
            requiresRestart = true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("datapack"), config.datapack == true,
            defaultConfig.datapack!!,
            { newValue -> config.datapack = newValue },
            requiresRestart = true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("entity"), config.entity == true,
            defaultConfig.entity!!,
            { newValue -> config.entity = newValue },
            requiresRestart = true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("entity_zombie"), config.entity_zombie == true,
            defaultConfig.entity_zombie!!,
            { newValue -> config.entity_zombie = newValue },
            requiresRestart = true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("fluid"), config.fluid == true,
            defaultConfig.fluid!!,
            { newValue -> config.fluid = newValue },
            requiresRestart = true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("game"), config.game == true,
            defaultConfig.game!!,
            { newValue -> config.game = newValue },
            requiresRestart = true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("game_client"), config.game_client == true,
            defaultConfig.game_client!!,
            { newValue -> config.game_client = newValue },
            requiresRestart = true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("screenshake"), config.screenshake == true,
            defaultConfig.screenshake!!,
            { newValue -> config.screenshake = newValue },
            requiresRestart = true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("screenshake_client"), config.screenshake_client == true,
            defaultConfig.screenshake_client!!,
            { newValue -> config.screenshake_client = newValue },
            requiresRestart = true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("splash_text"), config.splash_text == true,
            defaultConfig.splash_text!!,
            { newValue -> config.splash_text = newValue },
            requiresRestart = true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("world"), config.world == true,
            defaultConfig.world!!,
            { newValue -> config.world = newValue },
            requiresRestart = true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("world_client"), config.world_client == true,
            defaultConfig.world_client!!,
            { newValue -> config.world_client = newValue },
            requiresRestart = true
        ).build(entryBuilder))
    }
}
