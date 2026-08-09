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
        val removedStructures = EntryBuilder(StructureConfig.removedStructures,
            text("removed_structures"),
            tooltip("removed_structures"),
            StringList(StructureConfig.removedStructures.withSync.map { it.toString() }),
            StringList(StructureConfig.removedStructures.defaultValue().map { it.toString() }),
            { newValue -> StructureConfig.removedStructures.setValue((newValue as StringList).list.map { Identifier.parse(it) }.toMutableList()) },
            true,
            requirement = mainToggleReq,
        ).build(entryBuilder).apply {
            category.addEntry(this)
        }

        val removedStructureSets = EntryBuilder(StructureConfig.removedStructureSets,
            text("removed_structure_sets"),
            tooltip("removed_structure_sets"),
            StringList(StructureConfig.removedStructureSets.withSync.map { it.toString() }),
            StringList(StructureConfig.removedStructureSets.defaultValue().map { it.toString() }),
            { newValue -> StructureConfig.removedStructureSets.setValue((newValue as StringList).list.map { Identifier.parse(it) }.toMutableList()) },
            true,
            requirement = mainToggleReq,
        ).build(entryBuilder).apply {
            category.addEntry(this)
        }
    }
}
