package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.util.CESimpleConfig
import net.frozenblock.configurableeverything.util.CONFIG_FORMAT
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.instance.xjs.XjsConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment

@Suppress("PropertyName", "SpellCheckingInspection")
data class EarlyMainConfig(
    @JvmField
    var game: Boolean = true,
) {
    companion object : CESimpleConfig<EarlyMainConfig>(
        EarlyMainConfig::class,
        "early/main",
        false // horrible idea to support modification of this config
    ) {

        init {
            ConfigRegistry.register(this)
        }

        @JvmStatic
        fun get(): EarlyMainConfig = this.config()
    }
}
