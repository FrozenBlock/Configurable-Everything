package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.biome_placement.util.BiomeParameters
import net.frozenblock.configurableeverything.biome_placement.util.DimensionBiomeList
import net.frozenblock.configurableeverything.biome_placement.util.MutableParameter
import net.frozenblock.configurableeverything.biome_placement.util.mutable
import net.frozenblock.configurableeverything.config.BiomePlacementConfig
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.configurableeverything.util.tooltip
import net.frozenblock.lib.config.api.client.gui.EntryBuilder
import net.frozenblock.lib.config.api.client.gui.makeMultiElementEntry
import net.frozenblock.lib.config.api.client.gui.makeNestedList
import net.frozenblock.lib.config.api.client.gui.makeTypedEntryList
import net.frozenblock.lib.worldgen.biome.api.parameters.*
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.biome.Climate
import net.minecraft.world.level.dimension.BuiltinDimensionTypes

@Environment(EnvType.CLIENT)
object BiomePlacementConfigGui {
    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        val config = BiomePlacementConfig.get()
        val defaultConfig = BiomePlacementConfig.INSTANCE.defaultInstance()
        category.background = id("textures/config/screen_shake.png")

        category.addEntry(addedBiomes(entryBuilder, config, defaultConfig))
        //category.addEntry(removedBiomes(entryBuilder, config, defaultConfig))
    }
}

private fun addedBiomes(
    entryBuilder: ConfigEntryBuilder,
    config: BiomePlacementConfig,
    defaultConfig: BiomePlacementConfig
): AbstractConfigListEntry<*> {
    return makeTypedEntryList(
        entryBuilder,
        text("added_biomes"),
        config::addedBiomes,
        defaultConfig::addedBiomes,
        false,
        tooltip("added_biomes"),
        { newValue -> config.addedBiomes = newValue},
        { element, _ ->
            val defaultParameters = listOf(BiomeParameters(
                ResourceLocation(""),
                Climate.parameters(
                    Temperature.NEUTRAL,
                    Humidity.NEUTRAL,
                    Continentalness.INLAND,
                    Erosion.FULL_RANGE,
                    Climate.Parameter.span(Depth.SURFACE, Depth.FLOOR),
                    Weirdness.FULL_RANGE,
                    0F
                ).mutable()
            ))
            val dimensionBiomeList = element ?: DimensionBiomeList(BuiltinDimensionTypes.OVERWORLD, defaultParameters)
            makeMultiElementEntry(
                text("added_biomes.dimension_biome_list"),
                dimensionBiomeList,
                true,

                EntryBuilder(text("added_biomes.dimension"), dimensionBiomeList.dimension.location().toString(),
                    "",
                    { newValue -> dimensionBiomeList.dimension = ResourceKey.create(Registries.DIMENSION_TYPE, ResourceLocation(newValue)) },
                    tooltip("added_biomes.dimension")
                ).build(entryBuilder),

                makeNestedList(
                    entryBuilder,
                    text("added_biomes.biome_parameter_list"),
                    dimensionBiomeList::biomes,
                    { defaultParameters },
                    true,
                    tooltip("added_biomes.biome_parameter_list"),
                    { newValue -> dimensionBiomeList.biomes = newValue },
                    { biomeParameters, _ ->
                        makeMultiElementEntry(
                            text("added_biomes.biome_parameters"),
                            biomeParameters,
                            true,

                            EntryBuilder(text("added_biomes.biome"), biomeParameters.biome.toString(),
                                "",
                                { newValue -> biomeParameters.biome = ResourceLocation(newValue) },
                                tooltip("added_biomes.biome")
                            ).build(entryBuilder),

                            makeMultiElementEntry(
                                text("added_biomes.parameters"),
                                biomeParameters.parameters,
                                true,

                                makeMultiElementEntry(
                                    text("added_biomes.temperature"),
                                    biomeParameters.parameters?.temperature,
                                    true,

                                    makeParameter(biomeParameters.parameters?.temperature, true),
                                    makeParameter(biomeParameters.parameters?.temperature, false),
                                ) as AbstractConfigListEntry<out Any>,

                                makeMultiElementEntry(
                                    text("added_biomes.humidity"),
                                    biomeParameters.parameters?.humidity,
                                    true,

                                    makeParameter(biomeParameters.parameters?.humidity, true),
                                    makeParameter(biomeParameters.parameters?.humidity, false),
                                ) as AbstractConfigListEntry<out Any>,

                                makeMultiElementEntry(
                                    text("added_biomes.continentalness"),
                                    biomeParameters.parameters?.continentalness,
                                    true,

                                    makeParameter(biomeParameters.parameters?.continentalness, true),
                                    makeParameter(biomeParameters.parameters?.continentalness, false),
                                ) as AbstractConfigListEntry<out Any>,

                                makeMultiElementEntry(
                                    text("added_biomes.erosion"),
                                    biomeParameters.parameters?.erosion,
                                    true,

                                    makeParameter(biomeParameters.parameters?.erosion, true),
                                    makeParameter(biomeParameters.parameters?.erosion, false),
                                ) as AbstractConfigListEntry<out Any>,

                                makeMultiElementEntry(
                                    text("added_biomes.depth"),
                                    biomeParameters.parameters?.depth,
                                    true,

                                    makeParameter(biomeParameters.parameters?.depth, true),
                                    makeParameter(biomeParameters.parameters?.depth, false),
                                ) as AbstractConfigListEntry<out Any>,

                                makeMultiElementEntry(
                                    text("added_biomes.weirdness"),
                                    biomeParameters.parameters?.weirdness,
                                    true,

                                    makeParameter(biomeParameters.parameters?.weirdness, true),
                                    makeParameter(biomeParameters.parameters?.weirdness, false),
                                ) as AbstractConfigListEntry<out Any>,

                                EntryBuilder(text("added_biomes.offset"), biomeParameters.parameters?.offset,
                                    0L,
                                    { newValue -> biomeParameters.parameters?.offset = newValue },
                                    tooltip("added_biomes.offset")
                                ).build(entryBuilder),
                            ) as AbstractConfigListEntry<out Any>
                        )
                    }
                )
            )
        }
    )
}

private fun makeParameter(parameter: MutableParameter?, min: Boolean): AbstractConfigListEntry<out Any> {
    val title = if (min) "added_biomes.min_value" else "added_biomes.max_value"
    return EntryBuilder(text(title), (if (min) (parameter?.min ?: 0L) else (parameter?.max ?: 0L)),
        0L,
        { newValue -> if (min) parameter?.min = newValue else parameter?.max = newValue },
        tooltip(title)
    ).build(ConfigEntryBuilder.create())
}
