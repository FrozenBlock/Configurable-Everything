package net.frozenblock.configurableeverything.datafixer.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class SchemaEntry(
    @JvmField var version: Int,
    @JvmField var entries: List<DataFixEntry>
) {
    companion object {
        @JvmField
        val CODEC: Codec<SchemaEntry> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.INT.fieldOf("version").forGetter(SchemaEntry::version),
                Codec.list(DataFixEntry.CODEC).fieldOf("data_fixes").forGetter(SchemaEntry::entries)
            ).apply(instance, ::SchemaEntry)
        }
    }

    override fun toString(): String = "SchemaEntry[$version, $entries]"
}
