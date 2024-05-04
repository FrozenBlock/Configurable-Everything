package net.frozenblock.configurableeverything.config

import com.mojang.datafixers.util.Either
import net.frozenblock.configurableeverything.biome.util.*
import net.frozenblock.configurableeverything.datagen.ConfigurableEverythingDataGenerator.Companion.BLANK_BIOME
import net.frozenblock.configurableeverything.datagen.ConfigurableEverythingDataGenerator.Companion.BLANK_PLACED_FEATURE
import net.frozenblock.configurableeverything.datagen.ConfigurableEverythingDataGenerator.Companion.BLANK_TAG
import net.frozenblock.configurableeverything.util.*
import net.frozenblock.configurableeverything.util.CONFIG_FORMAT
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.xjs.XjsConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.annotation.EntrySyncData
import net.frozenblock.lib.config.api.sync.annotation.UnsyncableConfig
import net.frozenblock.lib.sound.api.asMutable
import net.minecraft.sounds.Music
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.levelgen.GenerationStep.Decoration

private val BIOME_PLACED_FEATURE_LIST: TypedEntryType<List<BiomePlacedFeatureList>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        BiomePlacedFeatureList.CODEC.listOf()
    )
)

private val BIOME_PLACED_FEATURE_REPLACEMENT_LIST: TypedEntryType<List<BiomePlacedFeatureReplacementList>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        BiomePlacedFeatureReplacementList.CODEC.listOf()
    )
)

private val BIOME_MUSIC_LIST: TypedEntryType<List<BiomeMusic>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        BiomeMusic.CODEC.listOf()
    )
)

@UnsyncableConfig
data class BiomeConfig(
	@JvmField
	@EntrySyncData("addedFeatures")
	var addedFeatures: TypedEntry<List<BiomePlacedFeatureList>> = TypedEntry.create(
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
	@EntrySyncData("removedFeatures")
	var removedFeatures: TypedEntry<List<BiomePlacedFeatureList>> = TypedEntry.create(
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
	@EntrySyncData("replacedFeatures")
	var replacedFeatures: TypedEntry<List<BiomePlacedFeatureReplacementList>> = TypedEntry.create(
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
	@EntrySyncData("musicReplacements")
	var musicReplacements: TypedEntry<List<BiomeMusic>> = TypedEntry.create(
		BIOME_MUSIC_LIST,
		listOf(
			BiomeMusic(
				Either.left(BLANK_BIOME),
				Music(
					SoundEvents.MUSIC_BIOME_DEEP_DARK,
					12000,
					24000,
					false
				).asMutable
			),
			BiomeMusic(
				Either.right(BLANK_TAG),
				Music(
					SoundEvents.MUSIC_BIOME_DEEP_DARK,
					12000,
					24000,
					false
				).asMutable
			)
		)
	)
) {
	companion object : CEConfig<BiomeConfig>(
        BiomeConfig::class,
        "biome"
    ) {

		init {
            ConfigRegistry.register(this)
        }

		@JvmStatic
        @JvmOverloads
		fun get(real: Boolean = false): BiomeConfig = if (real) this.instance() else this.config()
	}
}
