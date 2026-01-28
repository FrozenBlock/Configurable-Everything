package net.frozenblock.configurableeverything.world.util

import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.config.WorldConfig
import net.frozenblock.lib.config.api.instance.ConfigModification
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.frozenlib_config.FrozenLibConfig

object WorldConfigUtil {

    fun init() {
        val config = WorldConfig.get()
        if (!MainConfig.world.get()) return

        FrozenLibConfig.REMOVE_EXPERIMENTAL_WARNING.modify { entry ->
            if (config.disableExperimentalWarning)
                entry.value = true
        }
    }
}
