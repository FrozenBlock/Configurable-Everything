package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.util.CESimpleConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.SyncBehavior
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment

// UNSYNCABLE
data class ScriptingConfig(

    @JvmField
    // UNSYNCABLE
    @Comment("Requires Fabric Kotlin Extensions")
    var applyKotlinScripts: Boolean = true,

    @JvmField
    // UNSYNCABLE
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
) {

    companion object : CESimpleConfig<ScriptingConfig>(
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
}
