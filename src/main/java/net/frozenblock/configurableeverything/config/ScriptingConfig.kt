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

        // minecraft
        "net.minecraft.core.*",
        "net.minecraft.core.registries.*",
        "net.minecraft.resources.ResourceKey",
        "net.minecraft.resources.Identifier",
        "net.minecraft.world.level.block.Block",
        "net.minecraft.world.level.block.state.BlockBehaviour",
        "net.minecraft.world.level.block.state.BlockBehaviour.Properties",
        "net.minecraft.world.level.dimension.DimensionType",
    ),

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
