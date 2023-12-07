package net.frozenblock.configurableeverything.biome.util

import net.minecraft.resources.ResourceLocation

object BiomeChanges {

    @PublishedApi
    internal val MANAGER = BiomeChangeManager

    @JvmStatic
    inline val changes: List<BiomeChange?>?
        get() = MANAGER.getChanges()

    @JvmStatic
    fun getChange(id: ResourceLocation?): BiomeChange? = MANAGER.getChange(id)

    @JvmStatic
    fun addChange(
        key: ResourceLocation?,
        addedFeatures: List<BiomePlacedFeatureList?>?,
        removedFeatures: List<BiomePlacedFeatureList?>?,
        replacedFeatures: List<BiomePlacedFeatureReplacementList?>?,
        musicReplacements: List<BiomeMusic?>?
    ) {
        MANAGER.addChange(key, addedFeatures, removedFeatures, replacedFeatures, musicReplacements)
    }

    @JvmStatic
    fun addChange(key: ResourceLocation?, change: BiomeChange?) {
        MANAGER.addChange(key, change)
    }
}
