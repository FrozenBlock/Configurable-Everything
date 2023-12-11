package net.frozenblock.configurableeverything.config

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.util.*
import net.frozenblock.lib.config.api.annotation.FieldIdentifier
import net.frozenblock.lib.config.api.annotation.UnsyncableConfig
import net.frozenblock.lib.config.api.annotation.UnsyncableEntry
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment
import kotlin.io.path.pathString

@UnsyncableConfig
data class MainConfig(
    // ignore property name warnings because the goal is to match the config file name
    @JvmField
    @FieldIdentifier(identifier = "biome")
    @Comment(
"""
Enabled configs
Warning: It is important to check the contents of each config before enabling them here.
"""
    )
    var biome: Boolean? = false,

    @JvmField
    @FieldIdentifier(identifier = "biome_placement")
    var biome_placement: Boolean? = false,

    @JvmField
    @FieldIdentifier(identifier = "block")
    var block: Boolean? = false,

    @JvmField
    @FieldIdentifier(identifier = "datafixer")
    var datafixer: Boolean? = false,

    @JvmField
    @FieldIdentifier(identifier = "entity")
    var entity: Boolean? = false,

    @JvmField
    @FieldIdentifier(identifier = "fluid")
    var fluid: Boolean? = false,

    @JvmField
    @UnsyncableEntry
    var game: Boolean? = false,

    @JvmField
    @FieldIdentifier(identifier = "gravity")
    @Comment("Not functional until 1.1")
    var gravity: Boolean? = false,

    @JvmField
    @FieldIdentifier(identifier = "item")
    @Comment("Not functional until 1.1")
    var item: Boolean? = false,

    @JvmField
    @UnsyncableEntry
    @Comment("Not functional until 1.1")
    var music: Boolean? = false,

    @JvmField
    @FieldIdentifier(identifier = "registry")
    var registry: Boolean? = false,

    @JvmField
    @UnsyncableEntry
    var screen_shake: Boolean? = false,

    @JvmField
    @UnsyncableEntry
    @Comment("Requires Fabric Kotlin Extensions")
    var scripting: Boolean? = false,

    @JvmField
    @FieldIdentifier(identifier = "sculk_spreading")
    @Comment("Not functional until 1.1")
    var sculk_spreading: Boolean? = false,

    @JvmField
    //@Environment(EnvType.CLIENT) // not working idk why
    @UnsyncableEntry
    @Comment("Client only")
    var splash_text: Boolean? = false,

    @JvmField
    @FieldIdentifier(identifier = "structure")
    @Comment("Not functional until 1.1")
    var structure: Boolean? = false,

    @JvmField
    @FieldIdentifier(identifier = "surface_rule")
    var surface_rule: Boolean? = false,

    @JvmField
    @FieldIdentifier(identifier = "world")
    var world: Boolean? = false,

    @JvmField
    @UnsyncableEntry
    @Comment("Datapack features will not apply unless the main toggle and datapack toggle are set to true.")
    var datapack: DatapackConfig? = DatapackConfig(),
) {

    companion object : JsonConfig<MainConfig>(
        MOD_ID,
        MainConfig::class.java,
        makeConfigPath("main"),
        CONFIG_JSONTYPE,
        null,
        null
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
        @UnsyncableEntry
        var applyDatapackFolders: Boolean? = true,

        @JvmField
        @UnsyncableEntry
        var datapackFolders: List<String?>? = arrayListOf(
            DATAPACKS_PATH.pathString.replace('\\', '/'), // make it readable
            "./datapacks"
        ),

        @JvmField
        @UnsyncableEntry
        var biome: Boolean? = true,

        @JvmField
        @UnsyncableEntry
        var biome_placement: Boolean? = true,

        @JvmField
        @UnsyncableEntry
        @Comment("Allows the usage of JSON5 files in datapacks.")
        var json5Support: Boolean? = true
    )
}
