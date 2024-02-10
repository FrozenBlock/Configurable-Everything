package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.instance.ConfigModification
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.SyncBehavior
import net.frozenblock.lib.config.api.sync.annotation.EntrySyncData
import net.frozenblock.lib.config.api.sync.annotation.UnsyncableConfig
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.dimension.DimensionType
import kotlin.reflect.jvm.jvmName

@UnsyncableConfig
data class ScriptingConfig(

    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    @Comment("Requires Fabric Kotlin Extensions")
    var applyKotlinScripts: Boolean? = true,

    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    var defaultImports: List<String>? = arrayListOf(
        "kotlinx.coroutines.*",
        "net.frozenblock.configurableeverything.util.*",
        "net.frozenblock.configurableeverything.scripting.util.*",

        // minecraft
        "net.minecraft.core.*",
        "net.minecraft.core.registries.*"
    ).apply {
        // adds all of the listed classes as imports
        this.addAll(setOf(
            ConfigModification::class,
            ResourceKey::class,
            ResourceLocation::class,
            Block::class,
            BlockBehaviour::class,
            BlockBehaviour.Properties::class,
            DimensionType::class
        ).map { it.jvmName.replace('$', '.') })
    },

    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    var remapping: Boolean? = true,

    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    var filter: FilterOption? = FilterOption.INCLUDED,

    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    var modsToRemap: List<String>? = arrayListOf(
        "configurable_everything",
        "frozenlib",
    ),
) {

    companion object : JsonConfig<ScriptingConfig>(
        MOD_ID,
        ScriptingConfig::class.java,
        makeConfigPath("scripting"),
        CONFIG_JSONTYPE,
        null,
        null
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
