package net.frozenblock.configurableeverything.config.gui.main;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.configurableeverything.config.EntityConfig;
import net.frozenblock.configurableeverything.config.gui.EntityConfigGui;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstants;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public final class ConfigurableEverythingConfigGui {

	public static Screen buildScreen(Screen parent) {
		var configBuilder = ConfigBuilder.create().setParentScreen(parent).setTitle(ConfigurableEverythingConfigGui.text("component.title"));
		configBuilder.setSavingRunnable(() -> {
			EntityConfig.getConfigInstance().save();
		});
		var entity = configBuilder.getOrCreateCategory(ConfigurableEverythingConfigGui.text("entity"));
		ConfigEntryBuilder entryBuilder = configBuilder.entryBuilder();
		EntityConfigGui.setupEntries(entity, entryBuilder);
		return configBuilder.build();
	}

	public static ResourceLocation id(String key) {
		return new ResourceLocation(ConfigurableEverythingSharedConstants.MOD_ID, key);
	}

	public static Component text(String key) {
		return Component.translatable("option." + ConfigurableEverythingSharedConstants.MOD_ID + "." + key);
	}

	public static Component tooltip(String key) {
		return Component.translatable("tooltip." + ConfigurableEverythingSharedConstants.MOD_ID + "." + key);
	}
}
