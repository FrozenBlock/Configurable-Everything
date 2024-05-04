package net.frozenblock.configurableeverything.gravity.util

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.frozenblock.configurableeverything.config.GravityConfig
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.value
import net.frozenblock.lib.gravity.api.GravityAPI
import net.frozenblock.lib.gravity.api.GravityBelt
import net.frozenblock.lib.gravity.api.functions.AbsoluteGravityFunction
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.Level

internal object GravityConfigUtil {

    internal fun init() = runBlocking {
        val config = GravityConfig.get()
        if (!MainConfig.get().gravity) return@runBlocking

        val dimensionGravityBelts = config.gravityBelts.value
        for (dimensionGravityBelt in dimensionGravityBelts) { launch {
            val dimension: ResourceKey<Level> = dimensionGravityBelt.dimension
            val gravityBelts: List<GravityBelt<AbsoluteGravityFunction>> = dimensionGravityBelt.gravityBelts

            for (belt in gravityBelts) { launch {
                GravityAPI.register(dimension, belt)
            } }
        } }
    }
}
