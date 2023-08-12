package net.frozenblock.configurableeverything.biome.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public record DecorationStepPlacedFeature(GenerationStep.Decoration decoration, List<ResourceKey<PlacedFeature>> placedFeatures) {

	public static final Codec<DecorationStepPlacedFeature> CODEC = RecordCodecBuilder.create(instance ->
		instance.group(
			GenerationStep.Decoration.CODEC.fieldOf("decoration").forGetter(DecorationStepPlacedFeature::decoration),
			ResourceKey.codec(Registries.PLACED_FEATURE).listOf().fieldOf("placed_features").forGetter(DecorationStepPlacedFeature::placedFeatures)
		).apply(instance, DecorationStepPlacedFeature::new)
	);
}
