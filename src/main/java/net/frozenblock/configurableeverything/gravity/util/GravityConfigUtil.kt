package net.frozenblock.configurableeverything.gravity.util

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.launch
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

    fun init() = runBlocking {
        experimentalOrThrow()

        val config = GravityConfig.get()
        if (MainConfig.get().gravity != true) return

        val dimensionGravityBelts = config.gravityBelts?.value ?: return
        for (dimensionGravityBelt in dimensionGravityBelts) {
            launch {
                val dimension: ResourceKey<DimensionType> = dimensionGravityBelt?.dimension ?: continue
                val gravityBelts: List<GravityBelt<AbsoluteGravityFunction>?> = dimensionGravityBelt.gravityBelts ?: continue

                for (belt in gravityBelts) {
                    launch {
                        if (belt == null) continue
                        GravityAPI.register(dimension, belt)
                    }
                }
            }
        }
    }
}
