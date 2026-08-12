package net.frozenblock.configurableeverything

import net.fabricmc.api.ModInitializer

/**
 * Mod initializer for Configurable Everything.
 */
class ConfigurableEverythingFabric : ModInitializer {

    override fun onInitialize() {
        ConfigurableEverything.init()
        ConfigurableEverything.setup()
    }

}
