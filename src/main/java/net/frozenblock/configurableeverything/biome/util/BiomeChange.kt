package net.frozenblock.configurableeverything.biome.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class BiomeChange(
	@JvmField var addedFeatures: List<BiomePlacedFeatureList?>?,
	@JvmField var removedFeatures: List<BiomePlacedFeatureList?>?,
	@JvmField var replacedFeatures: List<BiomePlacedFeatureReplacementList?>?,
	@JvmField var musicReplacements: List<BiomeMusic?>?
) {
	companion object {
		@JvmField
		val CODEC: Codec<BiomeChange> = RecordCodecBuilder.create { instance ->
			instance.group(
				BiomePlacedFeatureList.CODEC.listOf().fieldOf("addedFeatures").forGetter(BiomeChange::addedFeatures),
				BiomePlacedFeatureList.CODEC.listOf().fieldOf("removedFeatures").forGetter(BiomeChange::removedFeatures),
				BiomePlacedFeatureReplacementList.CODEC.listOf().fieldOf("replacedFeatures").forGetter(BiomeChange::replacedFeatures),
				BiomeMusic.CODEC.listOf().fieldOf("musicReplacements").forGetter(BiomeChange::musicReplacements)
			).apply(instance, ::BiomeChange)
		}
	}

    override fun toString(): String = "BiomeChange[addedFeatures=$addedFeatures, removedFeatures=$removedFeatures, replacedFeatures=$replacedFeatures, musicReplacements=$musicReplacements]"
}
