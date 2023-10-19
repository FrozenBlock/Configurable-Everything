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
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items

data class ItemConfig(
    @JvmField
    var reachOverrides: TypedEntry<List<ItemReachOverride?>>? = TypedEntry(
        ITEM_REACH_OVERRIDES,
        listOf(
            ItemReachOverride(
                BuiltInRegistries.ITEM.getKey(Items.TRIDENT),
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
        val INSTANCE: Config<ItemConfig> = ConfigRegistry.register(
            JsonConfig(
                MOD_ID,
                ItemConfig::class.java,
                makeConfigPath("item"),
                CONFIG_JSONTYPE
            )
        )

        @JvmStatic
        fun get(real: Boolean = false): ItemConfig = if (real) INSTANCE.instance() else INSTANCE.config()
    }
}
