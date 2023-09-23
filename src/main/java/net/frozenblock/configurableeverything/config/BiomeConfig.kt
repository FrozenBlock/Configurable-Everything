package net.frozenblock.configurableeverything.config

import com.mojang.datafixers.util.Either
import net.frozenblock.configurableeverything.biome.util.BiomeMusic
import net.frozenblock.configurableeverything.biome.util.BiomePlacedFeatureList
import net.frozenblock.configurableeverything.biome.util.BiomePlacedFeatureReplacementList
import net.frozenblock.configurableeverything.biome.util.DecorationStepPlacedFeature
import net.frozenblock.configurableeverything.biome.util.PlacedFeatureReplacement
import net.frozenblock.configurableeverything.datagen.ConfigurableEverythingDataGenerator.*
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.instance.json.JsonType
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.minecraft.sounds.Music
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.levelgen.GenerationStep.Decoration
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE

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
		internal val INSTANCE: Config<BiomeConfig> = ConfigRegistry.register(
			JsonConfig(
				MOD_ID,
				BiomeConfig::class.java,
				makeConfigPath("biome"),
				CONFIG_JSONTYPE
			)
		)

		@JvmStatic
		fun get(): BiomeConfig = INSTANCE.config()
	}
}
