package net.frozenblock.configurableeverything.world.util

import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.config.WorldConfig
import net.frozenblock.lib.config.api.instance.ConfigModification
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.frozenlib_config.FrozenLibConfig

object WorldConfigUtil {

    @JvmStatic
    fun init() {
        val config = WorldConfig.get()
        if (MainConfig.get().world == true && config.disableExperimentalWarning == true)
            ConfigRegistry.register(FrozenLibConfig.INSTANCE, ConfigModification { libConfig ->
                libConfig.removeExperimentalWarning = true
            })
    }
}
