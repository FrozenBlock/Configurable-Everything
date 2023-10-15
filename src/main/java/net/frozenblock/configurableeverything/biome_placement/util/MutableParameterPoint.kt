package net.frozenblock.configurableeverything.biome_placement.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.biome.Climate
import net.minecraft.world.level.biome.Climate.ParameterPoint

data class MutableParameterPoint(
    @JvmField var temperature: MutableParameter,
    @JvmField var humidity: MutableParameter,
    @JvmField var continentalness: MutableParameter,
    @JvmField var erosion: MutableParameter,
    @JvmField var depth: MutableParameter,
    @JvmField var weirdness: MutableParameter,
    @JvmField var offset: Long
) {
    companion object {
        @JvmField
        val CODEC: Codec<MutableParameterPoint> = RecordCodecBuilder.create { instance ->
                instance.group(
                    MutableParameter.CODEC.fieldOf("temperature").forGetter(MutableParameterPoint::temperature),
                    MutableParameter.CODEC.fieldOf("humidity").forGetter(MutableParameterPoint::humidity),
                    MutableParameter.CODEC.fieldOf("continentalness").forGetter(MutableParameterPoint::continentalness),
                    MutableParameter.CODEC.fieldOf("erosion").forGetter(MutableParameterPoint::erosion),
                    MutableParameter.CODEC.fieldOf("depth").forGetter(MutableParameterPoint::depth),
                    MutableParameter.CODEC.fieldOf("weirdness").forGetter(MutableParameterPoint::weirdness),
                    Codec.floatRange(0.0F, 1.0F).fieldOf("offset").xmap(Climate::quantizeCoord, Climate::unquantizeCoord).forGetter(MutableParameterPoint::offset)
                ).apply(instance, ::MutableParameterPoint)
            }
    }

    fun toImmutable(): ParameterPoint = ParameterPoint(temperature.toImmutable(), humidity.toImmutable(), continentalness.toImmutable(), erosion.toImmutable(), depth.toImmutable(), weirdness.toImmutable(), offset)

    override fun toString(): String = "MutableParameterPoint[temperature=$temperature, humidity=$humidity, continentalness=$continentalness, erosion=$erosion, depth=$depth, weirdness=$weirdness, offset=$offset]"
}

fun ParameterPoint.mutable(): MutableParameterPoint = MutableParameterPoint(temperature.mutable(), humidity.mutable(), continentalness.mutable(), erosion.mutable(), depth.mutable(), weirdness.mutable(), offset)
