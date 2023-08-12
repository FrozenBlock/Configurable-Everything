package net.frozenblock.configurableeverything.config;

import java.util.List;
import net.frozenblock.configurableeverything.fluid.util.FluidFlowSpeed;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstants;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtils;
import net.frozenblock.lib.config.api.entry.TypedEntry;
import net.frozenblock.lib.config.api.entry.TypedEntryType;
import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.instance.json.JsonConfig;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.Fluids;

public class FluidConfig {

	private static final TypedEntryType<List<FluidFlowSpeed>> FLUID_FLOW_SPEEDS = ConfigRegistry.register(
		new TypedEntryType<>(
			ConfigurableEverythingSharedConstants.MOD_ID,
			FluidFlowSpeed.CODEC.listOf()
		)
	);

	private static final Config<FluidConfig> INSTANCE = ConfigRegistry.register(
		new JsonConfig<>(
			ConfigurableEverythingSharedConstants.MOD_ID,
			FluidConfig.class,
			ConfigurableEverythingUtils.makePath("fluid", true),
			true
		)
	);

	public TypedEntry<List<FluidFlowSpeed>> flowSpeeds = new TypedEntry<>(
		FLUID_FLOW_SPEEDS,
		List.of(
			new FluidFlowSpeed(
				BuiltInRegistries.FLUID.getResourceKey(Fluids.WATER).orElseThrow(),
				5,
				5
			),
			new FluidFlowSpeed(
				BuiltInRegistries.FLUID.getResourceKey(Fluids.LAVA).orElseThrow(),
				10,
				30
			)
		)
	);

	public static FluidConfig get() {
		return INSTANCE.config();
	}
}
