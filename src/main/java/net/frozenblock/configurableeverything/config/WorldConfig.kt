package net.frozenblock.configurableeverything.config

import blue.endless.jankson.Comment
import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry

class WorldConfig {
    companion object {
        internal val INSTANCE: Config<WorldConfig> = ConfigRegistry.register(
            JsonConfig(
                MOD_ID,
                WorldConfig::class.java,
                makeConfigPath("world"),
                CONFIG_JSONTYPE
            )
        )

        @JvmStatic
        fun getSunSize(): Float = get().sunSize / 10F

        @JvmStatic
        fun getMoonSize(): Float = get().moonSize / 10F

        @JvmStatic
        fun get(): WorldConfig = INSTANCE.config()
    }

    @JvmField
	@Comment("Does not modify tick rate. Only modifies daytime speed.")
    var dayTimeSpeedAmplifier: Long = 1
    @JvmField
	var fixSunMoonRotating = false

    @JvmField
	@Comment("Incompatible with mod Bedrockify.")
    var sunSize = 300
    @JvmField
	var moonSize = 200

    @JvmField
	@Comment("Disables the experimental warning screen when creating or loading worlds.")
    var disableExperimentalWarning = false
}
