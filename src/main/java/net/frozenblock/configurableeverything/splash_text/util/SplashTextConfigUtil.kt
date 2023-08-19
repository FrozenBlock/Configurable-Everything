package net.frozenblock.configurableeverything.splash_text.util;

import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.config.SplashTextConfig;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstants;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtilsKt;
import net.frozenblock.lib.menu.api.SplashTextAPI;

public final class SplashTextConfigUtil {

	public static void init() {
		var config = SplashTextConfig.get();
		if (MainConfig.get().splash_text) {
			var added = config.addedSplashes;
			var removed = config.removedSplashes;

			if (ConfigurableEverythingSharedConstants.UNSTABLE_LOGGING) {
				for (var string : added) {
					ConfigurableEverythingUtilsKt.log("Added '" + string + "' to splash texts.", true);
				}
				for (var string : removed) {
					ConfigurableEverythingUtilsKt.log("Removed '" + string + "' from splash texts.", true);
				}
			}

			for (var string : added) {
				SplashTextAPI.add(string);
			}

			for (var string : removed) {
				SplashTextAPI.remove(string);
			}
		}
	}
}
