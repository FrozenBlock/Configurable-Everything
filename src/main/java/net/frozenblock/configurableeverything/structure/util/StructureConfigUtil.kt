@file:JvmName("StructureConfigUtil")

package net.frozenblock.configurableeverything.structure.util

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.launch
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.config.StructureConfig
import net.frozenblock.configurableeverything.util.ENABLE_EXPERIMENTAL_FEATURES
import net.minecraft.core.Holder
import net.minecraft.world.level.levelgen.structure.StructureSet
import net.minecraft.world.level.levelgen.structure.StructureSet.StructureSelectionEntry
import java.util.*

internal fun modifyStructureList(original: List<StructureSelectionEntry>): List<StructureSelectionEntry> = runBlocking {
    if (!ENABLE_EXPERIMENTAL_FEATURES) return@runBlocking original
    val config = StructureConfig.get()
    if (MainConfig.get().structure != true) return@runBlocking original
    val newList: MutableList<StructureSelectionEntry> = mutableListOf()
    newList.addAll(original)

    config.removedStructures?.value?.apply {
        for (entry in original) {
            launch {
                val key = entry.structure.unwrapKey().orElseThrow().location()
                if (this@apply.contains(key)) newList.remove(entry)
            }
        }
    }
    return@runBlocking Collections.unmodifiableList(newList)
}

internal fun modifyStructureSetList(original: List<Holder<StructureSet>>): List<Holder<StructureSet>> = runBlocking {
    if (!ENABLE_EXPERIMENTAL_FEATURES) return@runBlocking original
    val config = StructureConfig.get()
    if (MainConfig.get().structure != true) return@runBlocking original
    val newList: MutableList<Holder<StructureSet>> = mutableListOf()
    newList.addAll(original)

    config.removedStructureSets?.value?.apply {
        for (set in original) {
            launch {
                if (this@apply.contains(set.unwrapKey().orElseThrow().location()))
                    newList.remove(set)
            }
        }
    }
    return@runBlocking Collections.unmodifiableList(newList)
}
