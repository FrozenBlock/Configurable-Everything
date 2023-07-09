package net.frozenblock.configurableeverything.biome.util;

import net.fabricmc.fabric.api.biome.v1.BiomeModification;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.frozenblock.configurableeverything.config.BiomeConfig;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import java.util.function.Consumer;

public final class BiomeConfigUtil {

	// TODO: fix impl
	public static void init() {
		if (MainConfig.get().biome) {
			var config = BiomeConfig.get();
			var modification = BiomeModifications.create(ConfigurableEverythingUtils.id("feature_modifications"));

			initAddedFeatures(config, modification);
			initRemovedFeatures(config, modification);
			initReplacedFeatures(config, modification);
		}
	}

	private static void initAddedFeatures(BiomeConfig config, BiomeModification modification) {
		var addedFeatures = config.addedFeatures;

		if (addedFeatures != null && addedFeatures.value() != null) {
			for (BiomePlacedFeatureList list : addedFeatures.value()) {
				var biome = list.biome();
				var features = list.features();

				Consumer<BiomeModificationContext> biomeModifyContext = context -> {
					for (DecorationStepPlacedFeature decorationFeature : features) {
						for (ResourceKey<PlacedFeature> placedFeature : decorationFeature.placedFeatures()) {
							context.getGenerationSettings().addFeature(decorationFeature.decoration(), placedFeature);
						}
					}
				};

				biome.ifLeft(tag -> modification.add(ModificationPhase.ADDITIONS, BiomeSelectors.tag(tag), biomeModifyContext));
				biome.ifRight(biomeKey -> modification.add(ModificationPhase.ADDITIONS, BiomeSelectors.includeByKey(biomeKey), biomeModifyContext));
			}
		}
	}

	private static void initRemovedFeatures(BiomeConfig config, BiomeModification modification) {
		var removedFeatures = config.removedFeatures;

		if (removedFeatures != null && removedFeatures.value() != null) {
			for (BiomePlacedFeatureList list : removedFeatures.value()) {
				var biome = list.biome();
				var features = list.features();

				Consumer<BiomeModificationContext> biomeModifyContext = context -> {
					for (DecorationStepPlacedFeature decorationFeature : features) {
						for (ResourceKey<PlacedFeature> placedFeature : decorationFeature.placedFeatures()) {
							context.getGenerationSettings().removeFeature(decorationFeature.decoration(), placedFeature);
						}
					}
				};

				biome.ifLeft(tag -> modification.add(ModificationPhase.REMOVALS, BiomeSelectors.tag(tag), biomeModifyContext));
				biome.ifRight(biomeKey -> modification.add(ModificationPhase.REMOVALS, BiomeSelectors.includeByKey(biomeKey), biomeModifyContext));
			}
		}
	}

	private static void initReplacedFeatures(BiomeConfig config, BiomeModification modification) {
		var replacedFeatures = config.replacedFeatures;

		if (replacedFeatures != null && replacedFeatures.value() != null) {
			for (BiomePlacedFeatureReplacementList list : replacedFeatures.value()) {
				var biome = list.biome();
				var replacements = list.replacements();

				Consumer<BiomeModificationContext> biomeModifyContext = context -> {
					for (PlacedFeatureReplacement replacement : replacements) {
						context.getGenerationSettings().removeFeature(replacement.replacement().decoration(), replacement.original());
						for (ResourceKey<PlacedFeature> placedFeature : replacement.replacement().placedFeatures()) {
							context.getGenerationSettings().addFeature(replacement.replacement().decoration(), placedFeature);
						}
					}
				};

				biome.ifLeft(tag -> modification.add(ModificationPhase.REPLACEMENTS, BiomeSelectors.tag(tag), biomeModifyContext));
				biome.ifRight(biomeKey -> modification.add(ModificationPhase.REPLACEMENTS, BiomeSelectors.includeByKey(biomeKey), biomeModifyContext));
			}
		}
	}
}
