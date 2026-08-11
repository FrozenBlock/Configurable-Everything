package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.util.CEConfig
import net.frozenblock.lib.config.v2.entry.ConfigEntry
import net.frozenblock.lib.config.v2.entry.EntryType
import net.mehvahdjukaar.candlelight.api.ClientOnly
import net.minecraft.world.item.DyeColor

// UNSYNCABLE
@ClientOnly
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
