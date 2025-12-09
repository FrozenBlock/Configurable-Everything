package net.frozenblock.configurableeverything.datafixer.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
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
    }
}
