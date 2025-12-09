package net.frozenblock.configurableeverything.biome.util

import net.frozenblock.configurableeverything.biome.util.BiomeConfigUtil.applyModifications
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.DataReloadManager
import net.minecraft.resources.Identifier

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
     * Adds a biome change with the specified [Identifier]
     */
    internal inline fun add(
        key: Identifier,
        addedFeatures: MutableList<BiomePlacedFeatureList>,
        removedFeatures: MutableList<BiomePlacedFeatureList>,
        replacedFeatures: MutableList<BiomePlacedFeatureReplacementList>,
        musicReplacements: MutableList<BiomeMusic>
    ) = add(key, BiomeChange(addedFeatures, removedFeatures, replacedFeatures, musicReplacements))
}
