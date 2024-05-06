package net.frozenblock.configurableeverything.entity.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.frozenblock.configurableeverything.util.mutListOf
import net.minecraft.resources.ResourceLocation

data class EntityHurtEffects(
    @JvmField var entity: ResourceLocation,
    @JvmField var entityName: String,
    @JvmField var effects: MutableList<MobEffectHolder>
) {
    companion object {
        @JvmField
        val CODEC: Codec<EntityHurtEffects> = RecordCodecBuilder.create { instance ->
            instance.group(
                ResourceLocation.CODEC.fieldOf("entity").forGetter(EntityHurtEffects::entity),
                Codec.STRING.fieldOf("entityName").forGetter(EntityHurtEffects::entityName),
                MobEffectHolder.CODEC.mutListOf().fieldOf("effects").forGetter(EntityHurtEffects::effects)
            ).apply(instance, ::EntityHurtEffects)
        }
    }
}
