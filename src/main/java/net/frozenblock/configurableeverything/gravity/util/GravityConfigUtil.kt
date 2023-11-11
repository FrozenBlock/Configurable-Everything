package net.frozenblock.configurableeverything.gravity.util

import net.frozenblock.configurableeverything.config.GravityConfig
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.experimental
import net.frozenblock.configurableeverything.util.experimentalOrThrow
import net.frozenblock.lib.gravity.api.GravityAPI
import net.frozenblock.lib.gravity.api.GravityAPI.AbsoluteGravityFunction
import net.frozenblock.lib.gravity.api.GravityAPI.GravityBelt
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.dimension.DimensionType

object GravityConfigUtil {

    @JvmStatic
    fun init() {
        experimentalOrThrow()

        val config = GravityConfig.get()
        if (MainConfig.get().gravity != true) return

        val dimensionGravityBelts = config.gravityBelts?.value ?: return
        for (dimensionGravityBelt in dimensionGravityBelts) {
            val dimension: ResourceKey<DimensionType> = dimensionGravityBelt?.dimension ?: continue
            val gravityBelts: List<GravityBelt<AbsoluteGravityFunction>?> = dimensionGravityBelt.gravityBelts ?: continue

            for (belt in gravityBelts) {
                if (belt == null) continue
                GravityAPI.register(dimension, belt)
            }
        }
    }
}
