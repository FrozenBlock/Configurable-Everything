package net.frozenblock.configurableeverything.config;

import blue.endless.jankson.Comment;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstants;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtils;
import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.instance.json.JsonConfig;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;
import net.minecraft.world.item.DyeColor;
import java.util.ArrayList;
import java.util.List;

public class SplashTextConfig {

	private static final Config<SplashTextConfig> INSTANCE = ConfigRegistry.register(
		new JsonConfig<>(
			ConfigurableEverythingSharedConstants.MOD_ID,
			SplashTextConfig.class,
			ConfigurableEverythingUtils.makePath("splash_text", true),
			true
		)
	);

	public List<String> addedSplashes = new ArrayList<>();

	public List<String> removedSplashes = new ArrayList<>();

	public int splashColor = DyeColor.YELLOW.getTextColor();

	@Comment("Removes all vanilla splashes.")
	public boolean removeVanilla = true;

	public static SplashTextConfig get() {
		return INSTANCE.config();
	}
}
