package net.frozenblock.configurableeverything.biome.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.frozenblock.configurableeverything.util.mutListOf
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
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

        @JvmField
        val DECORATION_STREAM_CODEC: StreamCodec<ByteBuf, GenerationStep.Decoration> = ByteBufCodecs.fromCodec(GenerationStep.Decoration.CODEC)

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, DecorationStepPlacedFeature> = StreamCodec.composite(
            DECORATION_STREAM_CODEC, DecorationStepPlacedFeature::decoration,
            ByteBufCodecs.list<ByteBuf, ResourceKey<PlacedFeature>>()
                .apply(ResourceKey.streamCodec(Registries.PLACED_FEATURE)), DecorationStepPlacedFeature::placedFeatures,
            ::DecorationStepPlacedFeature
        )
	}
}
