package net.frozenblock.configurableeverything.registry.util

import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.config.RegistryConfig

object RegistryConfigUtil {

    @JvmStatic
    fun init() {
        val config = RegistryConfig.get()
        if (MainConfig.registry == true) {
        }
    }
}