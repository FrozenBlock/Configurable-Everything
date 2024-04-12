package net.frozenblock.configurableeverything.biome.util

import net.frozenblock.configurableeverything.biome.util.BiomeConfigUtil.applyModifications
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.DataReloadManager
import net.minecraft.resources.ResourceLocation

@Suppress("NOTHING_TO_INLINE")
object BiomeChanges : DataReloadManager<BiomeChange>(
    "biome_change_reloader",
    "Biome Change Manager",
    "biome_modifications",
    "Load Biome Changes",
    "biome change",
    BiomeChange.CODEC
) {
    override val shouldApply: Boolean get() = MainConfig.get().datapack.biome

    override fun apply(values: Collection<BiomeChange>) {
        applyModifications(values)
    }

    /**
     * Adds a biome change with the specified [ResourceLocation]
     */
    internal inline fun add(
        key: ResourceLocation,
        addedFeatures: List<BiomePlacedFeatureList>,
        removedFeatures: List<BiomePlacedFeatureList>,
        replacedFeatures: List<BiomePlacedFeatureReplacementList>,
        musicReplacements: List<BiomeMusic>
    ) = add(key, BiomeChange(addedFeatures, removedFeatures, replacedFeatures, musicReplacements))
}
