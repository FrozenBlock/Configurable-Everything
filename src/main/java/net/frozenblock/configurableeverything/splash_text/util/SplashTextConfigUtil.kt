package net.frozenblock.configurableeverything.splash_text.util

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.launch
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.config.SplashTextConfig
import net.frozenblock.configurableeverything.util.UNSTABLE_LOGGING
import net.frozenblock.configurableeverything.util.log
import net.frozenblock.lib.menu.api.SplashTextAPI

@Environment(EnvType.CLIENT)
object SplashTextConfigUtil {

    fun init() = runBlocking {
        val config = SplashTextConfig.get()
        if (MainConfig.get().splash_text != true) return@runBlocking
        val added = config.addedSplashes
        val removed = config.removedSplashes

        if (added != null) {
            for (string in added) {
                launch {
                    SplashTextAPI.add(string)
                    log("Added $string to splash texts.", UNSTABLE_LOGGING)
                }
            }
        }
        if (removed != null) {
            for (string in removed) {
                launch {
                    SplashTextAPI.remove(string)
                    log("Removed $string from splash texts.", UNSTABLE_LOGGING)
                }
            }
        }
    }
}
