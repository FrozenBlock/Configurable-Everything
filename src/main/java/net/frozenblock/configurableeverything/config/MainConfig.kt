package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.util.*
import net.frozenblock.lib.config.api.instance.xjs.XjsConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.SyncBehavior
import net.frozenblock.lib.config.api.sync.annotation.EntrySyncData
import net.frozenblock.lib.config.api.sync.annotation.UnsyncableConfig
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment
import net.frozenblock.lib.shadow.blue.endless.jankson.annotation.SaveToggle
import kotlin.io.path.pathString

data class MainConfig(
    // ignore property name warnings because the goal is to match the config file name
    @JvmField
    @EntrySyncData("biome")
    @Comment(
"""
Enabled configs
Warning: It is important to check the contents of each config before enabling them here.
"""
    )
    var biome: Boolean = false,

    @JvmField
    @EntrySyncData("biome_placement")
    var biome_placement: Boolean = false,

    @JvmField
    @EntrySyncData("block")
    var block: Boolean = false,

    @JvmField
    @EntrySyncData("datafixer")
    var datafixer: Boolean = false,

    @JvmField
    @EntrySyncData("entity")
    var entity: Boolean = false,

    @JvmField
    @EntrySyncData("fluid")
    var fluid: Boolean = false,

    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    var game: Boolean = false,

    @JvmField
    @EntrySyncData("gravity")
    var gravity: Boolean = false,

    @JvmField
    @EntrySyncData("item")
    var item: Boolean = false,

    @JvmField
    @EntrySyncData("loot")
    var loot: Boolean = false,

    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    @SaveToggle(ENABLE_EXPERIMENTAL_FEATURES)
    var music: Boolean = false,

    @JvmField
    @EntrySyncData("recipe")
    var recipe: Boolean = false,

    @JvmField
    @EntrySyncData("registry")
    var registry: Boolean = false,

    @JvmField
    @EntrySyncData("screen_shake")
    var screen_shake: Boolean = false,

    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    @Comment("Requires Fabric Kotlin Extensions")
    var scripting: Boolean = false,

    @JvmField
    @EntrySyncData("sculk_spreading")
    var sculk_spreading: Boolean = false,

    @JvmField
    //@Environment(EnvType.CLIENT) // not working idk why
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    @Comment("Client only")
    var splash_text: Boolean = false,

    @JvmField
    @EntrySyncData("structure")
    var structure: Boolean = false,

    @JvmField
    @EntrySyncData("surface_rule")
    var surface_rule: Boolean = false,

    @JvmField
    @EntrySyncData("tag")
    var tag: Boolean = false,

    @JvmField
    @EntrySyncData("world")
    var world: Boolean = false,

    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    @Comment("Datapack features will not apply unless the main toggle and datapack toggle are set to true.")
    var datapack: DatapackConfig = DatapackConfig(),
) {

    companion object : CEConfig<MainConfig>(
        MainConfig::class,
        "main"
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
        @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
        var applyDatapackFolders: Boolean = true,

        @JvmField
        @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
        var datapackFolders: List<String> = arrayListOf(
            DATAPACKS_PATH.pathString.replace('\\', '/'), // make it readable
            "./datapacks"
        ),

        @JvmField
        @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
        var biome: Boolean = true,

        @JvmField
        @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
        var biome_placement: Boolean = true,

        @JvmField
        @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
        @Comment("Allows the usage of json5, djs (this file), jsonc, hjson, txt, and ubjson files in datapacks.")
        var moreJsonSupport: Boolean = true
    )
}
