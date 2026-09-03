package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.util.CESimpleConfig
import net.frozenblock.lib.config.v1.registry.BasicConfigRegistry
import blue.endless.jankson.Comment

// UNSYNCABLE
data class GameConfig(
    @JvmField
    // UNSYNCABLE
    @Comment("Changes the game window title. Change this from an empty string to apply.")
    var windowTitle: String = "",

    @JvmField
    // UNSYNCABLE
    @Comment(
"""
Changes the version series. Change this from an empty string to apply.
The default version series is "main".
"""
    )
    var versionSeries: String = ""
) {
    companion object : CESimpleConfig<GameConfig>(
        GameConfig::class,
        "game"
    ) {

        init {
            BasicConfigRegistry.register(this)
        }

        @JvmStatic
        @JvmOverloads
        fun get(real: Boolean = false): GameConfig = if (real) this.instance() else this.config()
    }

}
