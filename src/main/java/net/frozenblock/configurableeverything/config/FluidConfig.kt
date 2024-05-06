package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.fluid.util.FluidFlowSpeed
import net.frozenblock.configurableeverything.util.CEConfig
import net.frozenblock.configurableeverything.util.CONFIG_FORMAT
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.xjs.XjsConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.annotation.EntrySyncData
import net.frozenblock.lib.config.api.sync.annotation.UnsyncableConfig
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.material.Fluids

private val FLUID_FLOW_SPEEDS: TypedEntryType<MutableList<FluidFlowSpeed>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        FluidFlowSpeed.CODEC.mutListOf()
    )
)

@UnsyncableConfig
data class FluidConfig(
    @JvmField
    @EntrySyncData("flowSpeeds")
    var flowSpeeds: TypedEntry<MutableList<FluidFlowSpeed>> = TypedEntry.create(
        FLUID_FLOW_SPEEDS,
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
) {
    companion object : CEConfig<FluidConfig>(
        FluidConfig::class,
        "fluid"
    ) {

        init {
            ConfigRegistry.register(this)
        }

        @JvmStatic
        @JvmOverloads
        fun get(real: Boolean = false): FluidConfig = if (real) this.instance() else this.config()
    }
}
