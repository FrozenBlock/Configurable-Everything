package net.frozenblock.configurableeverything.config;

import blue.endless.jankson.Comment;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstants;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtils;
import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.instance.json.JsonConfig;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;

public class ServerConfig {

	private static final Config<ServerConfig> INSTANCE = ConfigRegistry.register(
		new JsonConfig<>(
			ConfigurableEverythingSharedConstants.MOD_ID,
			ServerConfig.class,
			ConfigurableEverythingUtils.makePath("server", true),
			true
		)
	);

	@Comment("Does not modify tick rate. Only modifies daytime speed.")
	public long dayTimeSpeedAmplifier = 3;

	public boolean flameBowsLightFire = false;

	public static ServerConfig get() {
		return INSTANCE.config();
	}
}
