@file:Environment(EnvType.CLIENT)

package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.clothconfig2.api.Requirement
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.StructureConfig
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.configurableeverything.util.tooltip
import net.frozenblock.configurableeverything.util.value
import net.frozenblock.lib.config.api.client.gui.EntryBuilder
import net.frozenblock.lib.config.api.client.gui.StringList
import net.minecraft.resources.Identifier

object StructureConfigGui {

    private inline val mainToggleReq: Requirement
        get() = Requirement.isTrue(MainConfigGui.INSTANCE!!.structure)

    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        val config = StructureConfig.get(real = true)
        val defaultConfig = StructureConfig.defaultInstance()

        val removedStructures = EntryBuilder(text("removed_structures"), StringList(config.removedStructures.value().map { it.toString() }),
            StringList(defaultConfig.removedStructures.value().map { it.toString() }),
            { newValue -> config.removedStructures.value = newValue.list.map { Identifier.parse(it) }.toMutableList() },
            tooltip("removed_structures"),
            true,
            requirement = mainToggleReq,
        ).build(entryBuilder).apply {
            category.addEntry(this)
        }

        val removedStructureSets = EntryBuilder(text("removed_structure_sets"), StringList(config.removedStructureSets.value().map { it.toString() }),
            StringList(defaultConfig.removedStructureSets.value().map { it.toString() }),
            { newValue -> config.removedStructureSets.value = newValue.list.map { Identifier.parse(it) }.toMutableList() },
            tooltip("removed_structure_sets"),
            true,
            requirement = mainToggleReq,
        ).build(entryBuilder).apply {
            category.addEntry(this)
        }
    }
}
