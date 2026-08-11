package net.frozenblock.configurableeverything.gravity.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.frozenblock.configurableeverything.util.mutListOf
import net.frozenblock.lib.gravity.api.GravityBelt
import net.frozenblock.lib.gravity.api.functions.AbsoluteGravityFunction
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.Level

data class DimensionGravityBelt(
    @JvmField
    var dimension: ResourceKey<Level>,

    @JvmField
    var gravityBelts: MutableList<GravityBelt<AbsoluteGravityFunction>>
) {
    companion object {
        @JvmField
        val CODEC: Codec<DimensionGravityBelt> = RecordCodecBuilder.create { instance ->
            instance.group(
                ResourceKey.codec(Registries.DIMENSION).fieldOf("dimension").forGetter(DimensionGravityBelt::dimension),
                AbsoluteGravityFunction.BELT_CODEC.mutListOf().fieldOf("gravityBelts").forGetter(DimensionGravityBelt::gravityBelts)
            ).apply(instance, ::DimensionGravityBelt)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, DimensionGravityBelt> = StreamCodec.composite(
            ResourceKey.streamCodec(Registries.DIMENSION), DimensionGravityBelt::dimension,
            ByteBufCodecs.list<ByteBuf, GravityBelt<AbsoluteGravityFunction>>()
                .apply(AbsoluteGravityFunction.BELT_STREAM_CODEC), DimensionGravityBelt::gravityBelts,
            ::DimensionGravityBelt
        )
    }
}
