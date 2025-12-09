package net.frozenblock.configurableeverything.loot.util

import net.fabricmc.fabric.api.loot.v3.LootTableEvents
import net.frozenblock.configurableeverything.config.LootConfig
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.value
import net.minecraft.resources.Identifier
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem

internal object LootConfigUtil {

    internal fun init() {
        LootTableEvents.MODIFY.register { key, tableBuilder, _, _ ->
            if (!MainConfig.get().loot) return@register
            val id = key.identifier()
            val mods = LootConfig.get().lootModifications.value
            for (mod in mods) {
                if (mod.id != id) continue
                val pool = mod.pool
                val removals = mod.removals
                if (removals != null) {
                    tableBuilder.removeItems(removals)
                }

                if (pool != null) tableBuilder.withPool(pool)
            }
        }
    }
}

private fun LootTable.Builder.withPool(pool: LootPool): LootTable.Builder {
    this.pools.add(pool)
    return this
}

private fun LootTable.Builder.removeItems(items: Iterable<Identifier?>) {
    for (pool in this.pools.build()) {
        for (entry in pool.entries) {
            if (entry is LootItem) {
                val location = entry.item.unwrapKey().orElseThrow().identifier()
                if (items.any { it == location })
                    entry.disable()
            }
        }
    }
}

@Suppress("NOTHING_TO_INLINE")
private inline fun LootItem.disable() = (this as ConfigurableLootItem).`configurableEverything$disable`()
