package net.frozenblock.configurableeverything.loot.util

import net.fabricmc.fabric.api.loot.v2.LootTableEvents
import net.frozenblock.configurableeverything.config.LootConfig
import net.frozenblock.configurableeverything.config.MainConfig
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem

internal object LootConfigUtil {

    internal fun init() {
        LootTableEvents.MODIFY.register { resourceManager, lootManager, id, tableBuilder, source ->
            if (MainConfig.get().loot != true) return@register
            val mods = LootConfig.get().lootModifications?.value ?: return@register
            for (mod in mods) {
                if (mod == null || mod.id != id) continue
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

private fun LootTable.Builder.removeItems(items: Iterable<ResourceLocation?>) {
    for (pool in this.pools.build()) {
        for (entry in pool.entries) {
            if (entry is LootItem) {
                val location = entry.item.unwrapKey().orElseThrow().location()
                if (items.any { it == location })
                    (entry as ConfigurableLootItem).`configurableEverything$disable`()
            }
        }
    }
}
