package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.util.CEConfig
import net.frozenblock.configurableeverything.util.CESimpleConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.SyncBehavior
import net.frozenblock.lib.config.v2.entry.ConfigEntry
import net.frozenblock.lib.config.v2.entry.EntryType
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment

// UNSYNCABLE
object ScriptingConfig : CEConfig("scripting") {

    @JvmField
    // UNSYNCABLE
    @Comment("Requires Fabric Kotlin Extensions")
    var applyKotlinScripts: ConfigEntry<Boolean> = this.unsyncableEntry("applyKotlinScripts", EntryType.BOOL, true)

    @JvmField
    // UNSYNCABLE
    var defaultImports: ConfigEntry<List<String>> = this.unsyncableEntry("defaultImports", EntryType.STRING.asList(),
        arrayListOf(
            "kotlinx.coroutines.*",
            "net.frozenblock.configurableeverything.util.*",

            // minecraft
            "net.minecraft.core.*",
            "net.minecraft.core.registries.*",
            "net.minecraft.resources.ResourceKey",
            "net.minecraft.resources.Identifier",
            "net.minecraft.world.level.block.Block",
            "net.minecraft.world.level.block.state.BlockBehaviour",
            "net.minecraft.world.level.block.state.BlockBehaviour.Properties",
            "net.minecraft.world.level.dimension.DimensionType",
        )
    )
}
