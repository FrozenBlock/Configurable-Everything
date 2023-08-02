package net.frozenblock.configurableeverything.biome_placement.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import java.util.List;

public record DimensionBiomeKeyList(ResourceKey<DimensionType> dimension, List<Either<ResourceKey<Biome>, TagKey<Biome>>> biomes) {

	public static final Codec<DimensionBiomeKeyList> CODEC = RecordCodecBuilder.create(instance ->
		instance.group(
			ResourceKey.codec(Registries.DIMENSION_TYPE).fieldOf("dimension").forGetter(DimensionBiomeKeyList::dimension),
			Codec.either(ResourceKey.codec(Registries.BIOME), TagKey.hashedCodec(Registries.BIOME)).listOf().fieldOf("biomes").forGetter(DimensionBiomeKeyList::biomes)
		).apply(instance, DimensionBiomeKeyList::new)
	);
}
