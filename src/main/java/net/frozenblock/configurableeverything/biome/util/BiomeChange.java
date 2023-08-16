package net.frozenblock.configurableeverything.biome.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;

public record BiomeChange(List<BiomePlacedFeatureList> addedFeatures, List<BiomePlacedFeatureList> removedFeatures, List<BiomePlacedFeatureReplacementList> replacedFeatures, List<BiomeMusic> musicReplacements) {

	public static final Codec<BiomeChange> CODEC = RecordCodecBuilder.create(instance ->
		instance.group(
			BiomePlacedFeatureList.CODEC.listOf().fieldOf("addedFeatures").forGetter(BiomeChange::addedFeatures),
			BiomePlacedFeatureList.CODEC.listOf().fieldOf("removedFeatures").forGetter(BiomeChange::removedFeatures),
			BiomePlacedFeatureReplacementList.CODEC.listOf().fieldOf("replacedFeatures").forGetter(BiomeChange::replacedFeatures),
			BiomeMusic.CODEC.listOf().fieldOf("musicReplacements").forGetter(BiomeChange::musicReplacements)
		).apply(instance, BiomeChange::new)
	);
}
