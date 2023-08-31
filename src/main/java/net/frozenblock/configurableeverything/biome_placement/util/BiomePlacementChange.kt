package net.frozenblock.configurableeverything.biome_placement.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import java.util.List

data class BiomePlacementChange(
	@JvmField var addedBiomes: List<DimensionBiomeList>,
	@JvmField var removedBiomes: List<DimensionBiomeKeyList>) {

	public static final Codec<BiomePlacementChange> CODEC = RecordCodecBuilder.create(instance ->
		instance.group(
			DimensionBiomeList.CODEC.listOf().fieldOf("addedBiomes").forGetter(BiomePlacementChange::addedBiomes),
			DimensionBiomeKeyList.CODEC.listOf().fieldOf("removedBiomes").forGetter(BiomePlacementChange::removedBiomes)
		).apply(instance, BiomePlacementChange::new)
	);
}
