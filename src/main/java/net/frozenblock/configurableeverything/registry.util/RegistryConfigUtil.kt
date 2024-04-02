package net.frozenblock.configurableeverything.registry.util

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.config.RegistryConfig
import net.frozenblock.configurableeverything.util.value
import org.quiltmc.qsl.frozenblock.core.registry.api.event.RegistryEvents

internal object RegistryConfigUtil {

    internal fun init() {
        val config = RegistryConfig.get()
        if (!MainConfig.get().registry) return

        RegistryEvents.DYNAMIC_REGISTRY_SETUP.register { setupContext ->
            runBlocking {
                launch {
                    config.placedFeatureAdditions?.value?.apply {
                        for (placedFeatureAddition in this) {
                            placedFeatureAddition?.register(setupContext)
                        }
                    }
                }

                launch {
                    config.biomeAdditions?.value?.apply {
                        for (biomeAddition in this) {
                            biomeAddition?.register(setupContext)
                        }
                    }
                }
            }
        }
    }
}
