package net.frozenblock.configurableeverything.biome.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.placement.PlacedFeature

data class PlacedFeatureReplacement(
	@JvmField var original: ResourceKey<PlacedFeature>,
	@JvmField var replacement: DecorationStepPlacedFeature
) {
	companion object {
		@JvmField
		val CODEC: Codec<PlacedFeatureReplacement> = RecordCodecBuilder.create { instance ->
			instance.group(
				ResourceKey.codec(Registries.PLACED_FEATURE).fieldOf("original").forGetter(PlacedFeatureReplacement::original),
				DecorationStepPlacedFeature.CODEC.fieldOf("replacement").forGetter(PlacedFeatureReplacement::replacement)
			).apply(instance, ::PlacedFeatureReplacement)
		}

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, PlacedFeatureReplacement> = StreamCodec.composite(
            ResourceKey.streamCodec(Registries.PLACED_FEATURE), PlacedFeatureReplacement::original,
            DecorationStepPlacedFeature.STREAM_CODEC, PlacedFeatureReplacement::replacement,
            ::PlacedFeatureReplacement
        )
	}
}
