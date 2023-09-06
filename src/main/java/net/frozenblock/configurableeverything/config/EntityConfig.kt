package net.frozenblock.configurableeverything.config;

import java.util.List;
import net.frozenblock.configurableeverything.entity.util.AttributeAmplifier;
import net.frozenblock.configurableeverything.entity.util.EntityAttributeAmplifier;
import net.frozenblock.configurableeverything.entity.util.EntityFlyBySound;
import net.frozenblock.configurableeverything.entity.util.EntityFlyBySoundData;
import net.frozenblock.configurableeverything.entity.util.EntityHurtEffects;
import net.frozenblock.configurableeverything.entity.util.EntitySpottingIcon;
import net.frozenblock.configurableeverything.entity.util.ExperienceOverride;
import net.frozenblock.configurableeverything.entity.util.MobEffectHolder;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstantsKt;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingConfigUtilsKt;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtilsKt;
import net.frozenblock.lib.config.api.entry.TypedEntry;
import net.frozenblock.lib.config.api.entry.TypedEntryType;
import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.instance.json.JsonConfig;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public final class EntityConfig {

	private static final TypedEntryType<List<EntityAttributeAmplifier>> ENTITY_ATTRIBUTE_AMPLIFIERS = ConfigRegistry.register(
		new TypedEntryType<>(
			ConfigurableEverythingSharedConstantsKt.MOD_ID,
			EntityAttributeAmplifier.CODEC.listOf()
		)
	);

	private static final TypedEntryType<List<EntityFlyBySound>> ENTITY_FLYBY_SOUNDS = ConfigRegistry.register(
		new TypedEntryType<>(
			ConfigurableEverythingSharedConstantsKt.MOD_ID,
			EntityFlyBySound.CODEC.listOf()
		)
	);

	private static final TypedEntryType<List<EntityHurtEffects>> ENTITY_HURT_EFFECTS = ConfigRegistry.register(
			new TypedEntryType<>(
					ConfigurableEverythingSharedConstantsKt.MOD_ID,
					EntityHurtEffects.CODEC.listOf()
			)
	);

	private static final TypedEntryType<List<ExperienceOverride>> EXPERIENCE_OVERRIDES = ConfigRegistry.register(
		new TypedEntryType<>(
			ConfigurableEverythingSharedConstantsKt.MOD_ID,
			ExperienceOverride.CODEC.listOf()
		)
	);

	private static final TypedEntryType<List<EntitySpottingIcon>> SPOTTING_ICONS = ConfigRegistry.register(
		new TypedEntryType<>(
			ConfigurableEverythingSharedConstantsKt.MOD_ID,
			EntitySpottingIcon.CODEC.listOf()
		)
	);

	private static final Config<EntityConfig> INSTANCE = ConfigRegistry.register(
		new JsonConfig<>(
			ConfigurableEverythingSharedConstantsKt.MOD_ID,
			EntityConfig.class,
			ConfigurableEverythingConfigUtilsKt.makeConfigPath("entity"),
			ConfigurableEverythingSharedConstantsKt.CONFIG_JSONTYPE
		)
	);

	public TypedEntry<List<EntityAttributeAmplifier>> entityAttributeAmplifiers = new TypedEntry<>(
		ENTITY_ATTRIBUTE_AMPLIFIERS,
		List.of(
			new EntityAttributeAmplifier(
				ResourceKey.create(Registries.ENTITY_TYPE, new ResourceLocation("example")),
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
				ResourceKey.create(Registries.ENTITY_TYPE, new ResourceLocation("example")),
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
					ConfigurableEverythingUtilsKt.id("flyby.arrow"),
					0.6F,
					1F
				)
			),
			new EntityFlyBySound(
				new ResourceLocation("minecraft:tipped_arrow"),
				new EntityFlyBySoundData(
					"neutral",
					ConfigurableEverythingUtilsKt.id("flyby.arrow"),
					0.6F,
					1F
				)
			),
			new EntityFlyBySound(
				new ResourceLocation("minecraft:spectral"),
				new EntityFlyBySoundData(
					"neutral",
					ConfigurableEverythingUtilsKt.id("flyby.arrow"),
					0.6F,
					1F
				)
			),
			new EntityFlyBySound(
				new ResourceLocation("minecraft:trident"),
				new EntityFlyBySoundData(
					"neutral",
					ConfigurableEverythingUtilsKt.id("flyby.trident"),
					0.7F,
					1F
				)
			),
			new EntityFlyBySound(
				new ResourceLocation("minecraft:egg"),
				new EntityFlyBySoundData(
					"neutral",
					ConfigurableEverythingUtilsKt.id("flyby.egg"),
					0.4F,
					1F
				)
			),
			new EntityFlyBySound(
				new ResourceLocation("minecraft:snowball"),
				new EntityFlyBySoundData(
					"neutral",
					ConfigurableEverythingUtilsKt.id("flyby.snowball"),
					0.4F,
					1F
				)
			),
			//TODO: Burning flyby sound
			new EntityFlyBySound(
				new ResourceLocation("minecraft:fireball"),
				new EntityFlyBySoundData(
					"neutral",
					ConfigurableEverythingUtilsKt.id("flyby.fireball"),
					0.4F,
					1F
				)
			),
			new EntityFlyBySound(
				new ResourceLocation("minecraft:potion"),
				new EntityFlyBySoundData(
					"neutral",
					ConfigurableEverythingUtilsKt.id("flyby.potion"),
					0.2F,
					1F
				)
			),
			new EntityFlyBySound(
				new ResourceLocation("minecraft:experience_bottle"),
				new EntityFlyBySoundData(
					"neutral",
					ConfigurableEverythingUtilsKt.id("flyby.experience_bottle"),
					0.2F,
					1F
				)
			)
		)
	);

	public TypedEntry<List<EntityHurtEffects>> entityHurtEffects = new TypedEntry<>(
			ENTITY_HURT_EFFECTS,
			List.of(
					new EntityHurtEffects(
							new ResourceLocation("cow"),
							"",
							List.of(
									new MobEffectHolder(
											ResourceKey.create(Registries.MOB_EFFECT, new ResourceLocation("speed")),
											5,
											10,
											true,
											true,
											true
									)
							)
					)
			)
	);

	public TypedEntry<List<EntitySpottingIcon>> entitySpottingIcons = new TypedEntry<>(
		SPOTTING_ICONS,
		List.of(
			new EntitySpottingIcon(
				ResourceKey.create(Registries.ENTITY_TYPE, new ResourceLocation("example")),
				ConfigurableEverythingUtilsKt.id("textures/spotting_icon/icon.png"),
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
		public boolean babyZombieSprintParticles = false;
		public boolean zombiesAvoidSun = false;
		public boolean ignoreDoorBreakDifficulty = false;
		public boolean allZombiesBreakDoors = false;
		public boolean ignoreReinforcementDifficulty = false;
		public boolean fullReinforcementChance = false;
	}

	public SkeletonConfig skeleton = new SkeletonConfig();
	public static class SkeletonConfig {
		public boolean skeletonAccuracyIgnoresDifficulty = false;
		public boolean skeletonsAvoidSun = true;
	}

	public boolean flamingArrowsLightFire = false;
}
