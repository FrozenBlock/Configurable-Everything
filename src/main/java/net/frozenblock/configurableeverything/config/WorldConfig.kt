package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.annotation.*
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment

@UnsyncableConfig
data class WorldConfig(
    @JvmField
    @FieldIdentifier(identifier = "dayTimeSpeedAmplifier")
    @Comment("Does not modify tick rate. Only modifies daytime speed.")
    var dayTimeSpeedAmplifier: Long? = 1,

    @JvmField
    @UnsyncableEntry
    var fixSunMoonRotating: Boolean? = false,

    @JvmField
    @UnsyncableEntry
    @Comment("Incompatible with mod Bedrockify.")
    var sunSize: Int? = 300,

    @JvmField
    @UnsyncableEntry
    var moonSize: Int? = 200,

    @JvmField
    @UnsyncableEntry
    @Comment("Disables the experimental warning screen when creating or loading worlds.")
    var disableExperimentalWarning: Boolean? = false,
) {
    companion object : JsonConfig<WorldConfig>(
        MOD_ID,
        WorldConfig::class.java,
        makeConfigPath("world"),
        CONFIG_JSONTYPE,
        null,
        null
    ) {

        init {
            ConfigRegistry.register(this)
        }

        @JvmStatic
        inline val sunSize: Float? get() = get().sunSize?.div(10F)

        @JvmStatic
        inline val moonSize: Float? get() = get().moonSize?.div(10F)

        @JvmStatic
        @JvmOverloads
        fun get(real: Boolean = false): WorldConfig = if (real) this.instance() else this.config()
    }
}
