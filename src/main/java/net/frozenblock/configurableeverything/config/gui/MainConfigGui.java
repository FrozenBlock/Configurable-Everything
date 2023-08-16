package net.frozenblock.configurableeverything.config.gui;

import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.lib.config.clothconfig.FrozenClothConfig;
import net.minecraft.network.chat.Component;

import static net.frozenblock.configurableeverything.config.gui.main.ConfigurableEverythingConfigGui.*;

@Environment(EnvType.CLIENT)
public class MainConfigGui {

	public static void setupEntries(ConfigCategory category, ConfigEntryBuilder entryBuilder) {
		var config = MainConfig.get();
		category.setBackground(id("textures/config/main.png"));

		category.addEntry(entryBuilder.startBooleanToggle(text("biome"), config.biome)
				.setDefaultValue(false)
				.setSaveConsumer(newValue -> config.biome = newValue)
				.setTooltip(tooltip("biome"))
				.setYesNoTextSupplier(bool -> text(String.valueOf(bool)))
			.build()
		);

		category.addEntry(entryBuilder.startBooleanToggle(text("biome_placement"), config.biome_placement)
			.setDefaultValue(false)
			.setSaveConsumer(newValue -> config.biome_placement = newValue)
			.setTooltip(tooltip("biome_placement"))
			.setYesNoTextSupplier(bool -> text(String.valueOf(bool)))
			.build()
		);

		category.addEntry(entryBuilder.startBooleanToggle(text("datafixer"), config.datafixer)
			.setDefaultValue(false)
			.setSaveConsumer(newValue -> config.datafixer = newValue)
			.setTooltip(tooltip("datafixer"))
			.setYesNoTextSupplier(bool -> text(String.valueOf(bool)))
			.build()
		);

		category.addEntry(entryBuilder.startBooleanToggle(text("entity"), config.entity)
			.setDefaultValue(false)
			.setSaveConsumer(newValue -> config.entity = newValue)
			.setTooltip(tooltip("entity"))
			.setYesNoTextSupplier(bool -> text(String.valueOf(bool)))
			.build()
		);

		category.addEntry(entryBuilder.startBooleanToggle(text("fluid"), config.fluid)
			.setDefaultValue(false)
			.setSaveConsumer(newValue -> config.fluid = newValue)
			.setTooltip(tooltip("fluid"))
			.setYesNoTextSupplier(bool -> text(String.valueOf(bool)))
			.build()
		);

		category.addEntry(entryBuilder.startBooleanToggle(text("screen_shake"), config.screen_shake)
			.setDefaultValue(false)
			.setSaveConsumer(newValue -> config.screen_shake = newValue)
			.setTooltip(tooltip("screen_shake"))
			.setYesNoTextSupplier(bool -> text(String.valueOf(bool)))
			.build()
		);

		category.addEntry(entryBuilder.startBooleanToggle(text("splash_text"), config.splash_text)
			.setDefaultValue(false)
			.setSaveConsumer(newValue -> config.splash_text = newValue)
			.setTooltip(tooltip("splash_text"))
			.setYesNoTextSupplier(bool -> text(String.valueOf(bool)))
			.build()
		);

		category.addEntry(entryBuilder.startBooleanToggle(text("surface_rule"), config.surface_rule)
			.setDefaultValue(false)
			.setSaveConsumer(newValue -> config.surface_rule = newValue)
			.setTooltip(tooltip("surface_rule"))
			.setYesNoTextSupplier(bool -> text(String.valueOf(bool)))
			.build()
		);

		category.addEntry(entryBuilder.startBooleanToggle(text("world"), config.world)
			.setDefaultValue(false)
			.setSaveConsumer(newValue -> config.world = newValue)
			.setTooltip(tooltip("world"))
			.setYesNoTextSupplier(bool -> text(String.valueOf(bool)))
			.build()
		);

		var applyDatapacksFolder = entryBuilder.startBooleanToggle(text("apply_datapacks_folder"), config.datapack.applyDatapacksFolder)
			.setDefaultValue(true)
			.setSaveConsumer(newValue -> config.datapack.applyDatapacksFolder = newValue)
			.setTooltip(tooltip("apply_datapacks_folder"))
			.setYesNoTextSupplier(bool -> text(String.valueOf(bool)))
			.build();

		var datapackBiome = entryBuilder.startBooleanToggle(text("datapack_biome"), config.datapack.biome)
			.setDefaultValue(true)
			.setSaveConsumer(newValue -> config.datapack.biome = newValue)
			.setTooltip(tooltip("datapack_biome"))
			.setYesNoTextSupplier(bool -> text(String.valueOf(bool)))
			.build();

		var datapackBiomePlacement = entryBuilder.startBooleanToggle(text("datapack_biome_placement"), config.datapack.biome_placement)
			.setDefaultValue(true)
			.setSaveConsumer(newValue -> config.datapack.biome_placement = newValue)
			.setTooltip(tooltip("datapack_biome_placement"))
			.setYesNoTextSupplier(bool -> text(String.valueOf(bool)))
			.build();

		FrozenClothConfig.createSubCategory(
			entryBuilder, category, text("datapack"), false, tooltip("datapack"),
			applyDatapacksFolder, datapackBiome, datapackBiomePlacement
		);
	}
}
