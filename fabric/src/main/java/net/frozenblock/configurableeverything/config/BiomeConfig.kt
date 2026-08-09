package net.frozenblock.configurableeverything.config

import com.mojang.datafixers.util.Either
import net.frozenblock.configurableeverything.biome.util.*
import net.frozenblock.configurableeverything.datagen.ConfigurableEverythingDataGenerator.Companion.BLANK_BIOME
import net.frozenblock.configurableeverything.datagen.ConfigurableEverythingDataGenerator.Companion.BLANK_PLACED_FEATURE
import net.frozenblock.configurableeverything.datagen.ConfigurableEverythingDataGenerator.Companion.BLANK_TAG
import net.frozenblock.configurableeverything.util.*
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.v2.entry.ConfigEntry
import net.frozenblock.lib.config.v2.entry.EntryType
import net.frozenblock.lib.sound.api.asMutable
import net.minecraft.sounds.Music
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.levelgen.GenerationStep.Decoration

private val BIOME_PLACED_FEATURE: EntryType<BiomePlacedFeatureList> = EntryType.create(
    BiomePlacedFeatureList.CODEC,
    BiomePlacedFeatureList.STREAM_CODEC,
)

private val BIOME_PLACED_FEATURE_REPLACEMENT: EntryType<BiomePlacedFeatureReplacementList> = EntryType.create(
    BiomePlacedFeatureReplacementList.CODEC,
    BiomePlacedFeatureReplacementList.STREAM_CODEC,
)

private val BIOME_MUSIC: EntryType<BiomeMusic> = EntryType.create(
    BiomeMusic.CODEC,
    BiomeMusic.STREAM_CODEC,
)

object BiomeConfig : CEConfig("biome") {
    @JvmField
    var addedFeatures: ConfigEntry<MutableList<BiomePlacedFeatureList>> = this.entry("addedFeatures",
        BIOME_PLACED_FEATURE.asList(),
        mutableListOf(
            BiomePlacedFeatureList(
                Either.left(BLANK_BIOME),
                mutableListOf(
                    DecorationStepPlacedFeature(
                        Decoration.VEGETAL_DECORATION,
                        mutableListOf(
                            BLANK_PLACED_FEATURE
                        )
                    )
                )
            ),
            BiomePlacedFeatureList(
                Either.right(BLANK_TAG),
                mutableListOf(
                    DecorationStepPlacedFeature(
                        Decoration.VEGETAL_DECORATION,
                        mutableListOf(
                            BLANK_PLACED_FEATURE
                        )
                    )
                )
            )
        )
    )

    @JvmField
    var removedFeatures: ConfigEntry<MutableList<BiomePlacedFeatureList>> = this.entry("removedFeatures",
        BIOME_PLACED_FEATURE.asList(),
        mutableListOf(
            BiomePlacedFeatureList(
                Either.left(BLANK_BIOME),
                mutableListOf(
                    DecorationStepPlacedFeature(
                        Decoration.VEGETAL_DECORATION,
                        mutableListOf(
                            BLANK_PLACED_FEATURE
                        )
                    )
                )
            ),
            BiomePlacedFeatureList(
                Either.right(BLANK_TAG),
                mutableListOf(
                    DecorationStepPlacedFeature(
                        Decoration.VEGETAL_DECORATION,
                        mutableListOf(
                            BLANK_PLACED_FEATURE
                        )
                    )
                )
            )
        )
    )

    @JvmField
    var replacedFeatures: ConfigEntry<MutableList<BiomePlacedFeatureReplacementList>> = this.entry("replacedFeatures",
        BIOME_PLACED_FEATURE_REPLACEMENT.asList(),
        mutableListOf(
            BiomePlacedFeatureReplacementList(
                Either.left(BLANK_BIOME),
                mutableListOf(
                    PlacedFeatureReplacement(
                        BLANK_PLACED_FEATURE,
                        DecorationStepPlacedFeature(
                            Decoration.VEGETAL_DECORATION,
                            mutableListOf(
                                BLANK_PLACED_FEATURE
                            )
                        )
                    )
                )
            ),
            BiomePlacedFeatureReplacementList(
                Either.right(BLANK_TAG),
                mutableListOf(
                    PlacedFeatureReplacement(
                        BLANK_PLACED_FEATURE,
                        DecorationStepPlacedFeature(
                            Decoration.VEGETAL_DECORATION,
                            mutableListOf(
                                BLANK_PLACED_FEATURE
                            )
                        )
                    )
                )
            )
        )
    )

    @JvmField
    var musicReplacements: ConfigEntry<MutableList<BiomeMusic>> = this.entry("musicReplacements",
        BIOME_MUSIC.asList(),
        mutableListOf(
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
}
