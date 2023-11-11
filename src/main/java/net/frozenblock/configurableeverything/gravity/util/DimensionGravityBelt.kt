package net.frozenblock.configurableeverything.gravity.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.frozenblock.lib.gravity.api.GravityAPI.AbsoluteGravityFunction
import net.frozenblock.lib.gravity.api.GravityAPI.GravityBelt
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.dimension.DimensionType

data class DimensionGravityBelt(
    @JvmField
    var dimension: ResourceKey<DimensionType>?,

    @JvmField
    var gravityBelts: List<GravityBelt<AbsoluteGravityFunction>?>?
) {
    companion object {
        @JvmField
        val CODEC: Codec<DimensionGravityBelt> = RecordCodecBuilder.create { instance ->
            instance.group(
                ResourceKey.codec(Registries.DIMENSION_TYPE).fieldOf("dimension").forGetter(DimensionGravityBelt::dimension),
                AbsoluteGravityFunction.BELT_CODEC.listOf().fieldOf("gravityBelts").forGetter(DimensionGravityBelt::gravityBelts)
            ).apply(instance, ::DimensionGravityBelt)
        }
    }
}
