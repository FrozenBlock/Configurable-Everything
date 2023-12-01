package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.ifExperimental
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.annotation.UnsyncableEntry
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment

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
            this.add("net.frozenblock.lib.config.api.instance.ConfigModification")
            this.add("net.minecraft.core.*")
            this.add("net.minecraft.core.registries.*")
            this.add("net.minecraft.resources.ResourceKey")
            this.add("net.minecraft.resources.ResourceLocation")
            this.add("net.minecraft.world.level.dimension.DimensionType")
        }
    },

    @JvmField
    @UnsyncableEntry
    var remapping: Boolean? = true,

    @JvmField
    @UnsyncableEntry
    var filter: FilterOption? = FilterOption.INCLUDED,

    @JvmField
    @UnsyncableEntry
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
