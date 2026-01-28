package net.frozenblock.configurableeverything.config.thirdparty

import net.frozenblock.configurableeverything.util.*
import net.frozenblock.lib.config.api.instance.xjs.XjsConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment

// Unsyncable
data class SodiumConfig(
    @JvmField
    // UNSYNCABLE
    @Comment("Whether or not to disable Sodium's resource pack compatibility scanner.")
    var disableResourcePackScanner: Boolean = false,
) {

    companion object : CESimpleConfig<SodiumConfig>(
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
