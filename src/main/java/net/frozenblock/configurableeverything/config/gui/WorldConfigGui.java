package net.frozenblock.configurableeverything.config.gui;

import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.configurableeverything.config.EntityConfig;
import net.frozenblock.configurableeverything.config.WorldConfig;
import net.frozenblock.configurableeverything.config.gui.main.ConfigurableEverythingConfigGui;
import net.frozenblock.lib.config.clothconfig.FrozenClothConfig;

@Environment(EnvType.CLIENT)
public final class WorldConfigGui {

	public static void setupEntries(ConfigCategory category, ConfigEntryBuilder entryBuilder) {
		var config = WorldConfig.get();

		category.addEntry(
			entryBuilder.startLongSlider(ConfigurableEverythingConfigGui.text("day_time_speed"), config.dayTimeSpeedAmplifier, 1L, 5000L)
				.setDefaultValue(1L)
				.setSaveConsumer(newValue -> config.dayTimeSpeedAmplifier = newValue)
				.setTooltip(ConfigurableEverythingConfigGui.tooltip("day_time_speed"))
				.build()
		);

		category.addEntry(
			entryBuilder.startIntSlider(ConfigurableEverythingConfigGui.text("sun_size"), config.sunSize, 1, 10000)
				.setDefaultValue(300)
				.setSaveConsumer(newValue -> config.sunSize = newValue)
				.setTooltip(ConfigurableEverythingConfigGui.tooltip("sun_size"))
				.build()
		);
		category.addEntry(
			entryBuilder.startIntSlider(ConfigurableEverythingConfigGui.text("moon_size"), config.moonSize, 1, 10000)
				.setDefaultValue(200)
				.setSaveConsumer(newValue -> config.moonSize = newValue)
				.setTooltip(ConfigurableEverythingConfigGui.tooltip("moon_size"))
				.build()
		);

		category.setBackground(ConfigurableEverythingConfigGui.id("textures/config/world.png"));
	}

}
