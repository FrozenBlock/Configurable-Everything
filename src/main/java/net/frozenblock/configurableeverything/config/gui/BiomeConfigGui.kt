package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.clothconfig2.gui.entries.MultiElementListEntry
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.biome.util.BiomePlacedFeatureList
import net.frozenblock.configurableeverything.biome.util.DecorationStepPlacedFeature
import net.frozenblock.configurableeverything.biome_placement.util.BiomeParameters
import net.frozenblock.configurableeverything.biome_placement.util.DimensionBiomeList
import net.frozenblock.configurableeverything.config.BiomeConfig
import net.frozenblock.configurableeverything.config.BiomePlacementConfig
import net.frozenblock.configurableeverything.datagen.ConfigurableEverythingDataGenerator.Companion.BLANK_BIOME
import net.frozenblock.configurableeverything.datagen.ConfigurableEverythingDataGenerator.Companion.BLANK_PLACED_FEATURE
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.serialization.Either
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.configurableeverything.util.tooltip
import net.frozenblock.lib.config.api.client.gui.EntryBuilder
import net.frozenblock.lib.config.api.client.gui.makeMultiElementEntry
import net.frozenblock.lib.config.api.client.gui.makeNestedList
import net.frozenblock.lib.config.api.client.gui.makeTypedEntryList
import net.frozenblock.lib.util.mutable
import net.frozenblock.lib.worldgen.biome.api.parameters.*
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.Climate
import net.minecraft.world.level.dimension.BuiltinDimensionTypes
import net.minecraft.world.level.levelgen.GenerationStep.Decoration
import net.minecraft.world.level.levelgen.placement.PlacedFeature

@Environment(EnvType.CLIENT)
object BiomeConfigGui {
    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        val config = BiomeConfig.get(real = true)
        val defaultConfig = BiomeConfig.INSTANCE.defaultInstance()
        category.background = id("textures/config/biome.png")

        category.addEntry(addedFeatures(entryBuilder, config, defaultConfig))
        category.addEntry(removedFeatures(entryBuilder, config, defaultConfig))
    }
}

private fun addedFeatures(
    entryBuilder: ConfigEntryBuilder,
    config: BiomeConfig,
    defaultConfig: BiomeConfig
): AbstractConfigListEntry<*> {
    return makeTypedEntryList(
        entryBuilder,
        text("added_features"),
        config::addedFeatures,
        {defaultConfig.addedFeatures!!},
        false,
        tooltip("added_features"),
        { newValue -> config.addedFeatures = newValue},
        { element, _ ->
            biomePlacedFeaturesElement(entryBuilder, element)
        }
    )
}

private fun removedFeatures(
    entryBuilder: ConfigEntryBuilder,
    config: BiomeConfig,
    defaultConfig: BiomeConfig
): AbstractConfigListEntry<*> {
    return makeTypedEntryList(
        entryBuilder,
        text("removed_features"),
        config::removedFeatures,
        {defaultConfig.removedFeatures!!},
        false,
        tooltip("removed_features"),
        { newValue -> config.removedFeatures = newValue},
        { element, _ ->
            biomePlacedFeaturesElement(entryBuilder, element)
        }
    )
}

private fun biomePlacedFeaturesElement(entryBuilder: ConfigEntryBuilder, element: BiomePlacedFeatureList?): AbstractConfigListEntry<BiomePlacedFeatureList?> {
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
    return makeMultiElementEntry(
        text("features.feature_list"),
        biomePlacedFeatureList,
        true,

        EntryBuilder(
            text("features.biome"), biomePlacedFeatureList.biome.toStr(),
            "",
            { newValue -> biomePlacedFeatureList.biome = newValue.toEitherKeyOrTag(Registries.BIOME) },
            tooltip("features.biome"),
            requiresRestart = true
        ).build(entryBuilder),

        makeNestedList(
            entryBuilder,
            text("features.decoration_features"),
            biomePlacedFeatureList::features,
            { defaultFeatures },
            true,
            tooltip("features.decoration_features"),
            { newValue -> biomePlacedFeatureList.features = newValue },
            { element, _ ->
                val decorationFeature: DecorationStepPlacedFeature = element ?: defaultFeature
                val decoration: Decoration = decorationFeature.decoration ?: defaultDecoration
                val placedFeatures: List<ResourceKey<PlacedFeature>?> = decorationFeature.placedFeatures ?: defaultPlacedFeatures
                makeMultiElementEntry(
                    text("features.decoration_feature"),
                    decorationFeature,
                    true,

                    entryBuilder.startEnumSelector(text("features.decoration"), Decoration::class.java, decoration)
                        .setDefaultValue(defaultDecoration)
                        .setSaveConsumer { newValue -> decorationFeature.decoration = newValue }
                        .setTooltip(tooltip("features.decoration"))
                        .requireRestart()
                        .build(),

                    entryBuilder.startStrList(text("features.placed_features"), placedFeatures.map { key -> key?.location().toString() })
                        .setDefaultValue(defaultPlacedFeatures.map { key -> key.location().toString() })
                        .setSaveConsumer { newValue -> decorationFeature.placedFeatures = newValue.map { it.toKey(Registries.PLACED_FEATURE) } }
                        .setTooltip(tooltip("features.placed_features"))
                        .requireRestart()
                        .build()
                )
            },
            requiresRestart = true
        ),
        requiresRestart = true
    )
}
