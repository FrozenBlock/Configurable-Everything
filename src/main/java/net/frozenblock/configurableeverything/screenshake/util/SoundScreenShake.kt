package net.frozenblock.configurableeverything.screenshake.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
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
    }
}
