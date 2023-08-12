package net.frozenblock.configurableeverything.biome_placement.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.DimensionType;

public record DimensionBiomeList(ResourceKey<DimensionType> dimension, List<BiomeParameters> biomes) {

	public static final Codec<DimensionBiomeList> CODEC = RecordCodecBuilder.create(instance ->
		instance.group(
			ResourceKey.codec(Registries.DIMENSION_TYPE).fieldOf("dimension").forGetter(DimensionBiomeList::dimension),
			BiomeParameters.CODEC.listOf().fieldOf("biomes").forGetter(DimensionBiomeList::biomes)
		).apply(instance, DimensionBiomeList::new)
	);
}
