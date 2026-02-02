package net.frozenblock.configurableeverything.entity.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.EntityType

data class ExperienceOverride(
    @JvmField var entity: ResourceKey<EntityType<*>>,
    @JvmField var amount: Int
) {
    companion object {
        @JvmField
		val CODEC: Codec<ExperienceOverride> = RecordCodecBuilder.create { instance ->
            instance.group(
                ResourceKey.codec(Registries.ENTITY_TYPE).fieldOf("entity").forGetter(ExperienceOverride::entity),
                Codec.INT.fieldOf("amount").forGetter(ExperienceOverride::amount)
            ).apply(instance, ::ExperienceOverride)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, ExperienceOverride> = StreamCodec.composite(
            ResourceKey.streamCodec(Registries.ENTITY_TYPE), ExperienceOverride::entity,
            ByteBufCodecs.INT, ExperienceOverride::amount,
            ::ExperienceOverride
        )
    }
}
