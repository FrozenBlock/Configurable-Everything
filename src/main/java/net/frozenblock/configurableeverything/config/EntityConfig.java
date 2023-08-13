package net.frozenblock.configurableeverything.config;

import java.util.List;
import net.frozenblock.configurableeverything.entity.util.AttributeAmplifier;
import net.frozenblock.configurableeverything.entity.util.EntityAttributeAmplifier;
import net.frozenblock.configurableeverything.entity.util.EntityFlyBySound;
import net.frozenblock.configurableeverything.entity.util.EntityFlyBySoundData;
import net.frozenblock.configurableeverything.entity.util.EntitySpottingIcon;
import net.frozenblock.configurableeverything.entity.util.ExperienceOverride;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstants;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtils;
import net.frozenblock.lib.config.api.entry.TypedEntry;
import net.frozenblock.lib.config.api.entry.TypedEntryType;
import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.instance.json.JsonConfig;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public final class EntityConfig {

	public SoundEvent configedSound = SoundEvents.ENDER_DRAGON_GROWL;

	private static final TypedEntryType<List<EntityAttributeAmplifier>> ENTITY_ATTRIBUTE_AMPLIFIERS = ConfigRegistry.register(
		new TypedEntryType<>(
			ConfigurableEverythingSharedConstants.MOD_ID,
			EntityAttributeAmplifier.CODEC.listOf()
		)
	);

	private static final TypedEntryType<List<EntityFlyBySound>> ENTITY_FLYBY_SOUNDS = ConfigRegistry.register(
		new TypedEntryType<>(
			ConfigurableEverythingSharedConstants.MOD_ID,
			EntityFlyBySound.CODEC.listOf()
		)
	);

	private static final TypedEntryType<List<ExperienceOverride>> EXPERIENCE_OVERRIDES = ConfigRegistry.register(
		new TypedEntryType<>(
			ConfigurableEverythingSharedConstants.MOD_ID,
			ExperienceOverride.CODEC.listOf()
		)
	);

	private static final TypedEntryType<List<EntitySpottingIcon>> SPOTTING_ICONS = ConfigRegistry.register(
		new TypedEntryType<>(
			ConfigurableEverythingSharedConstants.MOD_ID,
			EntitySpottingIcon.CODEC.listOf()
		)
	);

	private static final Config<EntityConfig> INSTANCE = ConfigRegistry.register(
		new JsonConfig<>(
			ConfigurableEverythingSharedConstants.MOD_ID,
			EntityConfig.class,
			ConfigurableEverythingUtils.makePath("entity", true),
			true
		)
	);

	public TypedEntry<List<EntityAttributeAmplifier>> entityAttributeAmplifiers = new TypedEntry<>(
		ENTITY_ATTRIBUTE_AMPLIFIERS,
		List.of(
			new EntityAttributeAmplifier(
				ResourceKey.create(Registries.ENTITY_TYPE, new ResourceLocation("player")),
				"",
				List.of(
					new AttributeAmplifier(
						ResourceKey.create(Registries.ATTRIBUTE, new ResourceLocation("generic.movement_speed")),
						1.5
					)
				)
			)
		)
	);

	public TypedEntry<List<ExperienceOverride>> experienceOverrides = new TypedEntry<>(
		EXPERIENCE_OVERRIDES,
		List.of(
			new ExperienceOverride(
				ResourceKey.create(Registries.ENTITY_TYPE, new ResourceLocation("cow")),
				5000
			)
		)
	);

	public TypedEntry<List<EntityFlyBySound>> entityFlyBySounds = new TypedEntry<>(
		ENTITY_FLYBY_SOUNDS,
		List.of(
			new EntityFlyBySound(
				new ResourceLocation("minecraft:arrow"),
				new EntityFlyBySoundData(
					"neutral",
					new ResourceLocation("minecraft:entity.warden.roar"),
					10F,
					1F
				)
			)
		)
	);

	public TypedEntry<List<EntitySpottingIcon>> entitySpottingIcons = new TypedEntry<>(
		SPOTTING_ICONS,
		List.of(
			new EntitySpottingIcon(
				ResourceKey.create(Registries.ENTITY_TYPE, new ResourceLocation("creeper")),
				ConfigurableEverythingUtils.id("textures/spotting_icon/icon.png"),
				5F,
				8F
			)
		)
	);

	public static Config<EntityConfig> getConfigInstance() {
		return INSTANCE;
	}

	public static EntityConfig get() {
		return getConfigInstance().config();
	}

	public PlayerConfig player = new PlayerConfig();

	public static class PlayerConfig {
		public int digSpeedAmplifier = 100;
		public double getDigSpeed() {
			return ((double) this.digSpeedAmplifier) / 100D;
		}
	}

	public ZombieConfig zombie = new ZombieConfig();

	public static class ZombieConfig {
		public boolean babyZombieSprintParticles = true;
		public boolean allZombiesBreakDoors = true;
	}
}
