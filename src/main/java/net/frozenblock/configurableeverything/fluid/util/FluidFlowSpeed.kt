package net.frozenblock.configurableeverything.fluid.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.material.Fluid

data class FluidFlowSpeed(
    @JvmField var fluid: ResourceKey<Fluid>?,
    @JvmField var ultraWarmFlowTickDelay: Int?,
    @JvmField var flowTickDelay: Int?
) {
    companion object {
        @JvmField
		val CODEC: Codec<FluidFlowSpeed> = RecordCodecBuilder.create { instance ->
            instance.group(
                ResourceKey.codec(Registries.FLUID).fieldOf("fluid").forGetter(FluidFlowSpeed::fluid),
                Codec.INT.fieldOf("ultra_warm_flow_tick_delay").forGetter(FluidFlowSpeed::ultraWarmFlowTickDelay),
                Codec.INT.fieldOf("flow_tick_delay").forGetter(FluidFlowSpeed::flowTickDelay)
            ).apply(instance, ::FluidFlowSpeed)
        }
    }
}
