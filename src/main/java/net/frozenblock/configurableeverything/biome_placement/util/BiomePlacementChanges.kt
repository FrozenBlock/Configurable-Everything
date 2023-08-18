package net.frozenblock.configurableeverything.biome_placement.util

import net.minecraft.resources.ResourceLocation

object BiomePlacementChanges {

    private val MANAGER = BiomePlacementChangeManager.INSTANCE

    val changes: List<BiomePlacementChange?>? get() = MANAGER.getChanges()

        fun getChange(id: ResourceLocation?): BiomePlacementChange? {
            return MANAGER.getChange(id)
        }

        fun addChange(
            key: ResourceLocation?,
            addedBiomes: List<DimensionBiomeList?>?,
            removedBiomes: List<DimensionBiomeKeyList?>?
        ) {
            MANAGER.addChange(key, addedBiomes, removedBiomes)
        }

        fun addChange(key: ResourceLocation?, change: BiomePlacementChange?) {
            MANAGER.addChange(key, change)
        }
}
