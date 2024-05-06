package net.frozenblock.configurableeverything.biome.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.placement.PlacedFeature

data class DecorationStepPlacedFeature(
	@JvmField var decoration: GenerationStep.Decoration,
	@JvmField var placedFeatures: MutableList<ResourceKey<PlacedFeature>>
) {
	companion object {
		@JvmField
		val CODEC: Codec<DecorationStepPlacedFeature> = RecordCodecBuilder.create { instance ->
			instance.group(
				GenerationStep.Decoration.CODEC.fieldOf("decoration").forGetter(DecorationStepPlacedFeature::decoration),
				ResourceKey.codec(Registries.PLACED_FEATURE).mutListOf().fieldOf("placed_features").forGetter(DecorationStepPlacedFeature::placedFeatures)
			).apply(instance, ::DecorationStepPlacedFeature)
		}
	}
}
