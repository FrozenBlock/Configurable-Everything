package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.fluid.util.FluidFlowSpeed
import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.instance.json.JsonType
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.material.Fluids

class FluidConfig {
    companion object {
        private val FLUID_FLOW_SPEEDS = ConfigRegistry.register(
            TypedEntryType<List<FluidFlowSpeed?>>(
                MOD_ID,
                FluidFlowSpeed.CODEC.listOf()
            )
        )

        internal val INSTANCE: Config<FluidConfig> = ConfigRegistry.register(
            JsonConfig(
                MOD_ID,
                FluidConfig::class.java,
                makeConfigPath("fluid"),
                CONFIG_JSONTYPE
            )
        )

        @JvmStatic
        fun get(): FluidConfig = INSTANCE.config()
    }
    @JvmField
	var flowSpeeds: TypedEntry<List<FluidFlowSpeed?>>? = TypedEntry(
        FLUID_FLOW_SPEEDS,
        listOf(
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
