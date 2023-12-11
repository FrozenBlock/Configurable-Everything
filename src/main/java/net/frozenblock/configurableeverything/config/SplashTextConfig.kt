@file:Environment(EnvType.CLIENT)

package net.frozenblock.configurableeverything.config

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.annotation.UnsyncableConfig
import net.frozenblock.lib.config.api.annotation.UnsyncableEntry
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment
import net.minecraft.world.item.DyeColor

@UnsyncableConfig
data class SplashTextConfig(

    @JvmField
    @UnsyncableEntry
    var addedSplashes: List<String?>? = arrayListOf(
        "Configurable Everything!"
    ),

    @JvmField
    @UnsyncableEntry
    var removedSplashes: List<String?>? = arrayListOf(
        "random splash text"
    ),

    @JvmField
    @UnsyncableEntry
    var splashColor: Int? = DyeColor.YELLOW.textColor,

    @JvmField
    @UnsyncableEntry
    @Comment("Removes all vanilla splashes.")
    var removeVanilla: Boolean? = true
) {
    companion object : JsonConfig<SplashTextConfig>(
        MOD_ID,
        SplashTextConfig::class.java,
        makeConfigPath("splash_text"),
        CONFIG_JSONTYPE,
        null,
        null
    ) {

        init {
            ConfigRegistry.register(this)
        }

        @JvmStatic
        @JvmOverloads
        fun get(real: Boolean = false): SplashTextConfig = if (real) this.instance() else this.config()
    }
}
