package net.frozenblock.configurableeverything.splash_text.util

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.config.SplashTextConfig
import net.frozenblock.configurableeverything.util.UNSTABLE_LOGGING
import net.frozenblock.configurableeverything.util.log
import net.frozenblock.lib.menu.api.SplashTextEvents
import net.mehvahdjukaar.candlelight.api.ClientOnly

@ClientOnly
object SplashTextConfigUtil {

    fun init() = runBlocking {
        if (!MainConfig.splash_text.get()) return@runBlocking

        for (string in SplashTextConfig.addedSplashes.get()) {
            launch {
                SplashTextEvents.ADD.register { it.add(string) }
                log("Added $string to splash texts.", UNSTABLE_LOGGING)
            }
        }
        for (string in SplashTextConfig.removedSplashes.get()) {
            launch {
                SplashTextEvents.REMOVE.register { it.add(string) }
                log("Removed $string from splash texts.", UNSTABLE_LOGGING)
            }
        }
    }
}
