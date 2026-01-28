package net.frozenblock.configurableeverything.config

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.util.*
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.SyncBehavior
import net.frozenblock.lib.config.v2.config.ConfigSettings
import net.frozenblock.lib.config.v2.entry.ConfigEntry
import net.frozenblock.lib.config.v2.entry.EntryType
import net.frozenblock.lib.config.v2.entry.property.VisibilityPredicate
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment
import net.frozenblock.lib.shadow.blue.endless.jankson.annotation.SaveToggle
import net.frozenblock.lib.shadow.xjs.data.JsonValue
import kotlin.io.path.pathString

object MainConfig : CEConfig("main") {
    // ignore property name warnings because the goal is to match the config file name
    @JvmField
    @Comment(
"""
Enabled configs
Warning: It is important to check the contents of each config before enabling them here.
"""
    )
    var biome: ConfigEntry<Boolean> = this.entry("biome", EntryType.BOOL, false)

    @JvmField
    var biome_placement: ConfigEntry<Boolean> = this.entry("biome_placement", EntryType.BOOL, false)

    @JvmField
    var block: ConfigEntry<Boolean> = this.entry("block", EntryType.BOOL, false)

    @JvmField
    var datafixer: ConfigEntry<Boolean> = this.entry("datafixer", EntryType.BOOL, false)

    @JvmField
    var entity: ConfigEntry<Boolean> = this.entry("entity", EntryType.BOOL, false)

    @JvmField
    var fluid: ConfigEntry<Boolean> = this.entry("fluid", EntryType.BOOL, false)

    @JvmField
    var gravity: ConfigEntry<Boolean> = this.entry("gravity", EntryType.BOOL, false)

    @JvmField
    var item: ConfigEntry<Boolean> = this.entry("item", EntryType.BOOL, false)

    @JvmField
    var loot: ConfigEntry<Boolean> = this.entry("loot", EntryType.BOOL, false)

    @JvmField
    @SaveToggle(ENABLE_EXPERIMENTAL_FEATURES)
    var music: ConfigEntry<Boolean> = this.unsyncableEntryBuilder("music", EntryType.BOOL, false).visibilityPredicate(
        VisibilityPredicate.of { ENABLE_EXPERIMENTAL_FEATURES }
    ).build()

    @JvmField
    var recipe: ConfigEntry<Boolean> = this.entry("recipe", EntryType.BOOL, false)

    @JvmField
    var registry: ConfigEntry<Boolean> = this.entry("registry", EntryType.BOOL, false)

    @JvmField
    var screen_shake: ConfigEntry<Boolean> = this.entry("screen_shake", EntryType.BOOL, false)

    @JvmField
    // UNSYNCABLE
    @Comment("Requires Fabric Kotlin Extensions")
    var scripting: ConfigEntry<Boolean> = this.unsyncableEntry("scripting", EntryType.BOOL, false)

    @JvmField
    var sculk_spreading: ConfigEntry<Boolean> = this.entry("sculk_spreading", EntryType.BOOL, false)

    @JvmField
    @Environment(EnvType.CLIENT) // not working idk why
    // UNSYNCABLE
    @Comment("Client only")
    var splash_text: ConfigEntry<Boolean> = this.unsyncableEntry("splash_text", EntryType.BOOL, false)

    @JvmField
    var structure: ConfigEntry<Boolean> = this.entry("structure", EntryType.BOOL, false)

    @JvmField
    var surface_rule: ConfigEntry<Boolean> = this.entry("surface_rule", EntryType.BOOL, false)

    @JvmField
    var tag: ConfigEntry<Boolean> = this.entry("tag", EntryType.BOOL, false)

    @JvmField
    var world: ConfigEntry<Boolean> = this.entry("world", EntryType.BOOL, false)

    // DATAPACK

    @JvmField
    // UNSYNCABLE
    var applyDatapackFolders: ConfigEntry<Boolean> = unsyncableEntry("datapack/applyDatapackFolders", EntryType.BOOL, true)

    @JvmField
    // UNSYNCABLE
    var datapackFolders: ConfigEntry<List<String>> = unsyncableEntry("datapack/datapackFolders", EntryType.STRING.asList(), arrayListOf(
        DATAPACKS_PATH.pathString.replace('\\', '/'), // make it readable
        "./datapacks"
    ))

    @JvmField
    // UNSYNCABLE
    var datapackBiome: ConfigEntry<Boolean> = this.unsyncableEntry("datapack/biome", EntryType.BOOL, true)

    @JvmField
    // UNSYNCABLE
    var datapackBiomePlacement: ConfigEntry<Boolean> = this.unsyncableEntry("datapack/biome_placement", EntryType.BOOL, true)

    @JvmField
    // UNSYNCABLE
    @Comment("Allows the usage of json5, djs (this file), jsonc, hjson, txt, and ubjson files in datapacks.")
    var moreJsonSupport: ConfigEntry<Boolean> = this.unsyncableEntry("datapack/moreJsonSupport", EntryType.BOOL, true)
}
