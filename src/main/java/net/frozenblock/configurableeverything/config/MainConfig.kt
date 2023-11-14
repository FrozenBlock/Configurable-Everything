package net.frozenblock.configurableeverything.config

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.util.*
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment
import kotlin.io.path.pathString

data class MainConfig(
    // ignore property name warnings because the goal is to match the config file name
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
    @Comment("Not functional until 1.1")
    var gravity: Boolean? = false,

    @JvmField
    @Comment("Not functional until 1.1")
    var item: Boolean? = false,

    @JvmField
    @Comment("Not functional until 1.1")
    var music: Boolean? = false,

    @JvmField
    var registry: Boolean? = false,

    @JvmField
    var screen_shake: Boolean? = false,

    @JvmField
    @Comment("Not functional until 1.1")
    var sculk_spreading: Boolean? = false,

    @JvmField
    @Environment(EnvType.CLIENT)
    @Comment("Client only")
    var splash_text: Boolean? = false,

    @JvmField
    @Comment("Not functional until 1.1")
    var structure: Boolean? = false,

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

    companion object : JsonConfig<MainConfig>(
        MOD_ID,
        MainConfig::class.java,
        makeConfigPath("main"),
        CONFIG_JSONTYPE
    ) {
        init {
            ConfigRegistry.register(this)
        }

        @JvmStatic
        @JvmOverloads
        fun get(real: Boolean = false): MainConfig = if (real) this.instance() else this.config()
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
        ).apply {
            ifExperimental {
                this.add("net.frozenblock.lib.config.api.instance.ConfigModification")
                this.add("net.minecraft.core.*")
                this.add("net.minecraft.core.registries.*")
                this.add("net.minecraft.core.resources.ResourceKey")
                this.add("net.minecraft.core.resources.ResourceLocation")
            }
        }
    )
}
