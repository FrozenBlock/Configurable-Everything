package net.frozenblock.configurableeverything.config

import com.mojang.datafixers.util.Either
import net.frozenblock.configurableeverything.biome_placement.util.BiomeParameters
import net.frozenblock.configurableeverything.biome_placement.util.DimensionBiomeKeyList
import net.frozenblock.configurableeverything.biome_placement.util.DimensionBiomeList
import net.frozenblock.configurableeverything.datagen.ConfigurableEverythingDataGenerator.Companion.BLANK_BIOME
import net.frozenblock.configurableeverything.datagen.ConfigurableEverythingDataGenerator.Companion.BLANK_TAG
import net.frozenblock.configurableeverything.util.CEConfig
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.mutListOf
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.annotation.EntrySyncData
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment
import net.frozenblock.lib.worldgen.biome.api.mutable
import net.frozenblock.lib.worldgen.biome.api.parameters.*
import net.minecraft.world.level.biome.Climate.Parameter.span
import net.minecraft.world.level.biome.Climate.parameters
import net.minecraft.world.level.dimension.BuiltinDimensionTypes

private val BIOME_KEY_LIST: TypedEntryType<MutableList<DimensionBiomeKeyList>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        DimensionBiomeKeyList.CODEC.mutListOf()
    )
)

private val BIOME_PARAMETER_LIST: TypedEntryType<MutableList<DimensionBiomeList>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        DimensionBiomeList.CODEC.mutListOf()
    )
)

data class BiomePlacementConfig(
	@JvmField
	@EntrySyncData("addedBiomes")
	@Comment(
"""
Add any biome to worldgen, with the parameters set.
Adds biomes after biomes are removed via "removedBiomes",
so replacing a biome's parameters is possible.
Supports: Vanilla biomes, datapack biomes, modded biomes
"""
	)
	var addedBiomes: TypedEntry<MutableList<DimensionBiomeList>> = TypedEntry.create(
		BIOME_PARAMETER_LIST,
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
		)
	),

	@JvmField
	@EntrySyncData("removedBiomes")
	@Comment(
"""
Remove any biome from worldgen
Removes biomes before biomes are added via "addedBiomes",
so replacing a biome's parameters is possible.
Supports: Vanilla biomes, datapack biomes, Vanilla biome tags, datapack biome tags
Does not support biomes added via TerraBlender
"""
	)
	var removedBiomes: TypedEntry<MutableList<DimensionBiomeKeyList>> = TypedEntry.create(
		BIOME_KEY_LIST,
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
		)
	)
) {
	companion object : CEConfig<BiomePlacementConfig>(
        BiomePlacementConfig::class,
        "biome_placement"
    ) {

        init {
            ConfigRegistry.register(this)
        }

		@JvmStatic
        @JvmOverloads
		fun get(real: Boolean = false): BiomePlacementConfig = if (real) this.instance() else this.config()
	}
}
