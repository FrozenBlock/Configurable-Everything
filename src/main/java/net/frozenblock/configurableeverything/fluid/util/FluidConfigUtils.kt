package net.frozenblock.configurableeverything.fluid.util

import net.frozenblock.configurableeverything.config.FluidConfig
import net.frozenblock.configurableeverything.config.MainConfig
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.material.Fluid

object FluidConfigUtils {

    @JvmStatic
    fun getTickDelay(original: Int, fluid: Fluid, level: LevelReader): Int {
        val config = FluidConfig.get()
        if (MainConfig.get().fluid == true) {
            val flowSpeeds = config.flowSpeeds
            if (flowSpeeds?.value != null) {
                for (flowSpeed in flowSpeeds.value) {
                    if (flowSpeed?.fluid == null || flowSpeed.flowTickDelay == null || flowSpeed.ultraWarmFlowTickDelay == null) continue
                    if (flowSpeed.fluid == BuiltInRegistries.FLUID.getResourceKey(fluid).orElseThrow()) {
                        return if (level.dimensionType().ultraWarm()) flowSpeed.ultraWarmFlowTickDelay else flowSpeed.flowTickDelay
                    }
                }
            }
        }
        return original
    }
}
