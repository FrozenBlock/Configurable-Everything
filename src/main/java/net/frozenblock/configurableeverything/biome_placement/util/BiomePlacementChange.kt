package net.frozenblock.configurableeverything.biome_placement.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class BiomePlacementChange(
	@JvmField var addedBiomes: List<DimensionBiomeList?>?,
	@JvmField var removedBiomes: List<DimensionBiomeKeyList?>?
) {
	companion object {
        @JvmField
		val CODEC: Codec<BiomePlacementChange> = RecordCodecBuilder.create { instance ->
			instance.group(
				DimensionBiomeList.CODEC.listOf().fieldOf("addedBiomes").forGetter(BiomePlacementChange::addedBiomes),
				DimensionBiomeKeyList.CODEC.listOf().fieldOf("removedBiomes").forGetter(BiomePlacementChange::removedBiomes)
			).apply(instance, ::BiomePlacementChange)
		}
	}

	override fun toString(): String = "BiomePlacementChange[addedBiomes=$addedBiomes, removedBiomes=$removedBiomes]"
}
