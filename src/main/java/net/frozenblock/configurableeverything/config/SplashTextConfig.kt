@file:Environment(EnvType.CLIENT)

package net.frozenblock.configurableeverything.config

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.util.CESimpleConfig
import net.frozenblock.configurableeverything.util.CONFIG_FORMAT
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.instance.xjs.XjsConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.SyncBehavior
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.material.MapColor

// UNSYNCABLE
data class SplashTextConfig(

    @JvmField
    // UNSYNCABLE
    var addedSplashes: MutableList<String> = arrayListOf(
        "Configurable Everything!"
    ),

    @JvmField
    // UNSYNCABLE
    var removedSplashes: MutableList<String> = arrayListOf(
        "random splash text"
    ),

    @JvmField
    // UNSYNCABLE
    var splashColor: Int = DyeColor.YELLOW.textColor,

    @JvmField
    // UNSYNCABLE
    @Comment("Removes all vanilla splashes.")
    var removeVanilla: Boolean = true
) {
    companion object : CESimpleConfig<SplashTextConfig>(
        SplashTextConfig::class,
        "splash_text",
    ) {

        init {
            ConfigRegistry.register(this)
        }

        @JvmStatic
        @JvmOverloads
        fun get(real: Boolean = false): SplashTextConfig = if (real) this.instance() else this.config()
    }
}
