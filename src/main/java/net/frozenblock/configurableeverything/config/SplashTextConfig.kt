@file:Environment(EnvType.CLIENT)

package net.frozenblock.configurableeverything.config

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.util.CEConfig
import net.frozenblock.configurableeverything.util.CESimpleConfig
import net.frozenblock.configurableeverything.util.CONFIG_FORMAT
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.instance.xjs.XjsConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.SyncBehavior
import net.frozenblock.lib.config.v2.entry.ConfigEntry
import net.frozenblock.lib.config.v2.entry.EntryType
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.material.MapColor

// UNSYNCABLE
object SplashTextConfig : CEConfig("splash_text") {

    @JvmField
    var addedSplashes: ConfigEntry<MutableList<String>> = this.unsyncableEntry("addedSplashes",
        EntryType.STRING.asList(),
        arrayListOf(
            "Configurable Everything!"
        )
    )

    @JvmField
    var removedSplashes: ConfigEntry<MutableList<String>> = this.unsyncableEntry("removedSplashes",
        EntryType.STRING.asList(),
        arrayListOf(
            "random splash text"
        )
    )

    @JvmField
    var splashColor: ConfigEntry<Int> = this.unsyncableEntry("splashColor",
        EntryType.INT,
        DyeColor.YELLOW.textColor
    )

    @JvmField
    var removeVanilla: ConfigEntry<Boolean> = this.unsyncableEntry("removeVanilla",
        EntryType.BOOL,
        true,
        "Removes all vanilla splashes."
    )
}
