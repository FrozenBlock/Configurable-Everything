package net.frozenblock.configurableeverything.splash_text.util

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
        if (!MainConfig.splash_text.get()) return@runBlocking

        for (string in SplashTextConfig.addedSplashes.get()) {
            launch {
                SplashTextAPI.add(string)
                log("Added $string to splash texts.", UNSTABLE_LOGGING)
            }
        }
        for (string in SplashTextConfig.removedSplashes.get()) {
            launch {
                SplashTextAPI.remove(string)
                log("Removed $string from splash texts.", UNSTABLE_LOGGING)
            }
        }
    }
}
