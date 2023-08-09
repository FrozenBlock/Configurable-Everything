package net.frozenblock.configurableeverything.config.gui;

import java.util.stream.Collectors;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.configurableeverything.config.EntityConfig;
import net.frozenblock.configurableeverything.config.additions.DropdownBoxEntries;
import net.frozenblock.configurableeverything.config.gui.main.ConfigurableEverythingConfigGui;
import net.frozenblock.lib.config.clothconfig.FrozenClothConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

@Environment(EnvType.CLIENT)
public final class EntityConfigGui {

	public static void setupEntries(ConfigCategory category, ConfigEntryBuilder entryBuilder) {
		var config = EntityConfig.get();

		var configedSound = entryBuilder.startDropdownMenu(
				ConfigurableEverythingConfigGui.text("screen_shake"),
				DropdownBoxEntries.ofSoundEventObject(config.configedSound),
				DropdownBoxEntries.ofSoundEvent()
			)
			.setDefaultValue(SoundEvents.ENDER_DRAGON_GROWL)
			.setSelections(BuiltInRegistries.SOUND_EVENT.stream().collect(Collectors.toSet()))
			.setSaveConsumer(soundEvent -> config.configedSound = soundEvent)
			.build();

		category.addEntry(configedSound);

		var player = config.player;
		var digSpeedAmplifier = entryBuilder.startIntSlider(ConfigurableEverythingConfigGui.text("dig_speed_amplifier"), player.digSpeedAmplifier, 1, 5000)
			.setDefaultValue(100)
			.setSaveConsumer(newValue -> player.digSpeedAmplifier = newValue)
			.setTooltip(ConfigurableEverythingConfigGui.tooltip("dig_speed_amplifier"))
			.build();

		var playerCategory = FrozenClothConfig.createSubCategory(entryBuilder, category, ConfigurableEverythingConfigGui.text("player"),
			false,
			ConfigurableEverythingConfigGui.tooltip("player"),
			digSpeedAmplifier
		);

		var zombie = config.zombie;
		var babyZombieSprint = entryBuilder.startBooleanToggle(ConfigurableEverythingConfigGui.text("baby_zombie_sprint_particles"), zombie.babyZombieSprintParticles)
			.setDefaultValue(true)
			.setSaveConsumer(newValue -> zombie.babyZombieSprintParticles = newValue)
			.setTooltip(ConfigurableEverythingConfigGui.tooltip("baby_zombie_sprint_particles"))
			.setYesNoTextSupplier(bool -> ConfigurableEverythingConfigGui.text(String.valueOf(bool)))
			.build();
		var allZombiesBreakDoors = entryBuilder.startBooleanToggle(ConfigurableEverythingConfigGui.text("all_zombies_break_doors"), zombie.allZombiesBreakDoors)
			.setDefaultValue(true)
			.setSaveConsumer(newValue -> zombie.allZombiesBreakDoors = newValue)
			.setTooltip(ConfigurableEverythingConfigGui.tooltip("all_zombies_break_doors"))
			.setYesNoTextSupplier(bool -> ConfigurableEverythingConfigGui.text(String.valueOf(bool)))
			.build();

		var zombieCategory = FrozenClothConfig.createSubCategory(entryBuilder, category, ConfigurableEverythingConfigGui.text("zombie"),
			false,
			ConfigurableEverythingConfigGui.tooltip("zombie"),
			allZombiesBreakDoors, babyZombieSprint
		);

		category.setBackground(ConfigurableEverythingConfigGui.id("textures/config/entity.png"));
	}

}
