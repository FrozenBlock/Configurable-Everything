@file:JvmName("StructureConfigUtil")

package net.frozenblock.configurableeverything.structure.util

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.config.StructureConfig
import net.frozenblock.configurableeverything.util.value
import net.minecraft.core.Holder
import net.minecraft.world.level.levelgen.structure.StructureSet
import net.minecraft.world.level.levelgen.structure.StructureSet.StructureSelectionEntry
import java.util.*

internal fun modifyStructureList(original: List<StructureSelectionEntry>): List<StructureSelectionEntry> = runBlocking {
    if (!MainConfig.structure.get()) return@runBlocking original
    val newList: MutableList<StructureSelectionEntry> = mutableListOf()
    newList.addAll(original)

    val removedStructures = StructureConfig.removedStructures.get()
    for (entry in original) { launch {
        val key = entry.structure.unwrapKey().orElseThrow().identifier()
        if (removedStructures.contains(key)) newList.remove(entry)
    } }
    return@runBlocking Collections.unmodifiableList(newList)
}

internal fun modifyStructureSetList(original: List<Holder<StructureSet>>): List<Holder<StructureSet>> = runBlocking {
    if (!MainConfig.structure.get()) return@runBlocking original
    val newList: MutableList<Holder<StructureSet>> = mutableListOf()
    newList.addAll(original)

    val removedStructureSets = StructureConfig.removedStructureSets.get()
    for (set in original) { launch {
        if (removedStructureSets.contains(set.unwrapKey().orElseThrow().identifier()))
            newList.remove(set)
    } }
    return@runBlocking Collections.unmodifiableList(newList)
}
