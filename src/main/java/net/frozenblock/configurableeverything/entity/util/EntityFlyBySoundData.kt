package net.frozenblock.configurableeverything.entity.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier

data class EntityFlyBySoundData(
    @JvmField var category: String,
    @JvmField var sound: Identifier,
    @JvmField var volume: Float,
    @JvmField var pitch: Float
) {
    companion object {
        @JvmField
        val CODEC: Codec<EntityFlyBySoundData> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.STRING.fieldOf("category").forGetter(EntityFlyBySoundData::category),
                Identifier.CODEC.fieldOf("sound").forGetter(EntityFlyBySoundData::sound),
                Codec.FLOAT.fieldOf("volume").forGetter(EntityFlyBySoundData::volume),
                Codec.FLOAT.fieldOf("pitch").forGetter(EntityFlyBySoundData::pitch)
            ).apply(instance, ::EntityFlyBySoundData)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, EntityFlyBySoundData> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, EntityFlyBySoundData::category,
            Identifier.STREAM_CODEC, EntityFlyBySoundData::sound,
            ByteBufCodecs.FLOAT, EntityFlyBySoundData::volume,
            ByteBufCodecs.FLOAT, EntityFlyBySoundData::pitch,
            ::EntityFlyBySoundData
        )
    }
}
