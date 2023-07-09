package net.frozenblock.configurableeverything.config;

import blue.endless.jankson.Comment;
import com.google.gson.GsonBuilder;
import java.util.List;
import net.frozenblock.configurableeverything.datagen.ConfigurableEverythingDataGenerator;
import net.frozenblock.configurableeverything.biome.util.DimensionBiomeList;
import net.frozenblock.configurableeverything.biome.util.BiomeParameters;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstants;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtils;
import net.frozenblock.configurableeverything.biome.util.DimensionBiomeKeyList;
import net.frozenblock.lib.config.api.entry.TypedEntry;
import net.frozenblock.lib.config.api.entry.TypedEntryType;
import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.instance.json.JsonConfig;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;

public class BiomeConfig {

	private static final TypedEntryType<List<DimensionBiomeKeyList>> BIOME_KEY_LIST = ConfigRegistry.register(
		new TypedEntryType<>(
			ConfigurableEverythingSharedConstants.MOD_ID,
			DimensionBiomeKeyList.CODEC.listOf()
		)
	);

	private static final TypedEntryType<List<DimensionBiomeList>> BIOME_PARAMETER_LIST = ConfigRegistry.register(
		new TypedEntryType<>(
			ConfigurableEverythingSharedConstants.MOD_ID,
			DimensionBiomeList.CODEC.listOf()
		)
	);

	private static final Config<BiomeConfig> INSTANCE = ConfigRegistry.register(
		new JsonConfig<>(
			ConfigurableEverythingSharedConstants.MOD_ID,
			BiomeConfig.class,
			ConfigurableEverythingUtils.makePath("biome", true),
			true,
			new GsonBuilder()
		)
	);

	@Comment("Enables or disables this feature")
	public boolean enabled = true;

	@Comment(
		"""
		Add any biome to worldgen, with the parameters set.
		Supports: Vanilla biomes, datapack biomes
		"""
	)
	public TypedEntry<List<DimensionBiomeList>> addedBiomes = new TypedEntry<>(
		BIOME_PARAMETER_LIST,
		List.of(
			new DimensionBiomeList(
				BuiltinDimensionTypes.OVERWORLD,
				List.of(
					new BiomeParameters(
						ConfigurableEverythingDataGenerator.BLANK_BIOME,
						Climate.parameters(
							Climate.Parameter.span(-1F, 1F),
							Climate.Parameter.span(0F, 1F),
							Climate.Parameter.span(-0.3F, 0.1666667F),
							Climate.Parameter.span(-1F, -1F),
							Climate.Parameter.point(-0.5F),
							Climate.Parameter.span(-1F, 1F),
							0F
						)
					)
				)
			),
			new DimensionBiomeList(
				BuiltinDimensionTypes.NETHER,
				List.of(
					new BiomeParameters(
						ConfigurableEverythingDataGenerator.BLANK_BIOME,
						Climate.parameters(
							Climate.Parameter.span(-1F, 1F),
							Climate.Parameter.span(-1F, 1F),
							Climate.Parameter.span(-1F, 1F),
							Climate.Parameter.span(-1F, 1F),
							Climate.Parameter.span(-1F, 1F),
							Climate.Parameter.span(-1F, 1F),
							0F
						)
					)
				)
			)
		)
	);

	@Comment(
		"""
		Remove any biome from worldgen
		Supports: Vanilla biomes, datapack biomes, biomes from "addedBiomes"
		"""
	)
	public TypedEntry<List<DimensionBiomeKeyList>> removedBiomes = new TypedEntry<>(
		BIOME_KEY_LIST,
		List.of(
			new DimensionBiomeKeyList(
				BuiltinDimensionTypes.OVERWORLD,
				List.of(
					ConfigurableEverythingDataGenerator.BLANK_BIOME
				)
			),
			new DimensionBiomeKeyList(
				BuiltinDimensionTypes.NETHER,
				List.of(
					ConfigurableEverythingDataGenerator.BLANK_BIOME
				)
			)
		)
	);

	public static BiomeConfig get() {
		return INSTANCE.config();
	}
}
