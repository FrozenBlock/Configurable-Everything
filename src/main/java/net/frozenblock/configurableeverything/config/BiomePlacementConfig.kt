package net.frozenblock.configurableeverything.config

import com.mojang.datafixers.util.Either

import net.frozenblock.configurableeverything.biome_placement.util.*
import net.frozenblock.configurableeverything.datagen.ConfigurableEverythingDataGenerator
import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.instance.json.JsonType
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment
import net.frozenblock.lib.worldgen.biome.api.parameters.Continentalness
import net.frozenblock.lib.worldgen.biome.api.parameters.Depth
import net.frozenblock.lib.worldgen.biome.api.parameters.Erosion
import net.frozenblock.lib.worldgen.biome.api.parameters.Humidity
import net.frozenblock.lib.worldgen.biome.api.parameters.Temperature
import net.frozenblock.lib.worldgen.biome.api.parameters.Weirdness
import net.minecraft.world.level.biome.Climate
import net.minecraft.world.level.dimension.BuiltinDimensionTypes

public class BiomePlacementConfig {
	companion object {
		private val BIOME_KEY_LIST: TypedEntryType<List<DimensionBiomeKeyList?>> = ConfigRegistry.register(
			TypedEntryType(
				MOD_ID,
				DimensionBiomeKeyList.CODEC.listOf()
			)
		)

		private val BIOME_PARAMETER_LIST: TypedEntryType<List<DimensionBiomeList>> = ConfigRegistry.register(
			TypedEntryType(
				MOD_ID,
				DimensionBiomeList.CODEC.listOf()
			)
		)

		@JvmField
		internal val INSTANCE: Config<BiomePlacementConfig> = ConfigRegistry.register(
			JsonConfig(
				MOD_ID,
				BiomePlacementConfig::class.java,
				makeConfigPath("biome_placement"),
				CONFIG_JSONTYPE
			)
		)

		@JvmStatic
		fun get() = INSTANCE.config()
	}

	@Comment(
		"""
		Add any biome to worldgen, with the parameters set.
		Adds biomes after biomes are removed via "removedBiomes",
		so replacing a biome's parameters is possible.
		Supports: Vanilla biomes, datapack biomes, modded biomes
		"""
	)
	var addedBiomes: TypedEntry<List<DimensionBiomeList>> = TypedEntry(
		BIOME_PARAMETER_LIST,
		listOf(
			DimensionBiomeList(
				BuiltinDimensionTypes.OVERWORLD,
				listOf(
					BiomeParameters(
						ConfigurableEverythingDataGenerator.BLANK_BIOME.location(),
						Climate.parameters(
							Temperature.FULL_RANGE,
							Humidity.FULL_RANGE,
							Continentalness.MUSHROOM_FIELDS,
							Erosion.FULL_RANGE,
							Climate.Parameter.span(Depth.SURFACE, Depth.FLOOR),
							Weirdness.FULL_RANGE,
							0F
						).mutable()
					)
				)
			),
			DimensionBiomeList(
				BuiltinDimensionTypes.NETHER,
				listOf(
					BiomeParameters(
						ConfigurableEverythingDataGenerator.BLANK_BIOME.location(),
						Climate.parameters(
							Climate.Parameter.span(-1F, 1F),
							Climate.Parameter.span(-1F, 1F),
							Continentalness.MUSHROOM_FIELDS,
							Climate.Parameter.span(-1F, 1F),
							Climate.Parameter.span(-1F, 1F),
							Climate.Parameter.span(-1F, 1F),
							0F
						).mutable()
					)
				)
			)
		)
	)

	@Comment(
		"""
		Remove any biome from worldgen
		Removes biomes before biomes are added via "addedBiomes",
		so replacing a biome's parameters is possible.
		Supports: Vanilla biomes, datapack biomes, Vanilla biome tags, datapack biome tags
		Does not support biomes added via TerraBlender
		"""
	)
	var removedBiomes: TypedEntry<List<DimensionBiomeKeyList?>>? = TypedEntry(
		BIOME_KEY_LIST,
		listOf(
			DimensionBiomeKeyList(
				BuiltinDimensionTypes.OVERWORLD,
				listOf(
					Either.left(ConfigurableEverythingDataGenerator.BLANK_BIOME),
					Either.right(ConfigurableEverythingDataGenerator.BLANK_TAG)
				)
			),
			DimensionBiomeKeyList(
				BuiltinDimensionTypes.NETHER,
				listOf(
					Either.left(ConfigurableEverythingDataGenerator.BLANK_BIOME)
				)
			)
		)
	)
}
