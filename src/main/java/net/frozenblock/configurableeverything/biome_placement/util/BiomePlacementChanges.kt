package net.frozenblock.configurableeverything.biome_placement.util

import net.minecraft.resources.ResourceLocation

object BiomePlacementChanges {

    private val MANAGER = BiomePlacementChangeManager.INSTANCE

    @JvmStatic
    val changes: List<BiomePlacementChange?>? get() = MANAGER.getChanges()

    @JvmStatic
    fun getChange(id: ResourceLocation?): BiomePlacementChange? {
        return MANAGER.getChange(id)
    }

    @JvmStatic
    fun addChange(
        key: ResourceLocation?,
        addedBiomes: List<DimensionBiomeList?>?,
        removedBiomes: List<DimensionBiomeKeyList?>?
    ) = MANAGER.addChange(key, addedBiomes, removedBiomes)

    @JvmStatic
    fun addChange(key: ResourceLocation?, change: BiomePlacementChange?)
        = MANAGER.addChange(key, change)
}
