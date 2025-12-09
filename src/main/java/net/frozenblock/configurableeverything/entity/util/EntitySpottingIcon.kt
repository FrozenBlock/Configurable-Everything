package net.frozenblock.configurableeverything.entity.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.EntityType

data class EntitySpottingIcon(
	@JvmField var entity: ResourceKey<EntityType<*>>,
	@JvmField var texture: Identifier,
	@JvmField var startFade: Float,
	@JvmField var endFade: Float
) {
    companion object {
        @JvmField
		val CODEC: Codec<EntitySpottingIcon> = RecordCodecBuilder.create { instance ->
            instance.group(
                ResourceKey.codec(Registries.ENTITY_TYPE).fieldOf("entity").forGetter(EntitySpottingIcon::entity),
                Identifier.CODEC.fieldOf("texture").forGetter(EntitySpottingIcon::texture),
                Codec.FLOAT.fieldOf("startFade").forGetter(EntitySpottingIcon::startFade),
                Codec.FLOAT.fieldOf("endFade").forGetter(EntitySpottingIcon::endFade)
            ).apply(instance, ::EntitySpottingIcon)
        }
    }
}
