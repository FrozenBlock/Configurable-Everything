@file:Environment(EnvType.CLIENT)
@file:Suppress("UnstableApiUsage")

package net.frozenblock.configurableeverything.config.gui

import com.mojang.datafixers.util.Either
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.clothconfig2.api.Requirement
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.biome_placement.util.BiomeParameters
import net.frozenblock.configurableeverything.biome_placement.util.DimensionBiomeKeyList
import net.frozenblock.configurableeverything.biome_placement.util.DimensionBiomeList
import net.frozenblock.configurableeverything.config.BiomePlacementConfig
import net.frozenblock.configurableeverything.datagen.ConfigurableEverythingDataGenerator
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.configurableeverything.util.tooltip
import net.frozenblock.lib.config.api.client.gui.EntryBuilder
import net.frozenblock.lib.config.api.client.gui.multiElementEntry
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.clothconfig.synced
import net.frozenblock.lib.worldgen.biome.api.MutableParameter
import net.frozenblock.lib.worldgen.biome.api.mutable
import net.frozenblock.lib.worldgen.biome.api.parameters.*
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.Climate
import net.minecraft.world.level.dimension.BuiltinDimensionTypes

private val configInstance: Config<BiomePlacementConfig> = BiomePlacementConfig

private inline val mainToggleReq: Requirement
    get() = Requirement.isTrue(MainConfigGui.INSTANCE!!.biomePlacement)

/**
 * An object representing the GUI for the [BiomePlacementConfig].
 */
object BiomePlacementConfigGui {
    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        val config = configInstance.instance()
        val syncConfig = configInstance.configWithSync()
        val defaultConfig = configInstance.defaultInstance()
        category.background = id("textures/config/biome_placement.png")

        category.addEntry(addedBiomes(entryBuilder, config, syncConfig, defaultConfig))
        category.addEntry(removedBiomes(entryBuilder, config, syncConfig, defaultConfig))
    }
}

private fun addedBiomes(
    entryBuilder: ConfigEntryBuilder,
    config: BiomePlacementConfig,
    syncConfig: BiomePlacementConfig,
    defaultConfig: BiomePlacementConfig
): AbstractConfigListEntry<*> {
    return typedEntryList(
        entryBuilder,
        text("added_biomes"),
        syncConfig::addedBiomes,
        { defaultConfig.addedBiomes },
        false,
        tooltip("added_biomes"),
        { newValue -> config.addedBiomes = newValue },
        { element: DimensionBiomeList?, _ ->
            val defaultParameters = mutableListOf(
                BiomeParameters(
                    ResourceLocation.withDefaultNamespace(""),
                    Climate.parameters(
                        Temperature.NEUTRAL,
                        Humidity.NEUTRAL,
                        Continentalness.INLAND,
                        Erosion.FULL_RANGE,
                        Climate.Parameter.span(Depth.SURFACE, Depth.FLOOR),
                        Weirdness.FULL_RANGE,
                        0F
                    ).mutable()
                )
            )
            val dimensionBiomeList: DimensionBiomeList =
                element ?: DimensionBiomeList(BuiltinDimensionTypes.OVERWORLD, defaultParameters)
            multiElementEntry(
                text("added_biomes.dimension_biome_list"),
                dimensionBiomeList,
                true,

                EntryBuilder(
                    text("added_biomes.dimension"),
                    dimensionBiomeList.dimension.location().toString(),
                    "",
                    { newValue -> dimensionBiomeList.dimension = newValue.toKey(Registries.DIMENSION_TYPE) },
                    tooltip("added_biomes.dimension")
                ).build(entryBuilder),

                nestedList(
                    entryBuilder,
                    text("added_biomes.biome_parameter_list"),
                    dimensionBiomeList::biomes,
                    { defaultParameters },
                    true,
                    tooltip("added_biomes.biome_parameter_list"),
                    { newValue -> dimensionBiomeList.biomes = newValue },
                    { biomeParameters, _ ->
                        multiElementEntry(
                            text("added_biomes.biome_parameters"),
                            biomeParameters,
                            true,

                            EntryBuilder(
                                text("added_biomes.biome"),
                                biomeParameters.biome.toString(),
                                "",
                                { newValue -> biomeParameters.biome = ResourceLocation.parse(newValue) },
                                tooltip("added_biomes.biome"),
                            ).build(entryBuilder),

                            multiElementEntry(
                                text("added_biomes.parameters"),
                                biomeParameters.parameters,
                                true,

                                multiElementEntry(
                                    text("added_biomes.temperature"),
                                    biomeParameters.parameters.temperature,
                                    true,

                                    makeParameter(biomeParameters.parameters.temperature, true),
                                    makeParameter(biomeParameters.parameters.temperature, false),
                                ) as AbstractConfigListEntry<out Any>,

                                multiElementEntry(
                                    text("added_biomes.humidity"),
                                    biomeParameters.parameters.humidity,
                                    true,

                                    makeParameter(biomeParameters.parameters.humidity, true),
                                    makeParameter(biomeParameters.parameters.humidity, false),
                                ) as AbstractConfigListEntry<out Any>,

                                multiElementEntry(
                                    text("added_biomes.continentalness"),
                                    biomeParameters.parameters.continentalness,
                                    true,

                                    makeParameter(biomeParameters.parameters.continentalness, true),
                                    makeParameter(biomeParameters.parameters.continentalness, false),
                                ) as AbstractConfigListEntry<out Any>,

                                multiElementEntry(
                                    text("added_biomes.erosion"),
                                    biomeParameters.parameters.erosion,
                                    true,

                                    makeParameter(biomeParameters.parameters.erosion, true),
                                    makeParameter(biomeParameters.parameters.erosion, false),
                                ) as AbstractConfigListEntry<out Any>,

                                multiElementEntry(
                                    text("added_biomes.depth"),
                                    biomeParameters.parameters.depth,
                                    true,

                                    makeParameter(biomeParameters.parameters.depth, true),
                                    makeParameter(biomeParameters.parameters.depth, false),
                                ) as AbstractConfigListEntry<out Any>,

                                multiElementEntry(
                                    text("added_biomes.weirdness"),
                                    biomeParameters.parameters.weirdness,
                                    true,

                                    makeParameter(biomeParameters.parameters.weirdness, true),
                                    makeParameter(biomeParameters.parameters.weirdness, false),
                                ) as AbstractConfigListEntry<out Any>,

                                EntryBuilder(
                                    text("added_biomes.offset"),
                                    biomeParameters.parameters.offset,
                                    0L,
                                    { newValue -> biomeParameters.parameters.offset = newValue },
                                    tooltip("added_biomes.offset")
                                ).build(entryBuilder),
                            ) as AbstractConfigListEntry<out Any>
                        )
                    }
                )
            )
        }
    ).apply {
        this.requirement = mainToggleReq
    }.synced(
        config::class,
        "addedBiomes",
        configInstance
    )
}

private fun makeParameter(parameter: MutableParameter?, min: Boolean): AbstractConfigListEntry<out Any> {
    val title = if (min) "added_biomes.min_value" else "added_biomes.max_value"
    return EntryBuilder(
        text(title),
        (if (min) (parameter?.min ?: 0L) else (parameter?.max ?: 0L)),
        0L,
        { newValue -> if (min) parameter?.min = newValue else parameter?.max = newValue },
        tooltip(title)
    ).build(ConfigEntryBuilder.create())
}

private fun removedBiomes(
    entryBuilder: ConfigEntryBuilder,
    config: BiomePlacementConfig,
    syncConfig: BiomePlacementConfig,
    defaultConfig: BiomePlacementConfig
): AbstractConfigListEntry<*> {
    return typedEntryList(
        entryBuilder,
        text("removed_biomes"),
        syncConfig::removedBiomes,
        { defaultConfig.removedBiomes },
        false,
        tooltip("removed_biomes"),
        { newValue -> config.removedBiomes = newValue },
        { element: DimensionBiomeKeyList?, _ ->
            val defaultBiomes: MutableList<Either<ResourceKey<Biome>, TagKey<Biome>>> = mutableListOf(
                Either.left(ConfigurableEverythingDataGenerator.BLANK_BIOME),
                Either.right(ConfigurableEverythingDataGenerator.BLANK_TAG)
            )
            val dimensionBiomeList = element ?: DimensionBiomeKeyList(BuiltinDimensionTypes.OVERWORLD, defaultBiomes)
            multiElementEntry(
                text("removed_biomes.dimensions"),
                dimensionBiomeList,
                true,

                EntryBuilder(
                    text("removed_biomes.dimension"),
                    dimensionBiomeList.dimension.location().toString(),
                    "",
                    { newValue -> dimensionBiomeList.dimension = ResourceKey.create(Registries.DIMENSION_TYPE, ResourceLocation.parse(newValue)) },
                    tooltip("removed_biomes.dimension")
                ).build(entryBuilder),

                entryBuilder.startStrList(
                    text("removed_biomes.biomes"),
                    dimensionBiomeList.biomes.map { either -> either.toStr() }
                )
                    .setDefaultValue(defaultBiomes.map { either -> either.toStr() })
                    .setSaveConsumer { newValue ->
                        dimensionBiomeList.biomes = newValue.map { it.toEitherKeyOrTag(Registries.BIOME) }.toMutableList()
                    }
                    .setTooltip(tooltip("removed_biomes.biomes"))
                    .build()
            )
        }
    ).apply {
        this.requirement = mainToggleReq
    }.synced(
        config::class,
        "removedBiomes",
        configInstance
    )
}
