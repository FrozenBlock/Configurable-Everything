package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.util.CEConfig
import net.frozenblock.configurableeverything.util.CONFIG_FORMAT
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.instance.xjs.XjsConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.SyncBehavior
import net.frozenblock.lib.config.api.sync.annotation.EntrySyncData
import net.frozenblock.lib.config.api.sync.annotation.UnsyncableConfig
import blue.endless.jankson.Comment

@UnsyncableConfig
data class GameConfig(
    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    @Comment("Changes the game window title. Change this from an empty string to apply.")
    var windowTitle: String = "",

    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    @Comment(
"""
Changes the version series. Change this from an empty string to apply.
The default version series is "main".
"""
    )
    var versionSeries: String = ""
) {
    companion object : CEConfig<GameConfig>(
        GameConfig::class,
        "game"
    ) {

        init {
            ConfigRegistry.register(this)
        }

        @JvmStatic
        @JvmOverloads
        fun get(real: Boolean = false): GameConfig = if (real) this.instance() else this.config()
    }

}
