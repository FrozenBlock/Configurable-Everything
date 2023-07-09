package net.frozenblock.configurableeverything.biome.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public record PlacedFeatureReplacement(ResourceKey<PlacedFeature> original, DecorationStepPlacedFeature replacement) {

	public static final Codec<PlacedFeatureReplacement> CODEC = RecordCodecBuilder.create(instance ->
		instance.group(
			ResourceKey.codec(Registries.PLACED_FEATURE).fieldOf("original").forGetter(PlacedFeatureReplacement::original),
			DecorationStepPlacedFeature.CODEC.fieldOf("replacement").forGetter(PlacedFeatureReplacement::replacement)
		).apply(instance, PlacedFeatureReplacement::new)
	);
}
