package net.frozenblock.configurableeverything.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.DimensionType;
import java.util.List;

public record DimensionBiomeList(ResourceKey<DimensionType> dimension, List<BiomeParameters> biomes) {

	public static final Codec<DimensionBiomeList> CODEC = RecordCodecBuilder.create(instance ->
		instance.group(
			ResourceKey.codec(Registries.DIMENSION_TYPE).fieldOf("dimension").forGetter(DimensionBiomeList::dimension),
			BiomeParameters.CODEC.listOf().fieldOf("biomes").forGetter(DimensionBiomeList::biomes)
		).apply(instance, DimensionBiomeList::new)
	);
}
