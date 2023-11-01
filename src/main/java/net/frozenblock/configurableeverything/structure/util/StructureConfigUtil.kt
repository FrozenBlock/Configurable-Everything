package net.frozenblock.configurableeverything.structure.util

import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.config.StructureConfig
import net.frozenblock.configurableeverything.util.ENABLE_EXPERIMENTAL_FEATURES
import net.minecraft.core.Holder
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.data.registries.VanillaRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.levelgen.structure.StructureSet
import net.minecraft.world.level.levelgen.structure.StructureSet.StructureSelectionEntry
import java.util.ArrayList
import java.util.Collections

object StructureConfigUtil {

    @JvmStatic
    fun modifyStructureList(original: List<StructureSelectionEntry>): List<StructureSelectionEntry> {
        if (!ENABLE_EXPERIMENTAL_FEATURES) return original
        val config = StructureConfig.get()
        if (MainConfig.get().structure == true) {
            val newList: MutableList<StructureSelectionEntry> = mutableListOf()
            newList.addAll(original)

            config.removedStructures?.value?.apply {
                for (entry in original) {
                    val key = entry.structure.unwrapKey().orElseThrow().location()
                    if (this.contains(key)) newList.remove(entry)
                }
            }
            return Collections.unmodifiableList(newList)
        }
        return original
    }

    @JvmStatic
    fun modifyStructureSetList(original: List<Holder<StructureSet>>): List<Holder<StructureSet>> {
        if (!ENABLE_EXPERIMENTAL_FEATURES) return original
        val config = StructureConfig.get()
        if (MainConfig.get().structure == true) {
            val newList: MutableList<Holder<StructureSet>> = mutableListOf()
            newList.addAll(original)

            config.removedStructureSets?.value?.apply {
                for (set in original) {
                    if (this.contains(set.unwrapKey().orElseThrow().location())) newList.remove(set)
                }
            }
            return Collections.unmodifiableList(newList)
        }
        return original
    }
}
