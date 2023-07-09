package net.frozenblock.configurableeverything.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.DimensionType;
import java.util.List;

public record BiomeList(ResourceKey<DimensionType> dimension, List<BiomeParameters> biomes) {

	public static final Codec<BiomeList> CODEC = RecordCodecBuilder.create(instance ->
		instance.group(
			ResourceKey.codec(Registries.DIMENSION_TYPE).fieldOf("dimension").forGetter(BiomeList::dimension),
			BiomeParameters.CODEC.listOf().fieldOf("biomes").forGetter(BiomeList::biomes)
		).apply(instance, BiomeList::new)
	);
}
