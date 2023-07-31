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

	// the configs may have weird casing because the goal is to match the config file name
	@Comment(
		"""
		Enabled configs
		Warning: It is important to check the contents of each config before enabling them here.
		"""
	)

	public boolean biome = false;

	public boolean biome_placement = false;

	public boolean datafixer = false;

	public boolean entity = false;

	public boolean fluid = false;

	public boolean screen_shake = false;

	public boolean sound = false;

	public boolean splash_text = false;

	public boolean surface_rule = false;

	public static MainConfig get() {
		return INSTANCE.config();
	}
}
