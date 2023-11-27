package net.frozenblock.configurableeverything.config.gui

import com.mojang.datafixers.util.Either
import me.shedaniel.clothconfig2.api.*
import me.shedaniel.clothconfig2.gui.entries.StringListEntry
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.biome.util.*
import net.frozenblock.configurableeverything.config.BiomeConfig
import net.frozenblock.configurableeverything.datagen.ConfigurableEverythingDataGenerator.Companion.BLANK_BIOME
import net.frozenblock.configurableeverything.datagen.ConfigurableEverythingDataGenerator.Companion.BLANK_PLACED_FEATURE
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.configurableeverything.util.tooltip
import net.frozenblock.lib.config.api.client.gui.EntryBuilder
import net.frozenblock.lib.config.api.client.gui.multiElementEntry
import net.frozenblock.lib.config.api.client.gui.nestedList
import net.frozenblock.lib.config.api.client.gui.synced
import net.frozenblock.lib.config.api.client.gui.typedEntryList
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.levelgen.GenerationStep.Decoration
import net.minecraft.world.level.levelgen.placement.PlacedFeature

@Environment(EnvType.CLIENT)
object BiomeConfigGui {
    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        val config = BiomeConfig.get(real = true)
        val defaultConfig = BiomeConfig.defaultInstance()
        category.background = id("textures/config/biome.png")

        category.addEntry(addedFeatures(entryBuilder, config, defaultConfig))
        category.addEntry(removedFeatures(entryBuilder, config, defaultConfig))
        category.addEntry(replacedFeatures(entryBuilder, config, defaultConfig))
        category.addEntry(musicReplacements(entryBuilder, config, defaultConfig))
    }
}

private fun addedFeatures(
    entryBuilder: ConfigEntryBuilder,
    config: BiomeConfig,
    defaultConfig: BiomeConfig
): AbstractConfigListEntry<*> {
    return typedEntryList(
        entryBuilder,
        text("added_features"),
        config::addedFeatures,
        {defaultConfig.addedFeatures!!},
        false,
        tooltip("added_features"),
        { newValue -> config.addedFeatures = newValue},
        { element, _ ->
            biomePlacedFeaturesElement(entryBuilder, element, "added")
        }
    ).apply {
        this.requirement = Requirement.isTrue(MainConfigGui.INSTANCE!!.biome)
    }
}

private fun removedFeatures(
    entryBuilder: ConfigEntryBuilder,
    config: BiomeConfig,
    defaultConfig: BiomeConfig
): AbstractConfigListEntry<*> {
    return typedEntryList(
        entryBuilder,
        text("removed_features"),
        config::removedFeatures,
        {defaultConfig.removedFeatures!!},
        false,
        tooltip("removed_features"),
        { newValue -> config.removedFeatures = newValue },
        { element, _ ->
            biomePlacedFeaturesElement(entryBuilder, element, "removed")
        }
    ).apply {
        this.requirement = Requirement.isTrue(MainConfigGui.INSTANCE!!.biome)
    }
}

private fun replacedFeatures(
    entryBuilder: ConfigEntryBuilder,
    config: BiomeConfig,
    defaultConfig: BiomeConfig
): AbstractConfigListEntry<*> {
    return typedEntryList(
        entryBuilder,
        text("replaced_features"),
        config::replacedFeatures,
        {defaultConfig.replacedFeatures!!},
        false,
        tooltip("replaced_features"),
        { newValue -> config.replacedFeatures = newValue },
        { element, _ ->
            val defaultBiome: Either<ResourceKey<Biome>, TagKey<Biome>> = Either.left(BLANK_BIOME)
            val defaultOriginal: ResourceKey<PlacedFeature> = BLANK_PLACED_FEATURE
            val defaultDecoration: Decoration = Decoration.VEGETAL_DECORATION
            val defaultReplacement: ResourceKey<PlacedFeature> = BLANK_PLACED_FEATURE
            val defaultReplacements = listOf(defaultReplacement)
            val defaultDecorationFeature = DecorationStepPlacedFeature(
                defaultDecoration,
                defaultReplacements
            )
            val defaultReplacementFeature = PlacedFeatureReplacement(
                defaultOriginal,
                defaultDecorationFeature
            )
            val defaultReplacementFeatures = listOf(defaultReplacementFeature)
            val biomeReplacementList = element ?: BiomePlacedFeatureReplacementList(
                defaultBiome,
                defaultReplacementFeatures
            )

            multiElementEntry(
                text("replaced_features.biome_replacement_list"),
                biomeReplacementList,
                true,

                EntryBuilder(text("replaced_features.biome"), biomeReplacementList.biome.toStr(),
                    "",
                    { newValue -> biomeReplacementList.biome = newValue.toEitherKeyOrTag(Registries.BIOME) },
                    tooltip("replaced_features.biome"),
                    requiresRestart = true
                ).build(entryBuilder),

                nestedList(
                    entryBuilder,
                    text("replaced_features.replacement_list"),
                    biomeReplacementList::replacements,
                    { defaultReplacementFeatures },
                    true,
                    tooltip("replaced_features.replacement_list"),
                    { newValue -> biomeReplacementList.replacements = newValue },
                    { element, _ ->
                        val featureReplacement: PlacedFeatureReplacement = element ?: defaultReplacementFeature
                        val original: ResourceKey<PlacedFeature> = featureReplacement.original ?: defaultOriginal
                        val replacement: DecorationStepPlacedFeature = featureReplacement.replacement ?: defaultDecorationFeature
                        val decoration: Decoration = replacement.decoration ?: defaultDecoration
                        val placedFeatures: List<ResourceKey<PlacedFeature>?> = replacement.placedFeatures ?: defaultReplacements

                        multiElementEntry(
                            text("replaced_features.feature_replacement"),
                            featureReplacement,
                            true,

                            EntryBuilder(text("replaced_features.original"), original.toStr(),
                                "",
                                { newValue -> featureReplacement.original = newValue.toKey(Registries.PLACED_FEATURE) },
                                tooltip("replaced_features.original"),
                                requiresRestart = true
                            ).build(entryBuilder),

                            multiElementEntry(
                                text("replaced_features.replacement"),
                                replacement,
                                true,

                                entryBuilder.startEnumSelector(text("replaced_features.decoration"), Decoration::class.java, decoration)
                                    .setDefaultValue(defaultDecoration)
                                    .setSaveConsumer { newValue -> replacement.decoration = newValue }
                                    .setTooltip(tooltip("replaced_features.decoration"))
                                    .requireRestart()
                                    .build(),

                                entryBuilder.startStrList(text("replaced_features.placed_features"), placedFeatures.map { key -> key?.toStr() })
                                    .setDefaultValue(defaultReplacements.map { key -> key.toStr() })
                                    .setSaveConsumer { newValue -> replacement.placedFeatures = newValue.map { it.toKey(Registries.PLACED_FEATURE) } }
                                    .setTooltip(tooltip("replaced_features.placed_features"))
                                    .requireRestart()
                                    .build()
                            ),
                            requiresRestart = true
                        )
                    },
                    requiresRestart = true
                ),
                requiresRestart = true
            )
        }
    ).apply {
        this.requirement = Requirement.isTrue(MainConfigGui.INSTANCE!!.biome)
    }
}

private fun musicReplacements(
    entryBuilder: ConfigEntryBuilder,
    config: BiomeConfig,
    defaultConfig: BiomeConfig
): AbstractConfigListEntry<*> {
    return typedEntryList(
        entryBuilder,
        text("music_replacements"),
        config::musicReplacements,
        {defaultConfig.musicReplacements!!},
        false,
        tooltip("music_replacements"),
        { newValue -> config.musicReplacements = newValue },
        { element, _ ->
            val defaultSound: Holder<SoundEvent> = SoundEvents.MUSIC_BIOME_DEEP_DARK
            val defaultMinDelay: Int = 12000
            val defaultMaxDelay: Int = 24000
            val defaultReplaceCurrentMusic: Boolean = false
            val defaultMusic = MutableMusic(defaultSound, defaultMinDelay, defaultMaxDelay, defaultReplaceCurrentMusic)
            val defaultBiome: Either<ResourceKey<Biome>, TagKey<Biome>> = Either.left(BLANK_BIOME)
            val biomeMusic: BiomeMusic = element ?: BiomeMusic(defaultBiome, defaultMusic)
            val biome: Either<ResourceKey<Biome>, TagKey<Biome>> = biomeMusic.biome ?: defaultBiome
            val music: MutableMusic = biomeMusic.music ?: defaultMusic
            val sound: Holder<SoundEvent> = music.event ?: defaultSound
            val minDelay: Int = music.minDelay ?: defaultMinDelay
            val maxDelay: Int = music.maxDelay ?: defaultMaxDelay
            val replaceCurrentMusic: Boolean = music.replaceCurrentMusic ?: defaultReplaceCurrentMusic

            multiElementEntry(
                text("music_replacements.replacement"),
                biomeMusic,
                true,

                EntryBuilder(text("music_replacements.biome"), biome.toStr(),
                    "",
                    { newValue -> biomeMusic.biome = newValue.toEitherKeyOrTag(Registries.BIOME) },
                    tooltip("music_replacements.biome"),
                    requiresRestart = true
                ).build(entryBuilder),

                multiElementEntry(
                    text("music_replacements.music"),
                    music,
                    true,

                    EntryBuilder(text("music_replacements.sound"), sound.toStr(),
                        "",
                        { newValue -> music.event = newValue.toHolder(BuiltInRegistries.SOUND_EVENT) },
                        tooltip("music_replacements.sound"),
                        requiresRestart = true
                    ).build(entryBuilder),

                    EntryBuilder(text("music_replacements.min_delay"), minDelay,
                        defaultMinDelay,
                        { newValue -> music.minDelay = newValue },
                        tooltip("music_replacements.min_delay"),
                        requiresRestart = true
                    ).build(entryBuilder),

                    EntryBuilder(text("music_replacements.max_delay"), maxDelay,
                        defaultMaxDelay,
                        { newValue -> music.maxDelay = newValue },
                        tooltip("music_replacements.max_delay"),
                        requiresRestart = true
                    ).build(entryBuilder),

                    EntryBuilder(text("music_replacements.replace_current_music"), replaceCurrentMusic,
                        defaultReplaceCurrentMusic,
                        { newValue -> music.replaceCurrentMusic = newValue },
                        tooltip("music_replacements.replace_current_music"),
                        requiresRestart = true
                    ).build(entryBuilder),
                    requiresRestart = true
                ),
                requiresRestart = true
            )
        }
    ).apply {
        this.requirement = Requirement.isTrue(MainConfigGui.INSTANCE!!.biome)
    }
}

private fun biomePlacedFeaturesElement(
    entryBuilder: ConfigEntryBuilder,
    element: BiomePlacedFeatureList?,
    lang: String
): AbstractConfigListEntry<BiomePlacedFeatureList?> {
    val defaultBiome: Either<ResourceKey<Biome>, TagKey<Biome>> = Either.left(BLANK_BIOME)
    val defaultDecoration = Decoration.VEGETAL_DECORATION
    val defaultPlacedFeature = BLANK_PLACED_FEATURE
    val defaultPlacedFeatures = listOf(defaultPlacedFeature)
    val defaultFeature = DecorationStepPlacedFeature(
        defaultDecoration,
        defaultPlacedFeatures
    )
    val defaultFeatures = listOf(defaultFeature)
    val biomePlacedFeatureList: BiomePlacedFeatureList = element ?: BiomePlacedFeatureList(
        defaultBiome,
        defaultFeatures
    )
    return multiElementEntry(
        text("features.feature_list"),
        biomePlacedFeatureList,
        true,

        EntryBuilder(text("$`lang`_features.biome"), biomePlacedFeatureList.biome.toStr(),
            "",
            { newValue -> biomePlacedFeatureList.biome = newValue.toEitherKeyOrTag(Registries.BIOME) },
            tooltip("$`lang`_features.biome"),
            requiresRestart = true
        ).build(entryBuilder),

        nestedList(
            entryBuilder,
            text("$`lang`_features.decoration_features"),
            biomePlacedFeatureList::features,
            { defaultFeatures },
            true,
            tooltip("$`lang`_features.decoration_features"),
            { newValue -> biomePlacedFeatureList.features = newValue },
            { element, _ ->
                val decorationFeature: DecorationStepPlacedFeature = element ?: defaultFeature
                val decoration: Decoration = decorationFeature.decoration ?: defaultDecoration
                val placedFeatures: List<ResourceKey<PlacedFeature>?> = decorationFeature.placedFeatures ?: defaultPlacedFeatures
                multiElementEntry(
                    text("$`lang`_features.decoration_feature"),
                    decorationFeature,
                    true,

                    entryBuilder.startEnumSelector(text("$`lang`_features.decoration"), Decoration::class.java, decoration)
                        .setDefaultValue(defaultDecoration)
                        .setSaveConsumer { newValue -> decorationFeature.decoration = newValue }
                        .setTooltip(tooltip("$`lang`_features.decoration"))
                        .requireRestart()
                        .build(),

                    entryBuilder.startStrList(text("$`lang`_features.placed_features"), placedFeatures.map { key -> key?.location().toString() })
                        .setDefaultValue(defaultPlacedFeatures.map { key -> key.location().toString() })
                        .setSaveConsumer { newValue -> decorationFeature.placedFeatures = newValue.map { it.toKey(Registries.PLACED_FEATURE) } }
                        .setTooltip(tooltip("$`lang`_features.placed_features"))
                        .requireRestart()
                        .build()
                )
            },
            requiresRestart = true,
        ),
        requiresRestart = true
    ).apply {
        this.requirement = Requirement.isTrue(MainConfigGui.INSTANCE!!.biome)
    }
}
