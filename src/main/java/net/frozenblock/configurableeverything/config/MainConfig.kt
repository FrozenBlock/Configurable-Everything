package net.frozenblock.configurableeverything.config

import blue.endless.jankson.Comment
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry

class MainConfig {
    companion object {
        private val INSTANCE: Config<MainConfig> = ConfigRegistry.register(
            JsonConfig(
                MOD_ID,
                MainConfig::class.java,
                makeConfigPath("main", true),
                true
            )
        )

        @JvmStatic
        fun get(): MainConfig = INSTANCE.config()

        @JvmStatic
        fun getConfigInstance(): Config<MainConfig> = INSTANCE
    }

    // the configs may have weird casing because the goal is to match the config file name
    @JvmField
	@Comment(
        """
		Enabled configs
		Warning: It is important to check the contents of each config before enabling them here.

		"""
    )
    var biome: Boolean? = false

    @JvmField
	var biome_placement: Boolean? = false

    @JvmField
	var datafixer: Boolean? = false

    @JvmField
	var entity: Boolean? = false

    @JvmField
	var fluid: Boolean? = false

    @JvmField
	var game: Boolean? = false

    @JvmField
	var screen_shake: Boolean? = false

    @JvmField
	var splash_text: Boolean? = false

    @JvmField
	var surface_rule: Boolean? = false

    @JvmField
	var world: Boolean? = false

    @JvmField
	@Comment("Datapack features will not apply unless the main toggle and datapack toggle is set to true.")
    val datapack: DatapackConfig? = DatapackConfig()

    class DatapackConfig {
        @JvmField
		var applyDatapacksFolder: Boolean? = true

        @JvmField
		var biome: Boolean? = true

        @JvmField
		var biome_placement: Boolean? = true

        @JvmField
        @Comment("Allows the usage of JSON5 files in datapacks.")
		var json5Support: Boolean? = true
    }
}
