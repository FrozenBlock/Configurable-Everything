@file:Environment(EnvType.CLIENT)

package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.FluidConfig
import net.frozenblock.configurableeverything.config.MixinsConfig
import net.frozenblock.configurableeverything.fluid.util.FluidFlowSpeed
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.configurableeverything.util.tooltip
import net.frozenblock.lib.config.api.client.gui.EntryBuilder
import net.frozenblock.lib.config.api.client.gui.multiElementEntry
import net.frozenblock.lib.config.api.client.gui.typedEntryList
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.material.Fluids

@Environment(EnvType.CLIENT)
object FluidConfigGui {
    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        val config = FluidConfig.get()
        val defaultConfig = FluidConfig.defaultInstance()
        category.background = id("textures/config/fluid.png")

        category.addEntry(fluidFlowSpeeds(entryBuilder, config, defaultConfig))
    }
}

private fun fluidFlowSpeeds(
    entryBuilder: ConfigEntryBuilder,
    config: FluidConfig,
    defaultConfig: FluidConfig
): AbstractConfigListEntry<*> {
    val defaultFluid = BuiltInRegistries.FLUID.getResourceKey(Fluids.WATER).orElseThrow()
    val defaultUltraWarmFlowTickDelay: Int = 5
    val defaultTickDelay: Int = 5
    val defaultFlowSpeed: FluidFlowSpeed = FluidFlowSpeed(defaultFluid, defaultUltraWarmFlowTickDelay, defaultTickDelay)
    val defaultFlowSpeeds: List<FluidFlowSpeed> = listOf(defaultFlowSpeed)
    return typedEntryList(
        entryBuilder,
        text("fluid_flow_speeds"),
        config::flowSpeeds,
        { defaultConfig.flowSpeeds!! },
        false,
        tooltip("fluid_flow_speeds"),
        { newValue -> config.flowSpeeds = newValue },
        { element, _ ->
            val fluidFlowSpeed = element ?: defaultFlowSpeed
            val ultraWarm = fluidFlowSpeed.ultraWarmFlowTickDelay ?: defaultUltraWarmFlowTickDelay
            val tickDelay = fluidFlowSpeed.flowTickDelay ?: defaultTickDelay

            multiElementEntry(
                text("fluid_flow_speeds.fluid_flow_speed"),
                fluidFlowSpeed,
                true,

                EntryBuilder(
                    text("fluid_flow_speeds.fluid"),
                    fluidFlowSpeed.fluid.toStr(),
                    "",
                    { newValue -> fluidFlowSpeed.fluid = newValue.toKey(Registries.FLUID) },
                    tooltip("fluid_flow_speeds.fluid")
                ).build(entryBuilder),

                EntryBuilder(
                    text("fluid_flow_speeds.ultra_warm_flow_tick_speed"),
                    ultraWarm,
                    10,
                    { newValue -> fluidFlowSpeed.ultraWarmFlowTickDelay = newValue },
                    tooltip("fluid_flow_speeds.ultra_warm_flow_tick_speed")
                ).build(entryBuilder),

                EntryBuilder(
                    text("fluid_flow_speeds.tick_delay"),
                    tickDelay,
                    5,
                    { newValue -> fluidFlowSpeed.flowTickDelay = newValue },
                    tooltip("fluid_flow_speeds.tick_delay")
                ).build(entryBuilder)
            )
        }
    )
}
