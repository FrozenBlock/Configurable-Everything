package net.frozenblock.configurableeverything.biome.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.placement.PlacedFeature

@Serializable
data class DecorationStepPlacedFeature(
	@JvmField var decoration: GenerationStep.Decoration?,
	@JvmField var placedFeatures: List<@Contextual ResourceKey<@Contextual PlacedFeature>?>?
) {
	companion object {
		@JvmField
		val CODEC: Codec<DecorationStepPlacedFeature> = RecordCodecBuilder.create { instance ->
			instance.group(
				GenerationStep.Decoration.CODEC.fieldOf("decoration").forGetter(DecorationStepPlacedFeature::decoration),
				ResourceKey.codec(Registries.PLACED_FEATURE).listOf().fieldOf("placed_features").forGetter(DecorationStepPlacedFeature::placedFeatures)
			).apply(instance, ::DecorationStepPlacedFeature)
		}
	}

    override fun toString(): String = "DecorationStepPlacedFeature[decoration=$decoration, placed_features=$placedFeatures]"
}
