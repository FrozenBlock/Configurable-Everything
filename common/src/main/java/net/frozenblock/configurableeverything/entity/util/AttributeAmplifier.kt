package net.frozenblock.configurableeverything.entity.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.ai.attributes.Attribute

data class AttributeAmplifier(
    @JvmField var attribute: ResourceKey<Attribute>,
    @JvmField var amplifier: Double
) {
    companion object {
        @JvmField
        val CODEC: Codec<AttributeAmplifier> = RecordCodecBuilder.create { instance ->
            instance.group(
                ResourceKey.codec(Registries.ATTRIBUTE).fieldOf("attribute").forGetter(AttributeAmplifier::attribute),
                Codec.DOUBLE.fieldOf("amplifier").forGetter(AttributeAmplifier::amplifier)
            ).apply(instance, ::AttributeAmplifier)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, AttributeAmplifier> = StreamCodec.composite(
            ResourceKey.streamCodec(Registries.ATTRIBUTE), AttributeAmplifier::attribute,
            ByteBufCodecs.DOUBLE, AttributeAmplifier::amplifier,
            ::AttributeAmplifier
        )
    }
}
