package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.util.*
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment
import kotlin.io.path.pathString

data class MainConfig(
    // the configs may have weird casing because the goal is to match the config file name
    @JvmField
    @Comment(
"""
Enabled configs
Warning: It is important to check the contents of each config before enabling them here.
"""
    )
    var biome: Boolean? = false,

    @JvmField
    var biome_placement: Boolean? = false,

    @JvmField
    var block: Boolean? = false,

    @JvmField
    var datafixer: Boolean? = false,

    @JvmField
    var entity: Boolean? = false,

    @JvmField
    var fluid: Boolean? = false,

    @JvmField
    var game: Boolean? = false,

    @JvmField
    var item: Boolean? = false,

    @JvmField
    var registry: Boolean? = false,

    @JvmField
    var screen_shake: Boolean? = false,

    @JvmField
    var splash_text: Boolean? = false,

    @JvmField
    var surface_rule: Boolean? = false,

    @JvmField
    var world: Boolean? = false,

    @JvmField
    @Comment("Datapack features will not apply unless the main toggle and datapack toggle are set to true.")
    var datapack: DatapackConfig? = DatapackConfig(),

    @JvmField
    @Comment("Requires Fabric Kotlin Extensions")
    var kotlinScripting: KotlinScriptingConfig? = KotlinScriptingConfig()
) {
    companion object {
        @JvmField
        val INSTANCE: Config<MainConfig> = ConfigRegistry.register(
            JsonConfig(
                MOD_ID,
                MainConfig::class.java,
                makeConfigPath("main"),
                CONFIG_JSONTYPE
            )
        )

        @JvmStatic
        fun get(real: Boolean = false): MainConfig = if (real) INSTANCE.instance() else INSTANCE.config()
    }

    data class DatapackConfig(
        @JvmField
        var applyDatapackFolders: Boolean? = true,

        @JvmField
        var datapackFolders: List<String?>? = arrayListOf(
            DATAPACKS_PATH.pathString.replace('\\', '/'), // make it readable
            "./datapacks"
        ),

        @JvmField
        var biome: Boolean? = true,

        @JvmField
        var biome_placement: Boolean? = true,

        @JvmField
        @Comment("Allows the usage of JSON5 files in datapacks.")
        var json5Support: Boolean? = true
    )

    data class KotlinScriptingConfig(
        @JvmField
        var applyKotlinScripts: Boolean? = true,

        @JvmField
        var defaultImports: List<String>? = arrayListOf(
            "kotlinx.coroutines.*",
            "net.frozenblock.configurableeverything.util.*",
            "net.frozenblock.configurableeverything.scripting.util.*",
        )?.apply {
            if (ENABLE_EXPERIMENTAL_FEATURES)
                this.add("net.frozenblock.lib.config.api.instance.ConfigModification")
        }
    )
}
