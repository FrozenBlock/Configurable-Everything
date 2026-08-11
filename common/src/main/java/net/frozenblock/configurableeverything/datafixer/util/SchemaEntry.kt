package net.frozenblock.configurableeverything.datafixer.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.frozenblock.configurableeverything.util.mutListOf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

data class SchemaEntry(
    @JvmField var version: Int,
    @JvmField var entries: MutableList<DataFixEntry>
) {
    companion object {
        @JvmField
        val CODEC: Codec<SchemaEntry> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.INT.fieldOf("version").forGetter(SchemaEntry::version),
                DataFixEntry.CODEC.mutListOf().fieldOf("data_fixes").forGetter(SchemaEntry::entries)
            ).apply(instance, ::SchemaEntry)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, SchemaEntry> = StreamCodec.composite(
            ByteBufCodecs.INT, SchemaEntry::version,
            ByteBufCodecs.list<ByteBuf, DataFixEntry>().apply(DataFixEntry.STREAM_CODEC), SchemaEntry::entries,
            ::SchemaEntry
        )
    }
}
