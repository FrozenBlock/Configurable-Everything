package net.frozenblock.configurableeverything.world.util

import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.config.WorldConfig
import net.frozenblock.lib.config.frozenlib_config.FrozenLibConfigOverrides

object WorldConfigUtil {

    @JvmStatic
    fun init() {
        val config = WorldConfig.get()
        if (MainConfig.get().world == true && config.disableExperimentalWarning)
            FrozenLibConfigOverrides.removeExperimentalWarning = true
    }
}
