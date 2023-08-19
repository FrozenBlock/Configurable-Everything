package net.frozenblock.configurableeverything.config;

import blue.endless.jankson.Comment;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstantsKt;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtilsKt;
import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.instance.json.JsonConfig;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;

public class WorldConfig {

	private static final Config<WorldConfig> INSTANCE = ConfigRegistry.register(
		new JsonConfig<>(
			ConfigurableEverythingSharedConstantsKt.MOD_ID,
			WorldConfig.class,
			ConfigurableEverythingUtilsKt.makeConfigPath("world", true),
			true
		)
	);

	@Comment("Does not modify tick rate. Only modifies daytime speed.")
	public long dayTimeSpeedAmplifier = 1;

	public boolean fixSunMoonRotating = false;

	public int sunSize = 300;
	public static float getSunSize() {
		return get().sunSize / 10F;
	}

	public int moonSize = 200;
	public static float getMoonSize() {
		return get().moonSize / 10F;
	}

	public boolean flameBowsLightFire = false;

	@Comment("Disables the experimental warning screen when creating or loading worlds.")
	public boolean disableExperimentalWarning = false;

	public static Config<WorldConfig> getConfigInstance() {
		return INSTANCE;
	}

	public static WorldConfig get() {
		return getConfigInstance().config();
	}
}
