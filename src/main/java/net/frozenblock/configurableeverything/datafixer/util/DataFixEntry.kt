package net.frozenblock.configurableeverything.datafixer.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class DataFixEntry(
    @JvmField var type: String,
    @JvmField var fixers: List<Fixer>
) {
    companion object {
        @JvmField
        val CODEC: Codec<DataFixEntry> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.STRING.fieldOf("type").forGetter(DataFixEntry::type),
                Codec.list(Fixer.CODEC).fieldOf("fixers").forGetter(DataFixEntry::fixers)
            ).apply(instance, ::DataFixEntry)
        }
    }
}
