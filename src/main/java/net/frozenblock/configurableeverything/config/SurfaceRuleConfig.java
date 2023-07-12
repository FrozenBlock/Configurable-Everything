package net.frozenblock.configurableeverything.config;

import com.google.gson.GsonBuilder;
import net.frozenblock.configurableeverything.datagen.ConfigurableEverythingDataGenerator;
import net.frozenblock.configurableeverything.surface_rule.util.SurfaceRuleConfigUtil;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstants;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtils;
import net.frozenblock.lib.config.api.entry.TypedEntry;
import net.frozenblock.lib.config.api.entry.TypedEntryType;
import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.instance.json.JsonConfig;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;
import net.frozenblock.lib.worldgen.surface.api.FrozenDimensionBoundRuleSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.levelgen.SurfaceRules;
import java.util.List;

public class SurfaceRuleConfig {
	public SurfaceRuleConfig() {
	}

	private static final TypedEntryType<List<FrozenDimensionBoundRuleSource>> SURFACE_RULE_LIST = ConfigRegistry.register(
		new TypedEntryType<>(
			ConfigurableEverythingSharedConstants.MOD_ID,
			SurfaceRuleConfigUtil.DIMENSION_SURFACE_RULE_CODEC.listOf()
		)
	);

	private static final Config<SurfaceRuleConfig> INSTANCE = ConfigRegistry.register(
		new JsonConfig<>(
			ConfigurableEverythingSharedConstants.MOD_ID,
			SurfaceRuleConfig.class,
			ConfigurableEverythingUtils.makePath("surface_rule", true),
			true,
			new GsonBuilder()
		)
	);

	public TypedEntry<List<FrozenDimensionBoundRuleSource>> addedSurfaceRule = new TypedEntry<>(
		SURFACE_RULE_LIST,
		List.of(
			new FrozenDimensionBoundRuleSource(
				BuiltinDimensionTypes.OVERWORLD.location(),
				SurfaceRules.sequence(
					SurfaceRules.ifTrue(
						SurfaceRules.isBiome(ConfigurableEverythingDataGenerator.BLANK_BIOME),
						SurfaceRules.state(Blocks.STONE.defaultBlockState())
					)
				)
			)
		)
	);

	public static SurfaceRuleConfig get() {
		return INSTANCE.config();
	}
}
