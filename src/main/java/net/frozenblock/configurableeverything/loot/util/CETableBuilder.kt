package net.frozenblock.configurableeverything.loot.util

import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable

interface CETableBuilder {
    fun `configurable_Everything$withPool`(pool: LootPool): LootTable.Builder
}

@Suppress("NOTHING_TO_INLINE")
inline fun LootTable.Builder.withPool(pool: LootPool): LootTable.Builder
    = (this as CETableBuilder).`configurable_Everything$withPool`(pool)
