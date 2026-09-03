package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.util.CESimpleConfig
import net.frozenblock.lib.config.v1.registry.BasicConfigRegistry

data class EarlyMainConfig(
    @JvmField
    var game: Boolean = false,
) {
    companion object : CESimpleConfig<EarlyMainConfig>(
        EarlyMainConfig::class,
        "early/main",
        false // horrible idea to support modification of this config
    ) {

        init {
            BasicConfigRegistry.register(this)
        }

        @JvmStatic
        fun get(): EarlyMainConfig = this.config()
    }
}
