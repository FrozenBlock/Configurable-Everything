package net.frozenblock.configurableeverything.config;

import blue.endless.jankson.Comment;
import com.google.gson.GsonBuilder;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstants;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtils;
import net.frozenblock.lib.config.api.entry.TypedEntry;
import net.frozenblock.lib.config.api.entry.TypedEntryType;
import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.instance.json.JsonConfig;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import java.util.List;

public class BiomeConfig {

	private static final TypedEntryType<List<ResourceKey<Biome>>> BIOME_LIST = ConfigRegistry.register(
		new TypedEntryType<>(
			ConfigurableEverythingSharedConstants.MOD_ID,
			ResourceKey.codec(Registries.BIOME).listOf()
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
		This is a test
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
