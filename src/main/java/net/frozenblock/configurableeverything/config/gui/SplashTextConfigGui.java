package net.frozenblock.configurableeverything.config.gui;

import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.configurableeverything.config.SplashTextConfig;
import net.minecraft.world.item.DyeColor;
import java.util.ArrayList;
import java.util.List;
import static net.frozenblock.configurableeverything.util.ConfigurableEverythingUtilsKt.*;

@Environment(EnvType.CLIENT)
public class SplashTextConfigGui {

	public static void setupEntries(ConfigCategory category, ConfigEntryBuilder entryBuilder) {
		var config = SplashTextConfig.get();
		category.setBackground(id("textures/config/splash_text.png"));

		var added = entryBuilder.startStrList(text("added_splashes"), config.addedSplashes)
			.setDefaultValue(new ArrayList<>(List.of()))
			.setSaveConsumer(newValue -> config.addedSplashes = newValue)
			.setTooltip(tooltip("added_splashes"))
			.requireRestart()
			.build();

		var removed = entryBuilder.startStrList(text("removed_splashes"), config.removedSplashes)
			.setDefaultValue(new ArrayList<>(List.of()))
			.setSaveConsumer(newValue -> config.removedSplashes = newValue)
			.setTooltip(tooltip("removed_splashes"))
			.requireRestart()
			.build();

		var splashColor = entryBuilder.startColorField(text("splash_color"), config.splashColor)
			.setDefaultValue(DyeColor.YELLOW.getTextColor())
			.setSaveConsumer(newValue -> config.splashColor = newValue)
			.setTooltip(tooltip("splash_color"))
			.build();

		var removeVanilla = entryBuilder.startBooleanToggle(text("remove_vanilla"), config.removeVanilla)
			.setDefaultValue(false)
			.setSaveConsumer(newValue -> config.removeVanilla = newValue)
			.setTooltip(tooltip("remove_vanilla"))
			.build();

		category.addEntry(added);
		category.addEntry(removed);
		category.addEntry(splashColor);
		category.addEntry(removeVanilla);
	}
}
