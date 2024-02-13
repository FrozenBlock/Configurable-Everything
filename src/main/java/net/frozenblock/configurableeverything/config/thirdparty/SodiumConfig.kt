package net.frozenblock.configurableeverything.config.thirdparty

import net.frozenblock.configurableeverything.util.*
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.annotation.EntrySyncData
import net.frozenblock.lib.config.api.sync.annotation.UnsyncableConfig
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment

@UnsyncableConfig
data class SodiumConfig(
    @JvmField
    @Comment("Whether or not to disable Sodium's resource pack compatibility scanner.")
    var disableCompatibilityChecks: Boolean? = false,

    @JvmField
    @Comment("Resource pack files to be ignored by Sodium's resource pack compatibility scanner.")
    var ignoredResourcePacks: List<String?>? = arrayListOf(),
) {

    companion object : JsonConfig<SodiumConfig>(
        MOD_ID,
        SodiumConfig::class.java,
        makeThirdPartyConfigPath("sodium"),
        CONFIG_JSONTYPE,
        false,
        null,
        null,
    ) {
        init {
            ConfigRegistry.register(this)
        }

        @JvmStatic
        fun get(): SodiumConfig = this.instance()
    }

}
