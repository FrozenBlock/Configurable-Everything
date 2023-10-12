package net.frozenblock.configurableeverything.config

import com.mojang.serialization.Codec
import net.frozenblock.configurableeverything.item.util.ItemReachOverride
import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.minecraft.resources.ResourceLocation

data class ItemConfig(
    @JvmField
    var itemReachOverrides: TypedEntry<List<ItemReachOverride?>>? = TypedEntry(
        ITEM_REACH_OVERRIDES,
        listOf(
            ItemReachOverride(
                ResourceLocation("minecraft:trident"),
                100.0
            )
        )
    )
) {
    companion object {
        private val ITEM_REACH_OVERRIDES: TypedEntryType<List<ItemReachOverride?>> = ConfigRegistry.register(
            TypedEntryType(
                MOD_ID,
                Codec.list(ItemReachOverride.CODEC)
            )
        )

        @JvmField
        internal val INSTANCE: Config<ItemConfig> = ConfigRegistry.register(
            JsonConfig(
                MOD_ID,
                ItemConfig::class.java,
                makeConfigPath("item"),
                CONFIG_JSONTYPE
            )
        )

        @JvmStatic
        fun get(): ItemConfig = INSTANCE.config()
    }
}
