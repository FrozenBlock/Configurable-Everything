package net.frozenblock.configurableeverything.tag.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.frozenblock.configurableeverything.util.mutListOf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

data class RegistryTagModification(
    @JvmField var registry: String,
    @JvmField var modifications: MutableList<TagModification>
) {
    companion object {
        @JvmField
        val CODEC: Codec<RegistryTagModification> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.STRING.fieldOf("registry").forGetter(RegistryTagModification::registry),
                TagModification.CODEC.mutListOf().fieldOf("modifications").forGetter(RegistryTagModification::modifications),
            ).apply(instance, ::RegistryTagModification)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, RegistryTagModification> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, RegistryTagModification::registry,
            ByteBufCodecs.list<ByteBuf, TagModification>().apply(TagModification.STREAM_CODEC), RegistryTagModification::modifications,
            ::RegistryTagModification
        )
    }
}
