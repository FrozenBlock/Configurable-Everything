package net.frozenblock.configurableeverything.config;

import blue.endless.jankson.Comment;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstantsKt;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtilsKt;
import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.instance.json.JsonConfig;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;

public class MainConfig {

	private static final Config<MainConfig> INSTANCE = ConfigRegistry.register(
		new JsonConfig<>(
			ConfigurableEverythingSharedConstantsKt.MOD_ID,
			MainConfig.class,
			ConfigurableEverythingUtilsKt.makeConfigPath("main", true),
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

	public boolean entity = true;

	public boolean fluid = false;

	public boolean screen_shake = false;

	public boolean splash_text = false;

	public boolean surface_rule = false;

	public boolean world = false;

	@Comment("Datapack features will not apply unless the main toggle and datapack toggle is set to true.")
	public DatapackConfig datapack = new DatapackConfig();

	public static MainConfig get() {
		return INSTANCE.config();
	}

	public static Config<MainConfig> getConfigInstance() {
		return INSTANCE;
	}

	public static class DatapackConfig {
		public boolean applyDatapacksFolder = true;

		public boolean biome = true;

		public boolean biome_placement = true;
	}
}
