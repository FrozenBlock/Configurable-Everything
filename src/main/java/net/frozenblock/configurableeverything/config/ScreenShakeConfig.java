package net.frozenblock.configurableeverything.config;

import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstants;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtils;
import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.instance.json.JsonConfig;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;

public final class ScreenShakeConfig {

	private static final Config<ScreenShakeConfig> INSTANCE = ConfigRegistry.register(
		new JsonConfig<>(
			ConfigurableEverythingSharedConstants.MOD_ID,
			ScreenShakeConfig.class,
			ConfigurableEverythingUtils.makePath("screen_shake", true),
			true
		)
	);

	public boolean explosionScreenShake = true;

	public static ScreenShakeConfig get() {
		return INSTANCE.config();
	}

	public static Config<ScreenShakeConfig> getConfigInstance() {
		return INSTANCE;
	}

}
