package net.frozenblock.configurableeverything.datafixer.util;

import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;
import net.fabricmc.loader.api.ModContainer;
import net.frozenblock.configurableeverything.config.DataFixerConfig;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstants;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.qsl.frozenblock.misc.datafixerupper.api.QuiltDataFixerBuilder;
import org.quiltmc.qsl.frozenblock.misc.datafixerupper.api.QuiltDataFixes;
import org.quiltmc.qsl.frozenblock.misc.datafixerupper.api.SimpleFixes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataFixerUtils {

	private static final List<SchemaEntry> SCHEMAS = new ArrayList<>();

	private static final List<RegistryFixer> REGISTRY_FIXERS = new ArrayList<>();

	public static void addSchema(SchemaEntry schema) {
		SCHEMAS.add(schema);
	}

	public static void addRegistryFixer(RegistryFixer fixer) {
		REGISTRY_FIXERS.add(fixer);
	}

	public static List<SchemaEntry> getSchemas() {
		var list = new ArrayList<>(SCHEMAS);
		list.addAll(DataFixerConfig.get().schemas.value());
		return list;
	}

	public static List<RegistryFixer> getRegistryFixers() {
		var list = new ArrayList<>(REGISTRY_FIXERS);
		list.addAll(DataFixerConfig.get().registryFixers.value());
		return list;
	}

	public static void applyDataFixes(final @NotNull ModContainer mod) {
		if (MainConfig.get().datafixer) {
			ConfigurableEverythingUtils.log("Applying configurable data fixes", ConfigurableEverythingSharedConstants.UNSTABLE_LOGGING);
			var config = DataFixerConfig.get();
			var schemas = DataFixerUtils.getSchemas();
			var dataVersion = config.dataVersion;

			var builder = new QuiltDataFixerBuilder(dataVersion);

			var maxSchema = 0;
			List<Schema> addedSchemas = new ArrayList<>();
			if (schemas.size() > 0) {
				var base = builder.addSchema(0, QuiltDataFixes.BASE_SCHEMA);
				addedSchemas.add(base);
			}

			for (var fix : schemas) {
				var version = fix.version();
				if (version > dataVersion) {
					ConfigurableEverythingUtils.error("Data fix version " + version + " is higher than the current data version " + dataVersion, true);
					continue;
				}

				if (version > maxSchema) {
					var schema = builder.addSchema(version, NamespacedSchema::new);
					addedSchemas.add(schema);
					maxSchema = version;
				}

				try {
					var schema = addedSchemas.get(version);

					for (var entry : fix.entries()) {
						for (var fixer : entry.fixers()) {
							handleFixer(builder, schema, entry, fixer);
						}
					}
				} catch (IndexOutOfBoundsException e) {
					ConfigurableEverythingUtils.error("Invalid data fix version: " + version, true);
				}
			}

			QuiltDataFixes.buildAndRegisterFixer(mod, builder);
			ConfigurableEverythingUtils.log(
				"Finished applying configurable data fixes"
					+ "\nData Version: " + dataVersion
					+ "\nMax schema: " + maxSchema,
				ConfigurableEverythingSharedConstants.UNSTABLE_LOGGING
			);
		}
	}

	private static void handleFixer(DataFixerBuilder builder, Schema schema, DataFixEntry entry, Fixer fixer) {
		var oldId = fixer.oldId();
		var newId = fixer.newId();

		var fixName = "fix_" + oldId + "_to_" + newId;

		switch (entry.type()) {
			case "biome" -> SimpleFixes.addBiomeRenameFix(builder, fixName, Map.of(oldId, newId), schema);
			case "block" -> SimpleFixes.addBlockRenameFix(builder, fixName, oldId, newId, schema);
			case "entity" -> SimpleFixes.addEntityRenameFix(builder, fixName, oldId, newId, schema);
			case "item" -> {
				SimpleFixes.addItemRenameFix(builder, fixName, oldId, newId, schema);
				DataFixerUtils.addRegistryFixer(new RegistryFixer(new ResourceLocation("item"), List.of(new Fixer(oldId, newId))));
			}
			default -> ConfigurableEverythingUtils.error("Invalid data fix type: " + entry.type(), true);
		}
	}
}
