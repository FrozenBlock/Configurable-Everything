package net.frozenblock.configurableeverything.tag.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.frozenblock.configurableeverything.util.mutListOf

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
    }
}
