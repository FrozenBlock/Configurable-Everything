package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.fluid.util.FluidFlowSpeed
import net.frozenblock.configurableeverything.util.*
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.xjs.XjsConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.v2.entry.ConfigEntry
import net.frozenblock.lib.config.v2.entry.EntryType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.material.Fluids

private val FLUID_FLOW_SPEEDS = EntryType.create(
    FluidFlowSpeed.CODEC,
    FluidFlowSpeed.STREAM_CODEC
)

object FluidConfig : CEConfig("fluid") {
    @JvmField
    var flowSpeeds: ConfigEntry<MutableList<FluidFlowSpeed>> = this.entry("flowSpeeds",
        FLUID_FLOW_SPEEDS.asList(),
        mutableListOf(
            FluidFlowSpeed(
                BuiltInRegistries.FLUID.getResourceKey(Fluids.WATER).orElseThrow(),
                5,
                5
            ),
            FluidFlowSpeed(
                BuiltInRegistries.FLUID.getResourceKey(Fluids.LAVA).orElseThrow(),
                10,
                30
            )
        )
    )
}
