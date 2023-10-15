package net.frozenblock.configurableeverything.biome_placement.util

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.level.biome.Climate

data class MutableParameter(
    @JvmField var min: Long?,
    @JvmField var max: Long?
) {
    companion object {
        @JvmField
        val CODEC: Codec<MutableParameter> = ExtraCodecs.intervalCodec(
            Codec.floatRange(-2.0F, 2.0F),
            "min",
            "max",
            { float, float2 ->
                if (float.compareTo(float2) > 0)
                    DataResult.error { "Cannon construct interval, min > max ($float > $float2)"}
                else
                    DataResult.success(MutableParameter(Climate.quantizeCoord(float), Climate.quantizeCoord(float2)))
            },
            { range -> Climate.unquantizeCoord(range.min ?: 0L) },
            { parameter -> Climate.unquantizeCoord(parameter.max ?: 0L) }
        )
    }

    fun toImmutable(): Climate.Parameter? = if (min == null || max == null) null else Climate.Parameter(min!!, max!!)

    override fun toString(): String = "MutableParameter[min=$min, max=$max]"
}

fun Climate.Parameter.mutable(): MutableParameter = MutableParameter(min, max)
