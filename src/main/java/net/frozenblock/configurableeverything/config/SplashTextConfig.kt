package net.frozenblock.configurableeverything.config

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment
import net.minecraft.world.item.DyeColor

@Environment(EnvType.CLIENT)
class SplashTextConfig {
    companion object {
        @JvmField
        val INSTANCE: Config<SplashTextConfig> = ConfigRegistry.register(
            JsonConfig(
                MOD_ID,
                SplashTextConfig::class.java,
                makeConfigPath("splash_text"),
                CONFIG_JSONTYPE
            )
        )

        @JvmStatic
        fun get(real: Boolean = false): SplashTextConfig = if (real) INSTANCE.instance() else INSTANCE.config()
    }

    @JvmField
	var addedSplashes: List<String?>? = arrayListOf(
        "Configurable Everything!"
    )

    @JvmField
	var removedSplashes: List<String?>? = arrayListOf(
        "random splash text"
    )

    @JvmField
	var splashColor: Int? = DyeColor.YELLOW.textColor

    @JvmField
	@Comment("Removes all vanilla splashes.")
    var removeVanilla: Boolean? = true
}
