package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.fluid.util.FluidFlowSpeed
import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.annotation.FieldIdentifier
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.material.Fluids

private val FLUID_FLOW_SPEEDS: TypedEntryType<List<FluidFlowSpeed?>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        FluidFlowSpeed.CODEC.listOf()
    )
)

data class FluidConfig(
    @JvmField
    @FieldIdentifier(identifier = "flowSpeeds")
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
) {
    companion object : JsonConfig<FluidConfig>(
        MOD_ID,
        FluidConfig::class.java,
        makeConfigPath("fluid"),
        CONFIG_JSONTYPE,
        null,
        null
    ) {

        init {
            ConfigRegistry.register(this)
        }

        @JvmStatic
        @JvmOverloads
        fun get(real: Boolean = false): FluidConfig = if (real) this.instance() else this.config()
    }
}
