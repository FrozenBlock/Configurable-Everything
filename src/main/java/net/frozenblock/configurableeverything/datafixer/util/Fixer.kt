package net.frozenblock.configurableeverything.datafixer.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.resources.ResourceLocation

data class Fixer(
    @JvmField var oldId: ResourceLocation,
    @JvmField var newId: ResourceLocation
) {
    companion object {
        @JvmField
        val CODEC: Codec<Fixer> = RecordCodecBuilder.create { instance ->
            instance.group(
                ResourceLocation.CODEC.fieldOf("old_id").forGetter(Fixer::oldId),
                ResourceLocation.CODEC.fieldOf("new_id").forGetter(Fixer::newId)
            ).apply(instance, ::Fixer)
        }
    }

    override fun toString(): String = "Fixer[old_id=$oldId, new_id=$newId]"
}
