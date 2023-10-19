package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment

data class GameConfig(
    @JvmField
    @Comment("Changes the game window title. Change this from an empty string to apply.")
    var windowTitle: String? = "",

    @JvmField
    @Comment(
"""
Changes the version series. Change this from an empty string to apply.
The default version series is "main".
"""
    )
    var versionSeries: String? = ""
) {
    companion object {
        @JvmField
        val INSTANCE: Config<GameConfig> = ConfigRegistry.register(
            JsonConfig(
                MOD_ID,
                GameConfig::class.java,
                makeConfigPath("game"),
                CONFIG_JSONTYPE
            )
        )

        @JvmStatic
        fun get(real: Boolean = false): GameConfig = if (real) INSTANCE.instance() else INSTANCE.config()
    }
}
