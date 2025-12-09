package net.frozenblock.configurableeverything.fluid.util

import net.frozenblock.configurableeverything.config.FluidConfig
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.value
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.attribute.EnvironmentAttributes
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.material.Fluid

object FluidConfigUtil {

    @JvmStatic
    fun getTickDelay(original: Int, fluid: Fluid, level: LevelReader): Int {
        val config = FluidConfig.get()
        if (MainConfig.get().fluid) {
            for (flowSpeed in config.flowSpeeds.value) {
                val fSfluid = flowSpeed.fluid
                val flowTickDelay = flowSpeed.flowTickDelay
                val ultraWarmFlowTickDelay = flowSpeed.ultraWarmFlowTickDelay
                if (fSfluid == BuiltInRegistries.FLUID.getResourceKey(fluid).orElseThrow()) {
                    return if (level.environmentAttributes().getDimensionValue(EnvironmentAttributes.FAST_LAVA)) ultraWarmFlowTickDelay else flowTickDelay
                }
            }
        }
        return original
    }
}
