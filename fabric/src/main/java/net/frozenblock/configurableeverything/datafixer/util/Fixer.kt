package net.frozenblock.configurableeverything.datafixer.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier

data class Fixer(
    @JvmField var oldId: Identifier,
    @JvmField var newId: Identifier
) {
    companion object {
        @JvmField
        val CODEC: Codec<Fixer> = RecordCodecBuilder.create { instance ->
            instance.group(
                Identifier.CODEC.fieldOf("old_id").forGetter(Fixer::oldId),
                Identifier.CODEC.fieldOf("new_id").forGetter(Fixer::newId)
            ).apply(instance, ::Fixer)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, Fixer> = StreamCodec.composite(
            Identifier.STREAM_CODEC, Fixer::oldId,
            Identifier.STREAM_CODEC, Fixer::newId,
            ::Fixer
        )
    }
}
