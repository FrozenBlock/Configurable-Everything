package net.frozenblock.configurableeverything.config.gui;

import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.configurableeverything.config.WorldConfig;

import static net.frozenblock.configurableeverything.util.ConfigurableEverythingConfigUtilsKt.*;
import static net.frozenblock.configurableeverything.util.ConfigurableEverythingUtilsKt.*;

@Environment(EnvType.CLIENT)
public final class WorldConfigGui {

	public static void setupEntries(ConfigCategory category, ConfigEntryBuilder entryBuilder) {
		var config = WorldConfig.get();
		category.setBackground(id("textures/config/world.png"));

		category.addEntry(
			entryBuilder.startLongSlider(text("day_time_speed"), config.dayTimeSpeedAmplifier, 1L, 100L)
				.setDefaultValue(1L)
				.setSaveConsumer(newValue -> config.dayTimeSpeedAmplifier = newValue)
				.setTooltip(tooltip("day_time_speed"))
				.build()
		);

		category.addEntry(
			entryBuilder.startBooleanToggle(text("fix_sun_moon_rotating"), config.fixSunMoonRotating)
				.setDefaultValue(false)
				.setSaveConsumer(newValue -> config.fixSunMoonRotating = newValue)
				.setTooltip(tooltip("fix_sun_moon_rotating"))
				.build()
		);

		category.addEntry(
			entryBuilder.startIntSlider(text("sun_size"), config.sunSize, 10, 1000)
				.setDefaultValue(300)
				.setSaveConsumer(newValue -> config.sunSize = newValue)
				.setTooltip(tooltip("sun_size"))
				.build()
		);
		category.addEntry(
			entryBuilder.startIntSlider(text("moon_size"), config.moonSize, 10, 1000)
				.setDefaultValue(200)
				.setSaveConsumer(newValue -> config.moonSize = newValue)
				.setTooltip(tooltip("moon_size"))
				.build()
		);

		category.addEntry(
			entryBuilder.startBooleanToggle(text("disable_experimental_warning"), config.disableExperimentalWarning)
				.setDefaultValue(false)
				.setSaveConsumer(newValue -> config.disableExperimentalWarning = newValue)
				.setTooltip(tooltip("disable_experimental_warning"))
				.setYesNoTextSupplier(bool -> text(String.valueOf(bool)))
				.build()
		);
	}

}
