package net.frozenblock.configurableeverything.splash_text.util

import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.config.SplashTextConfig
import net.frozenblock.configurableeverything.util.UNSTABLE_LOGGING
import net.frozenblock.configurableeverything.util.log
import net.frozenblock.lib.menu.api.SplashTextAPI

object SplashTextConfigUtil {

    @JvmStatic
    fun init() {
        val config = SplashTextConfig.get()
        if (MainConfig.get().splash_text) {
            val added = config.addedSplashes
            val removed = config.removedSplashes

            for (string in added) {
                SplashTextAPI.add(string)
                log("Added $string to splash texts.", UNSTABLE_LOGGING)
            }
            for (string in removed) {
                SplashTextAPI.remove(string)
                log("Removed $string from splash texts.", UNSTABLE_LOGGING)
            }
        }
    }
}
