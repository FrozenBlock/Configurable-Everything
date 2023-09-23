package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment

data class WorldConfig(
    @JvmField
    @Comment("Does not modify tick rate. Only modifies daytime speed.")
    var dayTimeSpeedAmplifier: Long? = 1,

    @JvmField
    var fixSunMoonRotating: Boolean? = false,

    @JvmField
    @Comment("Incompatible with mod Bedrockify.")
    var sunSize: Int? = 300,

    @JvmField
    var moonSize: Int? = 200,

    @JvmField
    @Comment("Disables the experimental warning screen when creating or loading worlds.")
    var disableExperimentalWarning: Boolean? = false,
) {
    companion object {
        @JvmField
        internal val INSTANCE: Config<WorldConfig> = ConfigRegistry.register(
            JsonConfig(
                MOD_ID,
                WorldConfig::class.java,
                makeConfigPath("world"),
                CONFIG_JSONTYPE
            )
        )

        @JvmStatic
        val sunSize: Float? get() = get().sunSize?.div(10F)

        @JvmStatic
        val moonSize: Float? get() = get().moonSize?.div(10F)

        @JvmStatic
        fun get(): WorldConfig = INSTANCE.config()
    }
}
