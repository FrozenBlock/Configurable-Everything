package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeLegacyConfigPath
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.annotation.UnsyncableConfig
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment

@Suppress("PropertyName", "SpellCheckingInspection")
@UnsyncableConfig
data class MixinsConfig(
    @Comment(
"""
Enables or disables the mod's mixins.
Warning: Functionality will be lost if these are disabled.
"""
    )

    @JvmField
    var biome_placement: Boolean = true,

    @JvmField
    var datafixer: Boolean = true,

    @JvmField
    var datapack: Boolean = true,

    @JvmField
    var entity: Boolean = true,

    @JvmField
    var entity_zombie: Boolean = true,

    @JvmField
    var fluid: Boolean = true,

    @JvmField
    var game: Boolean = true,

    @JvmField
    var game_client: Boolean = true,

    @JvmField
    var item: Boolean = true,

    @JvmField
    var loot: Boolean = true,

    @JvmField
    var screenshake: Boolean = true,

    @JvmField
    var screenshake_client: Boolean = true,

    @JvmField
    var sculk_spreading: Boolean = true,

    @JvmField
    //@Environment(EnvType.CLIENT) // not working idk why
    @Comment("Client only")
    var splash_text: Boolean = true,

    @JvmField
    var structure: Boolean = true,

    @JvmField
    var world: Boolean = true,

    @JvmField
    var world_client: Boolean = true
) {
    companion object : JsonConfig<MixinsConfig>(
        MOD_ID,
        MixinsConfig::class.java,
        makeLegacyConfigPath("mixins"),
        CONFIG_JSONTYPE,
        false, // horrible idea to support modification of this config
        null,
        null
    ) {

        init {
            ConfigRegistry.register(this)
        }

        @JvmStatic
        fun get(): MixinsConfig = this.config()
    }
}
