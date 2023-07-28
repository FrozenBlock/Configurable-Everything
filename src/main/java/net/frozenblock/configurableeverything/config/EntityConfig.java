package net.frozenblock.configurableeverything.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.CollapsibleObject;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstants;
import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.instance.json.JsonConfig;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;
import net.lunade.mcfixes.MCFixesMain;

public final class EntityConfig {

	//TODO: Port to Lib config instead of Cloth

	private static final Config<EntityConfig> INSTANCE = ConfigRegistry.register(
		new JsonConfig<>(
			ConfigurableEverythingSharedConstants.MOD_ID,
			EntityConfig.class,
			ConfigurableEverything.configPath("entity", true),
			true
		)
	);

	@CollapsibleObject
	public final ZombieConfig zombie = new ZombieConfig();

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
