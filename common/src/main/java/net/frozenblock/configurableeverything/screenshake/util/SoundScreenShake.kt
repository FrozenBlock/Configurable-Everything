package net.frozenblock.configurableeverything.screenshake.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier

data class SoundScreenShake(
    @JvmField var sound: Identifier,
    @JvmField var intensity: Float,
    @JvmField var duration: Int,
    @JvmField var falloffStart: Int,
    @JvmField var maxDistance: Float
) {
    companion object {
        @JvmField
		val CODEC: Codec<SoundScreenShake> = RecordCodecBuilder.create { instance ->
            instance.group(
                Identifier.CODEC.fieldOf("sound").forGetter(SoundScreenShake::sound),
                Codec.FLOAT.fieldOf("intensity").forGetter(SoundScreenShake::intensity),
                Codec.INT.fieldOf("duration").forGetter(SoundScreenShake::duration),
                Codec.INT.fieldOf("falloffStart").forGetter(SoundScreenShake::falloffStart),
                Codec.FLOAT.fieldOf("maxDistance").forGetter(SoundScreenShake::maxDistance)
            ).apply(instance, ::SoundScreenShake)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, SoundScreenShake> = StreamCodec.composite(
            Identifier.STREAM_CODEC, SoundScreenShake::sound,
            ByteBufCodecs.FLOAT, SoundScreenShake::intensity,
            ByteBufCodecs.INT, SoundScreenShake::duration,
            ByteBufCodecs.INT, SoundScreenShake::falloffStart,
            ByteBufCodecs.FLOAT, SoundScreenShake::maxDistance,
            ::SoundScreenShake
        )
    }
}
