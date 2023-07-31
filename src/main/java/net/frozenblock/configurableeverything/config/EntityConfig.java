package net.frozenblock.configurableeverything.config;

import net.frozenblock.configurableeverything.entity.util.EntityFlyBySound;
import net.frozenblock.configurableeverything.entity.util.EntityFlyBySoundData;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstants;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtils;
import net.frozenblock.lib.config.api.entry.TypedEntry;
import net.frozenblock.lib.config.api.entry.TypedEntryType;
import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.instance.json.JsonConfig;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;
import net.minecraft.resources.ResourceLocation;
import java.util.List;

public final class EntityConfig {

	private static final TypedEntryType<List<EntityFlyBySound>> ENTITY_FLYBY_SOUNDS = ConfigRegistry.register(
		new TypedEntryType<>(
			ConfigurableEverythingSharedConstants.MOD_ID,
			EntityFlyBySound.CODEC.listOf()
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

	public ZombieConfig zombie = new ZombieConfig();

	public static EntityConfig get() {
		return INSTANCE.config();
	}

	public static Config<EntityConfig> getConfigInstance() {
		return INSTANCE;
	}

	public static class ZombieConfig {
		public boolean babyZombieSprintParticles = true;
		public boolean allZombiesBreakDoors = true;
	}
}
