package net.frozenblock.configurableeverything.registry.util

import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.config.RegistryConfig
import org.quiltmc.qsl.frozenblock.core.registry.api.event.RegistryEvents

object RegistryConfigUtil {

    @JvmStatic
    fun init() {
        val config = RegistryConfig.get()
        if (MainConfig.get().registry == true) {
            RegistryEvents.DYNAMIC_REGISTRY_SETUP.register { setupContext ->
                config.biomeAdditions?.value?.let { biomeAdditions ->
                    for (biomeAddition in biomeAdditions) {
                        biomeAddition?.register(setupContext)
                    }
                }
            }
        }
    }
}
