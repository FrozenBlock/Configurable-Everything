package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.loot.util.LootModification
import net.frozenblock.configurableeverything.util.CEConfig
import net.frozenblock.configurableeverything.util.mutListOf
import net.frozenblock.lib.config.v2.entry.ConfigEntry
import net.frozenblock.lib.config.v2.entry.EntryType
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.world.item.Items
import net.minecraft.world.level.storage.loot.BuiltInLootTables
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator

private val LOOT_MODIFICATIONS: EntryType<MutableList<LootModification>> = EntryType.create(
    LootModification.CODEC.mutListOf(),
    LootModification.STREAM_CODEC.apply(ByteBufCodecs.list())
)

object LootConfig : CEConfig("loot") {
    @JvmField
    var lootModifications: ConfigEntry<MutableList<LootModification>> = this.entry("lootModifications",
        LOOT_MODIFICATIONS,
        mutableListOf(
            LootModification(
                BuiltInLootTables.ANCIENT_CITY.identifier(),
                LootPool.lootPool()
                    .add(
                        LootItem.lootTableItem(Items.DIAMOND_BLOCK)
                            .setWeight(10)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1F, 64F)))
                    )
                    .build(),
                mutableListOf()
            )
        )
    )
}
