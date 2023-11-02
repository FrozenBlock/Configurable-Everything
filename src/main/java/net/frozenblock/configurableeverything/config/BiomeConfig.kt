package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.biome.util.*
import net.frozenblock.configurableeverything.datagen.ConfigurableEverythingDataGenerator.Companion.BLANK_BIOME
import net.frozenblock.configurableeverything.datagen.ConfigurableEverythingDataGenerator.Companion.BLANK_PLACED_FEATURE
import net.frozenblock.configurableeverything.datagen.ConfigurableEverythingDataGenerator.Companion.BLANK_TAG
import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.configurableeverything.util.serialization.Either
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.minecraft.sounds.Music
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.levelgen.GenerationStep.Decoration

data class BiomeConfig(
	@JvmField
	var addedFeatures: TypedEntry<List<BiomePlacedFeatureList?>?>? = TypedEntry(
		BIOME_PLACED_FEATURE_LIST,
		listOf(
			BiomePlacedFeatureList(
				Either.left(BLANK_BIOME),
				listOf(
					DecorationStepPlacedFeature(
						Decoration.VEGETAL_DECORATION,
						listOf(
							BLANK_PLACED_FEATURE
						)
					)
				)
			),
			BiomePlacedFeatureList(
				Either.right(BLANK_TAG),
				listOf(
					DecorationStepPlacedFeature(
						Decoration.VEGETAL_DECORATION,
						listOf(
							BLANK_PLACED_FEATURE
						)
					)
				)
			)
		)
	),

	@JvmField
	var removedFeatures: TypedEntry<List<BiomePlacedFeatureList?>?>? = TypedEntry(
		BIOME_PLACED_FEATURE_LIST,
		listOf(
			BiomePlacedFeatureList(
				Either.left(BLANK_BIOME),
				listOf(
					DecorationStepPlacedFeature(
						Decoration.VEGETAL_DECORATION,
						listOf(
							BLANK_PLACED_FEATURE
						)
					)
				)
			),
			BiomePlacedFeatureList(
				Either.right(BLANK_TAG),
				listOf(
					DecorationStepPlacedFeature(
						Decoration.VEGETAL_DECORATION,
						listOf(
							BLANK_PLACED_FEATURE
						)
					)
				)
			)
		)
	),

	@JvmField
	var replacedFeatures: TypedEntry<List<BiomePlacedFeatureReplacementList?>?>? = TypedEntry(
		BIOME_PLACED_FEATURE_REPLACEMENT_LIST,
		listOf(
			BiomePlacedFeatureReplacementList(
				Either.left(BLANK_BIOME),
				listOf(
					PlacedFeatureReplacement(
						BLANK_PLACED_FEATURE,
						DecorationStepPlacedFeature(
							Decoration.VEGETAL_DECORATION,
							listOf(
								BLANK_PLACED_FEATURE
							)
						)
					)
				)
			),
			BiomePlacedFeatureReplacementList(
				Either.right(BLANK_TAG),
				listOf(
					PlacedFeatureReplacement(
						BLANK_PLACED_FEATURE,
						DecorationStepPlacedFeature(
							Decoration.VEGETAL_DECORATION,
							listOf(
								BLANK_PLACED_FEATURE
							)
						)
					)
				)
			)
		)
	),

	@JvmField
	var musicReplacements: TypedEntry<List<BiomeMusic?>?>? = TypedEntry(
		BIOME_MUSIC_LIST,
		listOf(
			BiomeMusic(
				Either.left(BLANK_BIOME),
				Music(
					SoundEvents.MUSIC_BIOME_DEEP_DARK,
					12000,
					24000,
					false
				)
			),
			BiomeMusic(
				Either.right(BLANK_TAG),
				Music(
					SoundEvents.MUSIC_BIOME_DEEP_DARK,
					12000,
					24000,
					false
				)
			)
		)
	)
) {
	companion object {
		private val BIOME_PLACED_FEATURE_LIST: TypedEntryType<List<BiomePlacedFeatureList?>?> = ConfigRegistry.register(
			TypedEntryType(
				MOD_ID,
				BiomePlacedFeatureList.CODEC.listOf()
			)
		)

		private val BIOME_PLACED_FEATURE_REPLACEMENT_LIST: TypedEntryType<List<BiomePlacedFeatureReplacementList?>?> = ConfigRegistry.register(
			TypedEntryType(
				MOD_ID,
				BiomePlacedFeatureReplacementList.CODEC.listOf()
			)
		)

		private val BIOME_MUSIC_LIST: TypedEntryType<List<BiomeMusic?>?> = ConfigRegistry.register(
			TypedEntryType(
				MOD_ID,
				BiomeMusic.CODEC.listOf()
			)
		)

		@JvmField
		val INSTANCE: Config<BiomeConfig> = ConfigRegistry.register(
			JsonConfig(
				MOD_ID,
				BiomeConfig::class.java,
				makeConfigPath("biome"),
				CONFIG_JSONTYPE
			)
		)

		@JvmStatic
		fun get(real: Boolean = false): BiomeConfig = if (real) INSTANCE.instance() else INSTANCE.config()
	}
}
