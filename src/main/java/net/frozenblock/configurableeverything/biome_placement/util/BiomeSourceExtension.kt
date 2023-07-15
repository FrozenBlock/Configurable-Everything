package net.frozenblock.configurableeverything.biome_placement.util

import net.minecraft.core.Holder
import net.minecraft.world.level.biome.Biome

fun interface BiomeSourceExtension {
    fun updateBiomesList(biomesToAdd: List<Holder<Biome>>, biomesToRemove: List<Holder<Biome>>)
}
