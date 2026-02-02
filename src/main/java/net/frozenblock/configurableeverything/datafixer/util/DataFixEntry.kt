package net.frozenblock.configurableeverything.datafixer.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.frozenblock.configurableeverything.util.mutListOf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

data class DataFixEntry(
    @JvmField var type: String,
    @JvmField var fixers: MutableList<Fixer>
) {
    companion object {
        @JvmField
        val CODEC: Codec<DataFixEntry> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.STRING.fieldOf("type").forGetter(DataFixEntry::type),
                Fixer.CODEC.mutListOf().fieldOf("fixers").forGetter(DataFixEntry::fixers)
            ).apply(instance, ::DataFixEntry)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, DataFixEntry> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, DataFixEntry::type,
            ByteBufCodecs.list<ByteBuf, Fixer>().apply(Fixer.STREAM_CODEC), DataFixEntry::fixers,
            ::DataFixEntry
        )
    }
}
