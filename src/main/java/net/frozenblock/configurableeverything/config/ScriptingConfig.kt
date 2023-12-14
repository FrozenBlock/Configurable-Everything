package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.util.*
import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE
import net.frozenblock.configurableeverything.util.ENABLE_EXPERIMENTAL_FEATURES
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.lib.config.api.annotation.UnsyncableConfig
import net.frozenblock.lib.config.api.annotation.UnsyncableEntry
import net.frozenblock.lib.config.api.instance.ConfigModification
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment
import net.frozenblock.lib.shadow.blue.endless.jankson.annotation.SaveToggle
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.dimension.DimensionType
import kotlin.reflect.jvm.jvmName

@UnsyncableConfig
data class ScriptingConfig(

    @JvmField
    @UnsyncableEntry
    @Comment("Requires Fabric Kotlin Extensions")
    var applyKotlinScripts: Boolean? = true,

    @JvmField
    @UnsyncableEntry
    var defaultImports: List<String>? = arrayListOf(
        "kotlinx.coroutines.*",
        "net.frozenblock.configurableeverything.util.*",
        "net.frozenblock.configurableeverything.scripting.util.*",
    ).apply {
        ifExperimental {
            this.add("net.minecraft.core.*")
            this.add("net.minecraft.core.registries.*")
            this.addAll(setOf(
                ConfigModification::class,
                ResourceKey::class,
                ResourceLocation::class,
                Block::class,
                BlockBehaviour::class,
                BlockBehaviour.Properties::class,
                DimensionType::class,
            ).map { it.jvmName.replace('$', '.') })
        }
    },

    @JvmField
    @UnsyncableEntry
    @Comment("Not functional until 1.1")
    @SaveToggle(ENABLE_EXPERIMENTAL_FEATURES)
    var remapping: Boolean? = true,

    @JvmField
    @UnsyncableEntry
    @Comment("Not functional until 1.1")
    @SaveToggle(ENABLE_EXPERIMENTAL_FEATURES)
    var filter: FilterOption? = FilterOption.INCLUDED,

    @JvmField
    @UnsyncableEntry
    @Comment("Not functional until 1.1")
    @SaveToggle(ENABLE_EXPERIMENTAL_FEATURES)
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
