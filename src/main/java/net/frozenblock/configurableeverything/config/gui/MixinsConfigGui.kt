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
            tooltip("biome_placement")
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("datafixer"), config.datafixer == true,
            defaultConfig.datafixer!!,
            { newValue -> config.datafixer = newValue },
            tooltip("datafixer")
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("datapack"), config.datapack == true,
            defaultConfig.datapack!!,
            { newValue -> config.datapack = newValue },
            tooltip("datapack")
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("entity"), config.entity == true,
            defaultConfig.entity!!,
            { newValue -> config.entity = newValue },
            tooltip("entity")
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("entity_zombie"), config.entity_zombie == true,
            defaultConfig.entity_zombie!!,
            { newValue -> config.entity_zombie = newValue },
            tooltip("entity_zombie")
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("fluid"), config.fluid == true,
            defaultConfig.fluid!!,
            { newValue -> config.fluid = newValue },
            tooltip("fluid")
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("game"), config.game == true,
            defaultConfig.game!!,
            { newValue -> config.game = newValue },
            tooltip("game")
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("game_client"), config.game_client == true,
            defaultConfig.game_client!!,
            { newValue -> config.game_client = newValue },
            tooltip("game_client")
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("screenshake"), config.screenshake == true,
            defaultConfig.screenshake!!,
            { newValue -> config.screenshake = newValue },
            tooltip("screenshake")
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("screenshake_client"), config.screenshake_client == true,
            defaultConfig.screenshake_client!!,
            { newValue -> config.screenshake_client = newValue },
            tooltip("screenshake_client")
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("splash_text"), config.splash_text == true,
            defaultConfig.splash_text!!,
            { newValue -> config.splash_text = newValue },
            tooltip("splash_text")
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("world"), config.world == true,
            defaultConfig.world!!,
            { newValue -> config.world = newValue },
            tooltip("world")
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("world_client"), config.world_client == true,
            defaultConfig.world_client!!,
            { newValue -> config.world_client = newValue },
            tooltip("world_client")
        ).build(entryBuilder))
    }
}
