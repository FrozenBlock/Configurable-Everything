package net.frozenblock.configurableeverything.config

import com.mojang.datafixers.util.Either
import net.frozenblock.configurableeverything.biome_placement.util.BiomeParameters
import net.frozenblock.configurableeverything.biome_placement.util.DimensionBiomeKeyList
import net.frozenblock.configurableeverything.biome_placement.util.DimensionBiomeKeyMap
import net.frozenblock.configurableeverything.biome_placement.util.DimensionBiomeList
import net.frozenblock.configurableeverything.datagen.ConfigurableEverythingDataGenerator.Companion.BLANK_BIOME
import net.frozenblock.configurableeverything.datagen.ConfigurableEverythingDataGenerator.Companion.BLANK_TAG
import net.frozenblock.configurableeverything.util.CEConfig
import net.frozenblock.lib.config.v2.entry.ConfigEntry
import net.frozenblock.lib.config.v2.entry.EntryType
import net.frozenblock.lib.worldgen.biome.api.mutable
import net.frozenblock.lib.worldgen.biome.api.parameters.*
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.biome.Climate.Parameter.span
import net.minecraft.world.level.biome.Climate.parameters
import net.minecraft.world.level.dimension.BuiltinDimensionTypes

private val BIOME_KEY_LIST: EntryType<DimensionBiomeKeyList> = EntryType.create(
    DimensionBiomeKeyList.CODEC,
    DimensionBiomeKeyList.STREAM_CODEC,
)

private val BIOME_KEY_MAP: EntryType<DimensionBiomeKeyMap> = EntryType.create(
    DimensionBiomeKeyMap.CODEC,
    DimensionBiomeKeyMap.STREAM_CODEC,
)

private val BIOME_PARAMETER_LIST: EntryType<DimensionBiomeList> = EntryType.create(
    DimensionBiomeList.CODEC,
    DimensionBiomeList.STREAM_CODEC,
)

object BiomePlacementConfig : CEConfig("biome_placement") {
    @JvmField
    var addedBiomes: ConfigEntry<MutableList<DimensionBiomeList>> = this.entry("addedBiomes",
        BIOME_PARAMETER_LIST.asList(),
        mutableListOf(
            DimensionBiomeList(
                BuiltinDimensionTypes.OVERWORLD,
                mutableListOf(
                    BiomeParameters(
                        BLANK_BIOME.identifier(),
                        parameters(
                            Temperature.FULL_RANGE,
                            Humidity.FULL_RANGE,
                            Continentalness.MUSHROOM_FIELDS,
                            Erosion.FULL_RANGE,
                            span(Depth.SURFACE, Depth.FLOOR),
                            Weirdness.FULL_RANGE,
                            0F
                        ).mutable()
                    )
                )
            ),
            DimensionBiomeList(
                BuiltinDimensionTypes.NETHER,
                mutableListOf(
                    BiomeParameters(
                        BLANK_BIOME.identifier(),
                        parameters(
                            Temperature.FULL_RANGE,
                            Humidity.FULL_RANGE,
                            Continentalness.MUSHROOM_FIELDS,
                            Erosion.FULL_RANGE,
                            span(Depth.SURFACE, Depth.FLOOR),
                            Weirdness.FULL_RANGE,
                            0F
                        ).mutable()
                    )
                )
            )
        ),
        """
        Add any biome to worldgen, with the parameters set.
        Adds biomes after biomes are removed via "removedBiomes",
        so replacing a biome's parameters is possible.
        Supports: Vanilla biomes, datapack biomes, modded biomes
        """.trimIndent()
    )

    @JvmField
    var removedBiomes: ConfigEntry<MutableList<DimensionBiomeKeyList>> = this.entry("removedBiomes",
        BIOME_KEY_LIST.asList(),
        mutableListOf(
            DimensionBiomeKeyList(
                BuiltinDimensionTypes.OVERWORLD,
                mutableListOf(
                    Either.left(BLANK_BIOME),
                    Either.right(BLANK_TAG)
                )
            ),
            DimensionBiomeKeyList(
                BuiltinDimensionTypes.NETHER,
                mutableListOf(
                    Either.left(BLANK_BIOME),
                    Either.right(BLANK_TAG)
                )
            )
        ),
        """
        Remove any biome from worldgen
        Removes biomes before biomes are added via "addedBiomes",
        so replacing a biome's parameters is possible.
        Supports: Vanilla biomes, datapack biomes, Vanilla biome tags, datapack biome tags
        Does not support biomes added via TerraBlender
        """.trimIndent()
    )

    @JvmField
    var replacedBiomes: ConfigEntry<MutableList<DimensionBiomeKeyMap>> = this.entry("replacedBiomes",
        BIOME_KEY_MAP.asList(),
        mutableListOf(
            DimensionBiomeKeyMap(
                BuiltinDimensionTypes.OVERWORLD,
                mutableMapOf(
                    Biomes.PLAINS to Biomes.DEEP_DARK
                )
            )
        )
    )
}
