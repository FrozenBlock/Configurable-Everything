package net.frozenblock.configurableeverything.entity.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier

data class EntityFlyBySound(
    @JvmField var entity: Identifier,
    @JvmField var sound: EntityFlyBySoundData
) {
    companion object {
        @JvmField
		val CODEC: Codec<EntityFlyBySound> = RecordCodecBuilder.create { instance ->
            instance.group(
                Identifier.CODEC.fieldOf("entity").forGetter(EntityFlyBySound::entity),
                EntityFlyBySoundData.CODEC.fieldOf("sound").forGetter(EntityFlyBySound::sound)
            ).apply(instance, ::EntityFlyBySound)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, EntityFlyBySound> = StreamCodec.composite(
            Identifier.STREAM_CODEC, EntityFlyBySound::entity,
            EntityFlyBySoundData.STREAM_CODEC, EntityFlyBySound::sound,
            ::EntityFlyBySound
        )
    }
}
