package net.frozenblock.configurableeverything.biome.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.frozenblock.configurableeverything.util.mutListOf

data class BiomeChange(
	@JvmField var addedFeatures: MutableList<BiomePlacedFeatureList>,
	@JvmField var removedFeatures: MutableList<BiomePlacedFeatureList>,
	@JvmField var replacedFeatures: MutableList<BiomePlacedFeatureReplacementList>,
	@JvmField var musicReplacements: MutableList<BiomeMusic>
) {
	companion object {
		@JvmField
		val CODEC: Codec<BiomeChange> = RecordCodecBuilder.create { instance ->
			instance.group(
				BiomePlacedFeatureList.CODEC.mutListOf().fieldOf("addedFeatures").forGetter(BiomeChange::addedFeatures),
				BiomePlacedFeatureList.CODEC.mutListOf().fieldOf("removedFeatures").forGetter(BiomeChange::removedFeatures),
				BiomePlacedFeatureReplacementList.CODEC.mutListOf().fieldOf("replacedFeatures").forGetter(BiomeChange::replacedFeatures),
				BiomeMusic.CODEC.mutListOf().fieldOf("musicReplacements").forGetter(BiomeChange::musicReplacements)
			).apply(instance, ::BiomeChange)
		}
	}
}
