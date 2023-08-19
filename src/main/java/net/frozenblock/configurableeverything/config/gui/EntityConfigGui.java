package net.frozenblock.configurableeverything.config.gui;

import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.configurableeverything.config.EntityConfig;
import net.frozenblock.lib.config.clothconfig.FrozenClothConfig;

import static net.frozenblock.configurableeverything.util.ConfigurableEverythingUtilsKt.*;

@Environment(EnvType.CLIENT)
public final class EntityConfigGui {

	public static void setupEntries(ConfigCategory category, ConfigEntryBuilder entryBuilder) {
		var config = EntityConfig.get();
		category.setBackground(id("textures/config/entity.png"));

		var player = config.player;
		var digSpeedAmplifier = entryBuilder.startIntSlider(text("dig_speed_amplifier"), player.digSpeedAmplifier, 1, 5000)
			.setDefaultValue(100)
			.setSaveConsumer(newValue -> player.digSpeedAmplifier = newValue)
			.setTooltip(tooltip("dig_speed_amplifier"))
			.build();

		var playerCategory = FrozenClothConfig.createSubCategory(entryBuilder, category, text("player"),
			false,
			tooltip("player"),
			digSpeedAmplifier
		);

		var zombie = config.zombie;
		var babyZombieSprint = entryBuilder.startBooleanToggle(text("baby_zombie_sprint_particles"), zombie.babyZombieSprintParticles)
			.setDefaultValue(true)
			.setSaveConsumer(newValue -> zombie.babyZombieSprintParticles = newValue)
			.setTooltip(tooltip("baby_zombie_sprint_particles"))
			.setYesNoTextSupplier(bool -> text(String.valueOf(bool)))
			.build();
		var allZombiesBreakDoors = entryBuilder.startBooleanToggle(text("all_zombies_break_doors"), zombie.allZombiesBreakDoors)
			.setDefaultValue(true)
			.setSaveConsumer(newValue -> zombie.allZombiesBreakDoors = newValue)
			.setTooltip(tooltip("all_zombies_break_doors"))
			.setYesNoTextSupplier(bool -> text(String.valueOf(bool)))
			.build();

		var zombieCategory = FrozenClothConfig.createSubCategory(entryBuilder, category, text("zombie"),
			false,
			tooltip("zombie"),
			allZombiesBreakDoors, babyZombieSprint
		);
	}

}
