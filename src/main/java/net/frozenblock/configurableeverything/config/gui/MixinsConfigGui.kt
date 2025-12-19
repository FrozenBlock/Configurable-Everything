@file:Environment(EnvType.CLIENT)

package net.frozenblock.configurableeverything.config.gui

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment

// TODO: Re-enable when cloth config is unobfuscated
/*import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.MixinsConfig
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.lib.config.api.client.gui.EntryBuilder

@Environment(EnvType.CLIENT)
object MixinsConfigGui {
    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        val config = MixinsConfig.get()
        val defaultConfig = MixinsConfig.defaultInstance()

        category.addEntry(EntryBuilder(text("biome_placement"), config.biome_placement,
            defaultConfig.biome_placement,
            { newValue -> config.biome_placement = newValue },
            requiresRestart = true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("datafixer"), config.datafixer,
            defaultConfig.datafixer,
            { newValue -> config.datafixer = newValue },
            requiresRestart = true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("datapack"), config.datapack,
            defaultConfig.datapack,
            { newValue -> config.datapack = newValue },
            requiresRestart = true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("entity"), config.entity,
            defaultConfig.entity,
            { newValue -> config.entity = newValue },
            requiresRestart = true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("entity_zombie"), config.entity_zombie,
            defaultConfig.entity_zombie,
            { newValue -> config.entity_zombie = newValue },
            requiresRestart = true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("fluid"), config.fluid,
            defaultConfig.fluid,
            { newValue -> config.fluid = newValue },
            requiresRestart = true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("game"), config.game,
            defaultConfig.game,
            { newValue -> config.game = newValue },
            requiresRestart = true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("game_client"), config.game_client,
            defaultConfig.game_client,
            { newValue -> config.game_client = newValue },
            requiresRestart = true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("item"), config.item,
            defaultConfig.item,
            { newValue -> config.item = newValue},
            requiresRestart = true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("loot"), config.loot,
            defaultConfig.loot,
            { newValue -> config.loot = newValue},
            requiresRestart = true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("screenshake"), config.screenshake,
            defaultConfig.screenshake,
            { newValue -> config.screenshake = newValue },
            requiresRestart = true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("screenshake_client"), config.screenshake_client,
            defaultConfig.screenshake_client,
            { newValue -> config.screenshake_client = newValue },
            requiresRestart = true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("sculk_spreading"), config.sculk_spreading,
            defaultConfig.sculk_spreading,
            { newValue -> config.sculk_spreading = newValue},
            requiresRestart = true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("splash_text"), config.splash_text,
            defaultConfig.splash_text,
            { newValue -> config.splash_text = newValue },
            requiresRestart = true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("structure"), config.structure,
            defaultConfig.structure,
            { newValue -> config.structure = newValue},
            requiresRestart = true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("tag"), config.tag,
            defaultConfig.tag,
            { newValue -> config.tag = newValue },
            requiresRestart = true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("world"), config.world,
            defaultConfig.world,
            { newValue -> config.world = newValue },
            requiresRestart = true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("world_client"), config.world_client,
            defaultConfig.world_client,
            { newValue -> config.world_client = newValue },
            requiresRestart = true
        ).build(entryBuilder))
    }
}
*/
