package net.frozenblock.configurableeverything.config;

import blue.endless.jankson.Comment;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstants;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtils;
import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.instance.json.JsonConfig;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;

public class MainConfig {

	private static final Config<MainConfig> INSTANCE = ConfigRegistry.register(
		new JsonConfig<>(
			ConfigurableEverythingSharedConstants.MOD_ID,
			MainConfig.class,
			ConfigurableEverythingUtils.makePath("main", true),
			true
		)
	);

	@Comment("Enabled features")

	public boolean biome = true;

	public boolean biomePlacement = true;

	public boolean entity = true;

	public boolean fluid = true;

	public boolean screenShake = false;

	public boolean surfaceRule = true;

	public static MainConfig get() {
		return INSTANCE.config();
	}
}
