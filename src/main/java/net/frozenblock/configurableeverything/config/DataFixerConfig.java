package net.frozenblock.configurableeverything.config;

import blue.endless.jankson.Comment;
import com.mojang.serialization.Codec;
import java.util.List;
import net.frozenblock.configurableeverything.datafixer.util.DataFixEntry;
import net.frozenblock.configurableeverything.datafixer.util.Fixer;
import net.frozenblock.configurableeverything.datafixer.util.RegistryFixer;
import net.frozenblock.configurableeverything.datafixer.util.SchemaEntry;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstantsKt;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingConfigUtilsKt;
import net.frozenblock.lib.config.api.entry.TypedEntry;
import net.frozenblock.lib.config.api.entry.TypedEntryType;
import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.instance.json.JsonConfig;
import net.frozenblock.lib.config.api.instance.json.JsonType;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

public class DataFixerConfig {

	private static final TypedEntryType<List<SchemaEntry>> SCHEMA_ENTRY_LIST = ConfigRegistry.register(
		new TypedEntryType<>(
			ConfigurableEverythingSharedConstantsKt.MOD_ID,
			Codec.list(SchemaEntry.CODEC)
		)
	);

	private static final TypedEntryType<List<RegistryFixer>> REGISTRY_FIXER_LIST = ConfigRegistry.register(
		new TypedEntryType<>(
			ConfigurableEverythingSharedConstantsKt.MOD_ID,
			Codec.list(RegistryFixer.CODEC)
		)
	);

	private static final Config<DataFixerConfig> INSTANCE = ConfigRegistry.register(
		new JsonConfig<>(
			ConfigurableEverythingSharedConstantsKt.MOD_ID,
			DataFixerConfig.class,
			ConfigurableEverythingConfigUtilsKt.makeConfigPath("datafixer"),
			ConfigurableEverythingSharedConstantsKt.CONFIG_JSONTYPE
		)
	);

	@Comment(
		"""
		Allows registry fixers (not schemas) to convert all IDs
		whether or not a valid entry exists
		By default, registry fixers will only run if the entry with the ID is missing.
		WARNING: THIS CAN POTENTIALLY CAUSE UNWANTED EFFECTS TO YOUR WORLDS, USE WITH CAUTION
		"""
	)
	public boolean overrideRealEntries = false;

	@Comment(
		"""
		The data fixer's main data version. Increment this when you add a new schema.
		Any schemas with a data version higher than this will be ignored.
		"""
	)
	public int dataVersion = 0;

	@Comment(
		"""
		The list of schemas to use for data fixing.
		Each schema has a data version and a list of data fix entries.
		Each data fix entry has a type and a list of fixers.
		The four types are "biome", "block", "entity", and "item".
		Although, it is recommended to use a registry fixer for items instead of a schema fixer.
		Each fixer contains an old id and a new id, and will replace all instances of the old id with the new id.
		However, if the old id is still found in the registry, it will not be replaced.
		"""
	)
	public TypedEntry<List<SchemaEntry>> schemas = new TypedEntry<>(
		SCHEMA_ENTRY_LIST,
		List.of(
			new SchemaEntry(
				1,
				List.of(
					new DataFixEntry(
						"biome",
						List.of(
							new Fixer(
								new ResourceLocation("examplemod:example_biome"),
								new ResourceLocation("minecraft:forest")
							)
						)
					),
					new DataFixEntry(
						"block",
						List.of(
							new Fixer(
								new ResourceLocation("examplemod:dark_stone"),
								new ResourceLocation("minecraft:deepslate")
							)
						)
					),
					new DataFixEntry(
						"entity",
						List.of(
							new Fixer(
								new ResourceLocation("examplemod:example_entity"),
								new ResourceLocation("minecraft:cow")
							)
						)
					),
					new DataFixEntry(
						"item",
						List.of(
							new Fixer(
								new ResourceLocation("examplemod:example_item"),
								new ResourceLocation("minecraft:stone")
							)
						)
					)
				)
			),
			new SchemaEntry(
				2,
				List.of(
					new DataFixEntry(
						"block",
						List.of(
							new Fixer(
								new ResourceLocation("examplemod:old_block"),
								new ResourceLocation("minecraft:grass_block")
							)
						)
					)
				)
			)
		)
	);

	@Comment(
		"""
		The list of registry fixers to use for data fixing.
		Each registry fixer contains a registry key and a list of fixers.
		Each fixer contains an old id and a new id, and will replace all instances of the old id with the new id.
		However, if the old id is still found in the registry, it will not be replaced (unless the overrideRealEntries option is set to true).
		"""
	)
	public TypedEntry<List<RegistryFixer>> registryFixers = new TypedEntry<>(
		REGISTRY_FIXER_LIST,
		List.of(
			new RegistryFixer(
				Registries.BLOCK.location(),
				List.of(
					new Fixer(
						new ResourceLocation("examplemod:example_block"),
						new ResourceLocation("minecraft:stone")
					)
				)
			),
			new RegistryFixer(
				Registries.ENTITY_TYPE.location(),
				List.of(
					new Fixer(
						new ResourceLocation("examplemod:example_entity"),
						new ResourceLocation("minecraft:cow")
					)
				)
			),
			new RegistryFixer(
				Registries.ITEM.location(),
				List.of(
					new Fixer(
						new ResourceLocation("examplemod:example_item"),
						new ResourceLocation("minecraft:stone")
					)
				)
			)
		)
	);

	public static DataFixerConfig get() {
		return INSTANCE.config();
	}

	public static Config<DataFixerConfig> getConfigInstance() {
		return INSTANCE;
	}
}
