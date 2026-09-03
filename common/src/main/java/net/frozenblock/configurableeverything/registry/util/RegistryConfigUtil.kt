package net.frozenblock.configurableeverything.registry.util

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.config.RegistryConfig
import net.frozenblock.configurableeverything.util.value
import org.quiltmc.qsl.frozenblock.core.registry.api.event.RegistryEvents

internal object RegistryConfigUtil {

    internal fun init() {
        if (!MainConfig.registry.get()) return

        RegistryEvents.DYNAMIC_REGISTRY_SETUP.register { setupContext ->
            runBlocking {
                launch {
                    RegistryConfig.placedFeatureAdditions.get().apply {
                        for (placedFeatureAddition in this) {
                            placedFeatureAddition.register(setupContext)
                        }
                    }
                }

                launch {
                    RegistryConfig.biomeAdditions.get().apply {
                        for (biomeAddition in this) {
                            biomeAddition.register(setupContext)
                        }
                    }
                }
            }
        }
    }
}
