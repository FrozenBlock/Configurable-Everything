package net.frozenblock.configurableeverything.biome_placement.util

import net.minecraft.core.RegistryAccess
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.dimension.DimensionType

fun interface ParameterListExtension {
    fun `configurableEverything$updateBiomesList`(registryAccess: RegistryAccess, dimension: ResourceKey<DimensionType>)
}
