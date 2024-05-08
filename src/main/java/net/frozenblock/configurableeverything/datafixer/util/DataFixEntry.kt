package net.frozenblock.configurableeverything.datafixer.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.frozenblock.configurableeverything.util.mutListOf

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
    }
}
