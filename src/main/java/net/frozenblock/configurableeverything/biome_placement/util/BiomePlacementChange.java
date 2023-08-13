package net.frozenblock.configurableeverything.biome_placement.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;

public record BiomePlacementChange(List<DimensionBiomeList> addedBiomes, List<DimensionBiomeKeyList> removedBiomes) {

	public static final Codec<BiomePlacementChange> CODEC = RecordCodecBuilder.create(instance ->
		instance.group(
			DimensionBiomeList.CODEC.listOf().fieldOf("addedBiomes").forGetter(BiomePlacementChange::addedBiomes),
			DimensionBiomeKeyList.CODEC.listOf().fieldOf("removedBiomes").forGetter(BiomePlacementChange::removedBiomes)
		).apply(instance, BiomePlacementChange::new)
	);
}
