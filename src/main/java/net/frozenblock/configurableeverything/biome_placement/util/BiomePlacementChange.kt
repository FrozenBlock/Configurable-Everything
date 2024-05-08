package net.frozenblock.configurableeverything.biome_placement.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.frozenblock.configurableeverything.util.mutListOf

data class BiomePlacementChange(
	@JvmField var addedBiomes: MutableList<DimensionBiomeList>,
	@JvmField var removedBiomes: MutableList<DimensionBiomeKeyList>
) {
	companion object {
        @JvmField
		val CODEC: Codec<BiomePlacementChange> = RecordCodecBuilder.create { instance ->
			instance.group(
				DimensionBiomeList.CODEC.mutListOf().fieldOf("addedBiomes").forGetter(BiomePlacementChange::addedBiomes),
				DimensionBiomeKeyList.CODEC.mutListOf().fieldOf("removedBiomes").forGetter(BiomePlacementChange::removedBiomes)
			).apply(instance, ::BiomePlacementChange)
		}
	}
}
