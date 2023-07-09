package net.frozenblock.configurableeverything.config;

import blue.endless.jankson.Comment;
import com.google.gson.GsonBuilder;
import com.mojang.datafixers.util.Pair;
import net.frozenblock.configurableeverything.util.BiomeList;
import net.frozenblock.configurableeverything.util.BiomeParameters;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstants;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtils;
import net.frozenblock.lib.config.api.entry.TypedEntry;
import net.frozenblock.lib.config.api.entry.TypedEntryType;
import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.instance.json.JsonConfig;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import java.util.List;

public class BiomeConfig {

	private static final TypedEntryType<List<ResourceKey<Biome>>> BIOME_LIST = ConfigRegistry.register(
		new TypedEntryType<>(
			ConfigurableEverythingSharedConstants.MOD_ID,
			ResourceKey.codec(Registries.BIOME).listOf()
		)
	);

	private static TypedEntryType<List<BiomeList>> BIOME_PARAMETERS = ConfigRegistry.register(
		new TypedEntryType<>(
			ConfigurableEverythingSharedConstants.MOD_ID,
			BiomeList.CODEC.listOf()
		)
	);

	private static final Config<BiomeConfig> INSTANCE = ConfigRegistry.register(
		new JsonConfig<>(
			ConfigurableEverythingSharedConstants.MOD_ID,
			BiomeConfig.class,
			ConfigurableEverythingUtils.makePath("biome", true),
			true,
			new GsonBuilder()
		)
	);

	@Comment(
		"""
		This does not currently work with datapacks
		"""
	)
	public TypedEntry<List<BiomeList>> addedBiomes = new TypedEntry<>(
		BIOME_PARAMETERS,
		List.of(
			new BiomeList(
				BuiltinDimensionTypes.OVERWORLD,
				List.of(
					new BiomeParameters(
						Biomes.CHERRY_GROVE,
						Climate.parameters(
							Climate.Parameter.span(-1F, 1F),
							Climate.Parameter.span(-1F, 1F),
							Climate.Parameter.span(-1F, 1F),
							Climate.Parameter.span(-1F, 1F),
							Climate.Parameter.span(-1F, 1F),
							Climate.Parameter.span(-1F, 1F),
							0F
						)
					)
				)
			)
		)
	);

	@Comment(
		"""
		This does not currently work with datapacks
		"""
	)
	public TypedEntry<List<ResourceKey<Biome>>> removedBiomes = new TypedEntry<>(
		BIOME_LIST,
		List.of(
			ResourceKey.create(Registries.BIOME, new ResourceLocation("mangrove_swamp"))
		)
	);

	public static BiomeConfig get() {
		return INSTANCE.config();
	}
}
