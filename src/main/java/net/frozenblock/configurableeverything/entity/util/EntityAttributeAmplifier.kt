package net.frozenblock.configurableeverything.entity.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.EntityType

data class EntityAttributeAmplifier(
	@JvmField var entity: ResourceKey<EntityType<*>>,
	@JvmField var entityName: String,
	@JvmField var amplifiers: MutableList<AttributeAmplifier>
) {
    companion object {
        @JvmField
		val CODEC: Codec<EntityAttributeAmplifier> = RecordCodecBuilder.create { instance ->
            instance.group(
                ResourceKey.codec(Registries.ENTITY_TYPE).fieldOf("entity").forGetter(EntityAttributeAmplifier::entity),
                Codec.STRING.fieldOf("entityName").forGetter(EntityAttributeAmplifier::entityName),
                AttributeAmplifier.CODEC.mutListOf().fieldOf("amplifiers").forGetter(EntityAttributeAmplifier::amplifiers)
            ).apply(instance, ::EntityAttributeAmplifier)
        }
    }
}
