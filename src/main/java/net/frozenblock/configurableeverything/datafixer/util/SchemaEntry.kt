package net.frozenblock.configurableeverything.datafixer.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.frozenblock.configurableeverything.util.mutListOf

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
    }
}
