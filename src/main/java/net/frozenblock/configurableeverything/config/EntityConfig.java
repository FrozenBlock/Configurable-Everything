package net.frozenblock.configurableeverything.config;

import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstants;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtils;
import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.instance.json.JsonConfig;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;

public final class EntityConfig {

	private static final Config<EntityConfig> INSTANCE = ConfigRegistry.register(
		new JsonConfig<>(
			ConfigurableEverythingSharedConstants.MOD_ID,
			EntityConfig.class,
			ConfigurableEverythingUtils.makePath("entity", true),
			true
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
		public boolean babyZombieSprint = true;
		public boolean allZombiesBreakDoors = true;
	}
}
