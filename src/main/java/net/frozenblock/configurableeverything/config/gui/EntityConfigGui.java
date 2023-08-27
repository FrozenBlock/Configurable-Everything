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
			.setDefaultValue(false)
			.setSaveConsumer(newValue -> zombie.babyZombieSprintParticles = newValue)
			.setTooltip(tooltip("baby_zombie_sprint_particles"))
			.setYesNoTextSupplier(bool -> text(String.valueOf(bool)))
			.build();
		var zombiesAvoidSun = entryBuilder.startBooleanToggle(text("zombies_avoid_sun"), zombie.zombiesAvoidSun)
			.setDefaultValue(false)
			.setSaveConsumer(newValue -> zombie.zombiesAvoidSun = newValue)
			.setTooltip(tooltip("zombies_avoid_sun"))
			.setYesNoTextSupplier(bool -> text(String.valueOf(bool)))
			.build();
		var ignoreZombieDoorBreakDifficulty = entryBuilder.startBooleanToggle(text("ignore_zombie_door_break_difficulty"), zombie.ignoreDoorBreakDifficulty)
			.setDefaultValue(false)
			.setSaveConsumer(newValue -> zombie.ignoreDoorBreakDifficulty = newValue)
			.setTooltip(tooltip("ignore_zombie_door_break_difficulty"))
			.setYesNoTextSupplier(bool -> text(String.valueOf(bool)))
			.build();
		var allZombiesBreakDoors = entryBuilder.startBooleanToggle(text("all_zombies_break_doors"), zombie.allZombiesBreakDoors)
			.setDefaultValue(false)
			.setSaveConsumer(newValue -> zombie.allZombiesBreakDoors = newValue)
			.setTooltip(tooltip("all_zombies_break_doors"))
			.setYesNoTextSupplier(bool -> text(String.valueOf(bool)))
			.build();
		var ignoreZombieReinforcementDifficulty = entryBuilder.startBooleanToggle(text("ignore_zombie_reinforcement_difficulty"), zombie.ignoreReinforcementDifficulty)
			.setDefaultValue(false)
			.setSaveConsumer(newValue -> zombie.ignoreReinforcementDifficulty = newValue)
			.setTooltip(tooltip("ignore_zombie_reinforcement_difficulty"))
			.setYesNoTextSupplier(bool -> text(String.valueOf(bool)))
			.build();
		var fullZombieReinforcementChance = entryBuilder.startBooleanToggle(text("full_zombie_reinforcement_chance"), zombie.fullReinforcementChance)
			.setDefaultValue(false)
			.setSaveConsumer(newValue -> zombie.fullReinforcementChance = newValue)
			.setTooltip(tooltip("full_zombie_reinforcement_chance"))
			.setYesNoTextSupplier(bool -> text(String.valueOf(bool)))
			.build();

		var zombieCategory = FrozenClothConfig.createSubCategory(entryBuilder, category, text("zombie"),
			false,
			tooltip("zombie"),
			babyZombieSprint, zombiesAvoidSun, ignoreZombieDoorBreakDifficulty, allZombiesBreakDoors, ignoreZombieReinforcementDifficulty, fullZombieReinforcementChance
		);

		var skeleton = config.skeleton;
		var skeletonAccuracyIgnoresDifficulty = entryBuilder.startBooleanToggle(text("skeleton_accuracy_ignores_difficulty"), skeleton.skeletonAccuracyIgnoresDifficulty)
			.setDefaultValue(false)
			.setSaveConsumer(newValue -> skeleton.skeletonAccuracyIgnoresDifficulty = newValue)
			.setTooltip(tooltip("skeleton_accuracy_ignores_difficulty"))
			.setYesNoTextSupplier(bool -> text(String.valueOf(bool)))
			.build();

		var skeletonCategory = FrozenClothConfig.createSubCategory(entryBuilder, category, text("skeleton"),
			false,
			tooltip("skeleton"),
			skeletonAccuracyIgnoresDifficulty
		);

		var flamingArrowsLightFire = category.addEntry(
			entryBuilder.startBooleanToggle(text("flaming_arrows_light_fire"), config.flamingArrowsLightFire)
			.setDefaultValue(false)
			.setSaveConsumer(newValue -> config.flamingArrowsLightFire = newValue)
			.setTooltip(tooltip("flaming_arrows_light_fire"))
			.setYesNoTextSupplier(bool -> text(String.valueOf(bool)))
			.build()
		);

	}

}
