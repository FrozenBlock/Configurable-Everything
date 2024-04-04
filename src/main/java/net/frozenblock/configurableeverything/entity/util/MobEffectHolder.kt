package net.frozenblock.configurableeverything.entity.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.effect.MobEffect

data class MobEffectHolder(
    @JvmField var effect: ResourceKey<MobEffect>,
    @JvmField var duration: Int,
    @JvmField var amplifier: Int,
    @JvmField var ambient: Boolean,
    @JvmField var visible: Boolean,
    @JvmField var showIcon: Boolean
) {
    companion object {
        @JvmField
        val CODEC: Codec<MobEffectHolder> = RecordCodecBuilder.create { instance ->
            instance.group(
                ResourceKey.codec(Registries.MOB_EFFECT).fieldOf("effect").forGetter(MobEffectHolder::effect),
                Codec.INT.fieldOf("duration").forGetter(MobEffectHolder::duration),
                Codec.INT.fieldOf("amplifier").forGetter(MobEffectHolder::amplifier),
                Codec.BOOL.fieldOf("ambient").forGetter(MobEffectHolder::ambient),
                Codec.BOOL.fieldOf("visible").forGetter(MobEffectHolder::visible),
                Codec.BOOL.fieldOf("showIcon").forGetter(MobEffectHolder::showIcon)
            ).apply(instance, ::MobEffectHolder)
        }
    }
}
