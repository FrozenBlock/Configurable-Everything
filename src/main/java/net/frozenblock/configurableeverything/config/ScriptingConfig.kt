package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.util.CEConfig
import net.frozenblock.configurableeverything.util.CONFIG_FORMAT
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.instance.xjs.XjsConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.SyncBehavior
import net.frozenblock.lib.config.api.sync.annotation.EntrySyncData
import net.frozenblock.lib.config.api.sync.annotation.UnsyncableConfig
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment

@UnsyncableConfig
data class ScriptingConfig(

    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    @Comment("Requires Fabric Kotlin Extensions")
    var applyKotlinScripts: Boolean = true,

    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    var defaultImports: List<String> = arrayListOf(
        "kotlinx.coroutines.*",
        "net.frozenblock.configurableeverything.util.*",
        "net.frozenblock.configurableeverything.scripting.util.*",
        "net.frozenblock.lib.config.api.instance.ConfigModification",

        // minecraft
        "net.minecraft.core.*",
        "net.minecraft.core.registries.*",
        "net.minecraft.resources.ResourceKey",
        "net.minecraft.resources.ResourceLocation",
        "net.minecraft.world.level.block.Block",
        "net.minecraft.world.level.block.state.BlockBehaviour",
        "net.minecraft.world.level.block.state.BlockBehaviour.Properties",
        "net.minecraft.world.level.dimension.DimensionType",
    ),

    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    @Comment(
"""
Remapping is experimental.
Requires a network connection.
When a network connection is available, it will download the Official Mojang Mappings

Use of the Official Mojang Mappings must be under the following license.
# (c) 2020 Microsoft Corporation.
These mappings are provided "as-is" and you bear the risk of using them.
You may copy and use the mappings for development purposes, but you may
not redistribute the mappings complete and unmodified. Microsoft makes
no warranties, express or implied, with respect to the mappings provided
here. Use and modification of this document or the source code (in any form)
of Minecraft: Java Edition is governed by the Minecraft End User License
Agreement available at https://account.mojang.com/documents/minecraft_eula.
"""
    )
    var remapping: Boolean = false,

    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    var filter: FilterOption = FilterOption.INCLUDED,

    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    var modsToRemap: List<String> = arrayListOf(
        "configurable_everything",
        "frozenlib",
    ),
) {

    companion object : CEConfig<ScriptingConfig>(
        ScriptingConfig::class,
        "scripting"
    ) {
        init {
            ConfigRegistry.register(this)
        }

        @JvmStatic
        @JvmOverloads
        fun get(real: Boolean = false): ScriptingConfig = if (real) this.instance() else this.config()
    }

    enum class FilterOption {
        INCLUDED,
        EXCLUDED
    }
}
