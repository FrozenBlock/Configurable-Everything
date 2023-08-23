package net.frozenblock.configurableeverything.entity.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.effect.MobEffect

@JvmRecord
data class MobEffectHolder(val effect: ResourceKey<MobEffect>?, val duration: Int?, val amplifier: Int?, val ambient: Boolean?, val visible: Boolean?, val showIcon: Boolean?) {
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
