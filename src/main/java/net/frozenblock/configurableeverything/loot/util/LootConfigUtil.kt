package net.frozenblock.configurableeverything.loot.util

import net.fabricmc.fabric.api.loot.v2.LootTableEvents
import net.frozenblock.configurableeverything.config.LootConfig
import net.frozenblock.configurableeverything.config.MainConfig

internal object LootConfigUtil {

    internal fun init() {
        LootTableEvents.MODIFY.register { resourceManager, lootManager, id, tableBuilder, source ->
            if (MainConfig.get().loot != true) return@register
            val mods = LootConfig.get().lootModifications?.value ?: return@register
            for (mod in mods) {
                if (mod == null || mod.id != id) continue
                val pool = mod.pool

                if (pool != null) tableBuilder.withPool(pool)
            }
        }
    }
}
