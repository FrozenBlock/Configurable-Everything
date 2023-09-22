package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.instance.json.JsonType
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment

data class MainConfig(
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
    var registry: Boolean? = false,

    @JvmField
    var screen_shake: Boolean? = false,

    @JvmField
    var surface_rule: Boolean? = false,

    @JvmField
    var world: Boolean? = false,

    @JvmField
    @Comment("Datapack features will not apply unless the main toggle and datapack toggle are set to true.")
    var datapack: DatapackConfig? = DatapackConfig(),

    @JvmField
    var kotlinScripting: KotlinScriptingConfig? = KotlinScriptingConfig()
) {
    companion object {
        @JvmField
        internal val INSTANCE: Config<MainConfig> = ConfigRegistry.register(
            JsonConfig(
                MOD_ID,
                MainConfig::class.java,
                makeConfigPath("main"),
                CONFIG_JSONTYPE
            )
        )

        @JvmStatic
        fun get(): MainConfig = INSTANCE.config()
    }

    data class DatapackConfig(
        @JvmField
        var applyDatapacksFolder: Boolean? = true,

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
        var applyKotlinScripts: Boolean? = true

        @JvmField
        var defaultImports: List<String>? = ArrayList(
            listOf(
                "java.util.*",
                "kotlinx.coroutines.*",
                "net.frozenblock.configurableeverything.util.*",
                "net.minecraft.core.registries.*",
                "net.minecraft.core.registries.Registries.*",
                "net.minecraft.resources.*",
                "net.minecraft.server.*",
                "net.minecraft.world.level.block.*",
                "net.minecraft.world.level.block.state.*",
                "net.minecraft.world.level.block.state.BlockBehaviour.Properties",
            )
        )
    )
}
