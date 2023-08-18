package net.frozenblock.configurableeverything.biome.util

import net.minecraft.resources.ResourceLocation

object BiomeChanges {

    private val MANAGER = BiomeChangeManager.INSTANCE
    val changes: List<BiomeChange?>? get() = MANAGER.getChanges()

    fun getChange(id: ResourceLocation?): BiomeChange? {
        return MANAGER.getChange(id)
    }

    fun addChange(
        key: ResourceLocation?,
        addedFeatures: List<BiomePlacedFeatureList?>?,
        removedFeatures: List<BiomePlacedFeatureList?>?,
        replacedFeatures: List<BiomePlacedFeatureReplacementList?>?,
        musicReplacements: List<BiomeMusic?>?
    ) {
        MANAGER.addChange(key, addedFeatures, removedFeatures, replacedFeatures, musicReplacements)
    }

    fun addChange(key: ResourceLocation?, change: BiomeChange?) {
        MANAGER.addChange(key, change)
    }
}
