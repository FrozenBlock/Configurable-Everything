package net.frozenblock.configurableeverything.fluid.util

import net.frozenblock.configurableeverything.config.FluidConfig
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.value
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.material.Fluid

object FluidConfigUtil {

    @JvmStatic
    fun getTickDelay(original: Int, fluid: Fluid, level: LevelReader): Int {
        val config = FluidConfig.get()
        if (MainConfig.get().fluid == true) {
            config.flowSpeeds?.value?.apply {
                for (flowSpeed in this) {
                    val fSfluid = flowSpeed?.fluid ?: continue
                    val flowTickDelay = flowSpeed.flowTickDelay ?: continue
                    val ultraWarmFlowTickDelay = flowSpeed.ultraWarmFlowTickDelay ?: continue
                    if (fSfluid == BuiltInRegistries.FLUID.getResourceKey(fluid).orElseThrow()) {
                        return if (level.dimensionType().ultraWarm()) ultraWarmFlowTickDelay else flowTickDelay
                    }
                }
            }
        }
        return original
    }
}
