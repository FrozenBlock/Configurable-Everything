package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.entity.util.*
import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

class BlockConfig {
    companion object {

        @JvmField
        internal val INSTANCE: Config<BlockConfig> = ConfigRegistry.register(
            JsonConfig(
                MOD_ID,
                BlockConfig::class.java,
                makeConfigPath("block"),
                CONFIG_JSONTYPE
            )
        )

        @JvmStatic
        fun get(): BlockConfig = INSTANCE.config()
    }

    @JvmField
    var makeBlocksCool_insert_sunglasses_emoji_here: Boolean? = false
}
