package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.instance.json.JsonType
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment
import net.minecraft.world.item.DyeColor

class SplashTextConfig {
    companion object {
        @JvmField
        internal val INSTANCE: Config<SplashTextConfig> = ConfigRegistry.register(
            JsonConfig(
                MOD_ID,
                SplashTextConfig::class.java,
                makeConfigPath("splash_text"),
                CONFIG_JSONTYPE
            )
        )

        @JvmStatic
        fun get(): SplashTextConfig = INSTANCE.config()
    }

    @JvmField
	var addedSplashes: List<String?>? = ArrayList(
        listOf(
            "Configurable Everything!"
        )
    )
    @JvmField
	var removedSplashes: List<String?>? = ArrayList(
        listOf(
            "random splash text"
        )
    )
    @JvmField
	var splashColor: Int? = DyeColor.YELLOW.textColor

    @JvmField
	@Comment("Removes all vanilla splashes.")
    var removeVanilla: Boolean? = true
}
