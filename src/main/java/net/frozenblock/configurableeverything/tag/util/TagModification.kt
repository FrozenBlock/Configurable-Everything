package net.frozenblock.configurableeverything.tag.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.frozenblock.configurableeverything.util.mutListOf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

data class TagModification(
    @JvmField var tag: String,
    @JvmField var additions: MutableList<String>,
    @JvmField var removals: MutableList<String>
) {
    companion object {
        @JvmField
        val CODEC: Codec<TagModification> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.STRING.fieldOf("tag").forGetter(TagModification::tag),
                Codec.STRING.mutListOf().fieldOf("additions").forGetter(TagModification::additions),
                Codec.STRING.mutListOf().fieldOf("removals").forGetter(TagModification::removals)
            ).apply(instance, ::TagModification)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, TagModification> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, TagModification::tag,
            ByteBufCodecs.list<ByteBuf, String>().apply(ByteBufCodecs.STRING_UTF8), TagModification::additions,
            ByteBufCodecs.list<ByteBuf, String>().apply(ByteBufCodecs.STRING_UTF8), TagModification::removals,
            ::TagModification
        )
    }
}
