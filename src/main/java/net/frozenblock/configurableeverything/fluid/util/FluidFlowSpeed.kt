package net.frozenblock.configurableeverything.fluid.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.material.Fluid

data class FluidFlowSpeed(
    @JvmField var fluid: ResourceKey<Fluid>,
    @JvmField var ultraWarmFlowTickDelay: Int,
    @JvmField var flowTickDelay: Int
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

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, FluidFlowSpeed> = StreamCodec.composite(
            ResourceKey.streamCodec(Registries.FLUID), FluidFlowSpeed::fluid,
            ByteBufCodecs.INT, FluidFlowSpeed::ultraWarmFlowTickDelay,
            ByteBufCodecs.INT, FluidFlowSpeed::flowTickDelay,
            ::FluidFlowSpeed
        )
    }
}
