package net.frozenblock.configurableeverything.entity.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.frozenblock.configurableeverything.util.mutListOf
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
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

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, EntityAttributeAmplifier> = StreamCodec.composite(
            ResourceKey.streamCodec(Registries.ENTITY_TYPE), EntityAttributeAmplifier::entity,
            ByteBufCodecs.STRING_UTF8, EntityAttributeAmplifier::entityName,
            ByteBufCodecs.list<ByteBuf, AttributeAmplifier>().apply(AttributeAmplifier.STREAM_CODEC),
            EntityAttributeAmplifier::amplifiers,
            ::EntityAttributeAmplifier
        )
    }
}
