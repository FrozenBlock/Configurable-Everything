package net.frozenblock.configurableeverything.biome.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import java.util.List;

public record BiomePlacedFeatureList(Either<TagKey<Biome>, ResourceKey<Biome>> biome, List<DecorationStepPlacedFeature> features) {

	public static final Codec<BiomePlacedFeatureList> CODEC = RecordCodecBuilder.create(instance ->
		instance.group(
			Codec.either(TagKey.codec(Registries.BIOME), ResourceKey.codec(Registries.BIOME)).fieldOf("biome").forGetter(BiomePlacedFeatureList::biome),
			DecorationStepPlacedFeature.CODEC.listOf().fieldOf("placed_features").forGetter(BiomePlacedFeatureList::features)
		).apply(instance, BiomePlacedFeatureList::new)
	);
}
