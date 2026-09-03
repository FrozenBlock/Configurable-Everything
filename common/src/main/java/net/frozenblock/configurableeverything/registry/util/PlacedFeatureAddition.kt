package net.frozenblock.configurableeverything.registry.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier
import net.minecraft.world.level.levelgen.placement.PlacedFeature

data class PlacedFeatureAddition(
    override var key: Identifier,
    override var value: PlacedFeature
) : DynamicRegistryAddition<PlacedFeature>(Registries.PLACED_FEATURE, key, value) {
    companion object {
        @JvmField
        val CODEC: Codec<PlacedFeatureAddition> = RecordCodecBuilder.create { instance ->
            instance.group(
                Identifier.CODEC.fieldOf("key").forGetter(PlacedFeatureAddition::key),
                PlacedFeature.DIRECT_CODEC.fieldOf("value").forGetter(PlacedFeatureAddition::value)
            ).apply(instance, ::PlacedFeatureAddition)
        }

        // todo more compact stream codec
        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, PlacedFeatureAddition> = StreamCodec.composite(
            Identifier.STREAM_CODEC,
            PlacedFeatureAddition::key,
            ByteBufCodecs.fromCodec(PlacedFeature.DIRECT_CODEC),
            PlacedFeatureAddition::value,
            ::PlacedFeatureAddition
        )
    }
}
