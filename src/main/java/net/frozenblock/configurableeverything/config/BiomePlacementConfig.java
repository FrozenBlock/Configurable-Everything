package net.frozenblock.configurableeverything.config;

import blue.endless.jankson.Comment;
import com.google.gson.GsonBuilder;
import java.util.List;
import net.frozenblock.configurableeverything.datagen.ConfigurableEverythingDataGenerator;
import net.frozenblock.configurableeverything.biome_placement.util.DimensionBiomeList;
import net.frozenblock.configurableeverything.biome_placement.util.BiomeParameters;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstants;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtils;
import net.frozenblock.configurableeverything.biome_placement.util.DimensionBiomeKeyList;
import net.frozenblock.lib.config.api.entry.TypedEntry;
import net.frozenblock.lib.config.api.entry.TypedEntryType;
import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.instance.json.JsonConfig;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;
import net.frozenblock.lib.worldgen.biome.api.parameters.Continentalness;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;

public class BiomePlacementConfig {

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

	private static final Config<BiomePlacementConfig> INSTANCE = ConfigRegistry.register(
		new JsonConfig<>(
			ConfigurableEverythingSharedConstants.MOD_ID,
			BiomePlacementConfig.class,
			ConfigurableEverythingUtils.makePath("biome_placement", true),
			true
		)
	);

	@Comment(
		"""
		Add any biome_placement to worldgen, with the parameters set.
		Adds biomes after biomes are removed via "removedBiomes",
		so replacing a biome's parameters is possible.
		Supports: Vanilla biomes, datapack biomes
		Does not support biomes added via TerraBlender
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
							Continentalness.MUSHROOM_FIELDS,
							Climate.Parameter.span(-1F, 1F),
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
							Continentalness.MUSHROOM_FIELDS,
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
		Remove any biome_placement from worldgen
		Removes biomes before biomes are added via "addedBiomes",
		so replacing a biome's parameters is possible.
		Supports: Vanilla biomes, datapack biomes
		Does not support biomes added via TerraBlender
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

	public static BiomePlacementConfig get() {
		return INSTANCE.config();
	}
}
