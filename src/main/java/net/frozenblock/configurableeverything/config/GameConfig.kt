package net.frozenblock.configurableeverything.config;

import blue.endless.jankson.Comment;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstantsKt;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtilsKt;
import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.instance.json.JsonConfig;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;

public class GameConfig {

	private static final Config<GameConfig> INSTANCE = ConfigRegistry.register(
		new JsonConfig<>(
			ConfigurableEverythingSharedConstantsKt.MOD_ID,
			GameConfig.class,
			ConfigurableEverythingUtilsKt.makeConfigPath("game", true),
			true
		)
	);

	@Comment("Changes the game window title. Change this from an empty string to apply.")
	public String windowTitle = "";

	@Comment(
		"""
		Changes the version series. Change this from an empty string to apply.
		The default version series is "main".
		"""
	)
	public String versionSeries = "";

	public static GameConfig get() {
		return INSTANCE.config();
	}
}
