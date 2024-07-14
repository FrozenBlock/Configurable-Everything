package net.frozenblock.configurableeverything.config.thirdparty

import net.frozenblock.configurableeverything.util.*
import net.frozenblock.lib.config.api.instance.xjs.XjsConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.annotation.UnsyncableConfig
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment

@UnsyncableConfig
data class SodiumConfig(
    @JvmField
    @Comment("Whether or not to disable Sodium's resource pack compatibility scanner.")
    var disableResourcePackScanner: Boolean = false,
) {

    companion object : CEConfig<SodiumConfig>(
        SodiumConfig::class,
        "sodium",
        false,
        true
    ) {
        init {
            ConfigRegistry.register(this)
        }

        @JvmStatic
        fun get(): SodiumConfig = this.instance()
    }

}
