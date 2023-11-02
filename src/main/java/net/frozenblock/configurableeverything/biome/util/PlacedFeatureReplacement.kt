package net.frozenblock.configurableeverything.biome.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.placement.PlacedFeature

@Serializable
data class PlacedFeatureReplacement(
	@JvmField var original: @Contextual ResourceKey<@Contextual PlacedFeature>?,
	@JvmField var replacement: DecorationStepPlacedFeature?
) {
	companion object {
		@JvmField
		val CODEC: Codec<PlacedFeatureReplacement> = RecordCodecBuilder.create { instance ->
			instance.group(
				ResourceKey.codec(Registries.PLACED_FEATURE).fieldOf("original").forGetter(PlacedFeatureReplacement::original),
				DecorationStepPlacedFeature.CODEC.fieldOf("replacement").forGetter(PlacedFeatureReplacement::replacement)
			).apply(instance, ::PlacedFeatureReplacement)
		}
	}

    override fun toString(): String = "PlacedFeatureReplacement[original=$original, replacement=$replacement]"
}
