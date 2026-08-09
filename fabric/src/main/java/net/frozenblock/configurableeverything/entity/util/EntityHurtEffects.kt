package net.frozenblock.configurableeverything.entity.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.frozenblock.configurableeverything.util.mutListOf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier

data class EntityHurtEffects(
    @JvmField var entity: Identifier,
    @JvmField var entityName: String,
    @JvmField var effects: MutableList<MobEffectHolder>
) {
    companion object {
        @JvmField
        val CODEC: Codec<EntityHurtEffects> = RecordCodecBuilder.create { instance ->
            instance.group(
                Identifier.CODEC.fieldOf("entity").forGetter(EntityHurtEffects::entity),
                Codec.STRING.fieldOf("entityName").forGetter(EntityHurtEffects::entityName),
                MobEffectHolder.CODEC.mutListOf().fieldOf("effects").forGetter(EntityHurtEffects::effects)
            ).apply(instance, ::EntityHurtEffects)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, EntityHurtEffects> = StreamCodec.composite(
            Identifier.STREAM_CODEC, EntityHurtEffects::entity,
            ByteBufCodecs.STRING_UTF8, EntityHurtEffects::entityName,
            ByteBufCodecs.list<ByteBuf, MobEffectHolder>().apply(MobEffectHolder.STREAM_CODEC),
            EntityHurtEffects::effects,
            ::EntityHurtEffects
        )
    }
}
