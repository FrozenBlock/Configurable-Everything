package net.frozenblock.configurableeverything.config

import com.mojang.serialization.Codec
import net.frozenblock.configurableeverything.item.util.ItemReachOverride
import net.frozenblock.configurableeverything.util.CEConfig
import net.frozenblock.configurableeverything.util.CONFIG_FORMAT
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.xjs.XjsConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.annotation.EntrySyncData
import net.frozenblock.lib.config.api.sync.annotation.UnsyncableConfig
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Items

private val ITEM_REACH_OVERRIDES: TypedEntryType<MutableList<ItemReachOverride>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        Codec.list(ItemReachOverride.CODEC)
    )
)

@UnsyncableConfig
data class ItemConfig(
    @JvmField
    @EntrySyncData("reachOverrides")
    var reachOverrides: TypedEntry<MutableList<ItemReachOverride>> = TypedEntry.create(
        ITEM_REACH_OVERRIDES,
        mutableListOf(
            ItemReachOverride(
                BuiltInRegistries.ITEM.getKey(Items.TRIDENT),
                100.0
            )
        )
    )
) {
    companion object : CEConfig<ItemConfig>(
        ItemConfig::class,
        "item"
    ) {

        init {
            ConfigRegistry.register(this)
        }

        @JvmStatic
        @JvmOverloads
        fun get(real: Boolean = false): ItemConfig = if (real) this.instance() else this.config()
    }
}
