package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.util.CEConfig
import net.frozenblock.lib.config.v2.entry.ConfigEntry
import net.frozenblock.lib.config.v2.entry.EntryType

@Suppress("NOTHING_TO_INLINE")
object WorldConfig : CEConfig("world") {
    @JvmField
    var dayTimeSpeedAmplifier: ConfigEntry<Long> = this.entry("dayTimeSpeedAmplifier", EntryType.LONG, 1,
        "Does not modify tick rate. Only modifies daytime speed.\n26.1: Port is TODO"
    )

    @JvmField
    var sunSize: ConfigEntry<Int> = this.unsyncableEntry("sunSize", EntryType.INT, 300,
        "Incompatible with mod Bedrockify."
    )

    @JvmField
    var moonSize: ConfigEntry<Int> = this.unsyncableEntry("moonSize", EntryType.INT, 200)

    @JvmField
    var disableExperimentalWarning: ConfigEntry<Boolean> = this.unsyncableEntry("disableExperimentalWarning", EntryType.BOOL, false,
        "Disables the experimental warning screen when creating or loading worlds."
    )

    @JvmStatic
    inline fun sunSize(): Float = sunSize.get().div(10F)

    @JvmStatic
    inline fun moonSize(): Float = moonSize.get().div(10F)
}
