package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.util.CONFIG_FORMAT
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.instance.xjs.XjsConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.SyncBehavior
import net.frozenblock.lib.config.api.sync.annotation.EntrySyncData
import net.frozenblock.lib.config.api.sync.annotation.UnsyncableConfig
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment

@UnsyncableConfig
data class WorldConfig(
    @JvmField
    @EntrySyncData("dayTimeSpeedAmplifier")
    @Comment("Does not modify tick rate. Only modifies daytime speed.")
    var dayTimeSpeedAmplifier: Long = 1,

    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    var fixSunMoonRotating: Boolean = false,

    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    @Comment("Incompatible with mod Bedrockify.")
    var sunSize: Int = 300,

    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    var moonSize: Int = 200,

    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    @Comment("Disables the experimental warning screen when creating or loading worlds.")
    var disableExperimentalWarning: Boolean = false,
) {
    companion object : XjsConfig<WorldConfig>(
        MOD_ID,
        WorldConfig::class.java,
        makeConfigPath("world"),
        CONFIG_FORMAT
    ) {

        init {
            ConfigRegistry.register(this)
        }

        @JvmStatic
        inline val sunSize: Float get() = get().sunSize.div(10F)

        @JvmStatic
        inline val moonSize: Float get() = get().moonSize.div(10F)

        @JvmStatic
        @JvmOverloads
        fun get(real: Boolean = false): WorldConfig = if (real) this.instance() else this.config()
    }
}
