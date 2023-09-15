package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.instance.json.JsonType
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment

class MixinsConfig {
    companion object {
        @JvmField
        internal val INSTANCE: Config<MixinsConfig> = ConfigRegistry.register(
            JsonConfig(
                MOD_ID,
                MixinsConfig::class.java,
                makeConfigPath("mixins"),
                CONFIG_JSONTYPE
            )
        )

        @JvmStatic
        fun get(): MixinsConfig = INSTANCE.config()
    }

    @Comment(
"""
Enables or disables the mod's mixins.
Warning: Functionality will be lost if these are disabled.
"""
    )

    @JvmField
    var biome_placement: Boolean? = true

    @JvmField
    var datafixer: Boolean? = true

    @JvmField
    var datapack: Boolean? = true

    @JvmField
    var entity: Boolean? = true

    @JvmField
    var entity_zombie: Boolean? = true

    @JvmField
    var fluid: Boolean? = true

    @JvmField
    var game: Boolean? = true

    @JvmField
    var game_client: Boolean? = true

    @JvmField
    var screenshake: Boolean? = true

    @JvmField
    var screenshake_client: Boolean? = true

    @JvmField
    var splash_text: Boolean? = true

    @JvmField
    var world: Boolean? = true

    @JvmField
    var world_client: Boolean? = true
}
