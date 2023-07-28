package net.frozenblock.configurableeverything.config;

import com.google.gson.GsonBuilder;
import net.frozenblock.configurableeverything.biome.util.BiomeMusic;
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
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvents;
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

	private static final TypedEntryType<List<BiomeMusic>> BIOME_MUSIC_LIST = ConfigRegistry.register(
		new TypedEntryType<>(
			ConfigurableEverythingSharedConstants.MOD_ID,
			BiomeMusic.CODEC.listOf()
		)
	);

	private static final Config<BiomeConfig> INSTANCE = ConfigRegistry.register(
		new JsonConfig<>(
			ConfigurableEverythingSharedConstants.MOD_ID,
			BiomeConfig.class,
			ConfigurableEverythingUtils.makePath("biome", true),
			true
		)
	);

	public TypedEntry<List<BiomePlacedFeatureList>> addedFeatures = new TypedEntry<>(
		BIOME_PLACED_FEATURE_LIST,
		List.of(
			new BiomePlacedFeatureList(
				ConfigurableEverythingDataGenerator.BLANK_BIOME,
				List.of(
					new DecorationStepPlacedFeature(
						GenerationStep.Decoration.VEGETAL_DECORATION,
						List.of(
							ConfigurableEverythingDataGenerator.BLANK_PLACED_FEATURE
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
				ConfigurableEverythingDataGenerator.BLANK_BIOME,
				List.of(
					new DecorationStepPlacedFeature(
						GenerationStep.Decoration.VEGETAL_DECORATION,
						List.of(
							ConfigurableEverythingDataGenerator.BLANK_PLACED_FEATURE
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
				ConfigurableEverythingDataGenerator.BLANK_BIOME,
				List.of(
					new PlacedFeatureReplacement(
						ConfigurableEverythingDataGenerator.BLANK_PLACED_FEATURE,
						new DecorationStepPlacedFeature(
							GenerationStep.Decoration.VEGETAL_DECORATION,
							List.of(
								ConfigurableEverythingDataGenerator.BLANK_PLACED_FEATURE
							)
						)
					)
				)
			)
		)
	);

	public TypedEntry<List<BiomeMusic>> musicReplacements = new TypedEntry<>(
		BIOME_MUSIC_LIST,
		List.of(
			new BiomeMusic(
				ConfigurableEverythingDataGenerator.BLANK_BIOME,
				new Music(
					SoundEvents.MUSIC_BIOME_DEEP_DARK,
					12000,
					24000,
					false
				)
			)
		)
	);

	public static BiomeConfig get() {
		return INSTANCE.config();
	}
}
