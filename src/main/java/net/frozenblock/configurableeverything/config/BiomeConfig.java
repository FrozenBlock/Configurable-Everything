package net.frozenblock.configurableeverything.config;

import com.google.gson.GsonBuilder;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import net.frozenblock.configurableeverything.biome.util.BiomePlacedFeatureList;
import net.frozenblock.configurableeverything.biome.util.BiomePlacedFeatureReplacementList;
import net.frozenblock.configurableeverything.biome.util.DecorationStepPlacedFeature;
import net.frozenblock.configurableeverything.biome.util.PlacedFeatureReplacement;
import net.frozenblock.configurableeverything.datagen.ConfigurableEverythingDataGenerator;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstants;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtils;
import net.frozenblock.lib.config.api.entry.TypedEntry;
import net.frozenblock.lib.config.api.entry.TypedEntryType;
import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.instance.json.JsonConfig;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.world.level.levelgen.GenerationStep;
import java.util.List;

public class BiomeConfig {

	private static final TypedEntryType<List<BiomePlacedFeatureList>> BIOME_PLACED_FEATURE_LIST = ConfigRegistry.register(
		new TypedEntryType<>(
			ConfigurableEverythingSharedConstants.MOD_ID,
			BiomePlacedFeatureList.CODEC.listOf()
		)
	);

	private static final TypedEntryType<List<BiomePlacedFeatureReplacementList>> BIOME_PLACED_FEATURE_REPLACEMENT_LIST = ConfigRegistry.register(
		new TypedEntryType<>(
			ConfigurableEverythingSharedConstants.MOD_ID,
			BiomePlacedFeatureReplacementList.CODEC.listOf()
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

	public TypedEntry<List<BiomePlacedFeatureList>> addedFeatures = new TypedEntry<>(
		BIOME_PLACED_FEATURE_LIST,
		List.of(
			new BiomePlacedFeatureList(
				Either.right(ConfigurableEverythingDataGenerator.BLANK_BIOME),
				List.of(
					new DecorationStepPlacedFeature(
						GenerationStep.Decoration.VEGETAL_DECORATION,
						List.of(
							VegetationPlacements.TREES_MANGROVE
						)
					)
				)
			)
		)
	);

	public TypedEntry<List<BiomePlacedFeatureList>> removedFeatures = new TypedEntry<>(
		BIOME_PLACED_FEATURE_LIST,
		List.of(
			new BiomePlacedFeatureList(
				Either.right(ConfigurableEverythingDataGenerator.BLANK_BIOME),
				List.of(
					new DecorationStepPlacedFeature(
						GenerationStep.Decoration.VEGETAL_DECORATION,
						List.of(
							VegetationPlacements.TREES_MANGROVE
						)
					)
				)
			)
		)
	);

	public TypedEntry<List<BiomePlacedFeatureReplacementList>> replacedFeatures = new TypedEntry<>(
		BIOME_PLACED_FEATURE_REPLACEMENT_LIST,
		List.of(
			new BiomePlacedFeatureReplacementList(
				Either.right(ConfigurableEverythingDataGenerator.BLANK_BIOME),
				List.of(
					new PlacedFeatureReplacement(
						VegetationPlacements.TREES_MANGROVE,
						new DecorationStepPlacedFeature(
							GenerationStep.Decoration.VEGETAL_DECORATION,
							List.of(
								VegetationPlacements.TREES_MANGROVE
							)
						)
					)
				)
			)
		)
	);

	public static BiomeConfig get() {
		return INSTANCE.config();
	}
}
