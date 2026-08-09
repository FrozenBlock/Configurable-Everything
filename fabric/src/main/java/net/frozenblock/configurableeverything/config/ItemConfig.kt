package net.frozenblock.configurableeverything.config

import com.mojang.serialization.Codec
import net.frozenblock.configurableeverything.item.util.ItemReachOverride
import net.frozenblock.configurableeverything.util.*
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.xjs.XjsConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.v2.entry.ConfigEntry
import net.frozenblock.lib.config.v2.entry.EntryType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Items

private val ITEM_REACH_OVERRIDES: EntryType<ItemReachOverride> = EntryType.create(
    ItemReachOverride.CODEC,
    ItemReachOverride.STREAM_CODEC,
)

object ItemConfig : CEConfig("item") {
    @JvmField
    var reachOverrides: ConfigEntry<MutableList<ItemReachOverride>> = this.entry("reachOverrides",
        ITEM_REACH_OVERRIDES.asList(),
        mutableListOf(
            ItemReachOverride(
                BuiltInRegistries.ITEM.getKey(Items.TRIDENT),
                100.0
            )
        )
    )
}
