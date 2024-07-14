package net.frozenblock.configurableeverything.config

import com.mojang.serialization.Codec
import net.frozenblock.configurableeverything.loot.util.LootModification
import net.frozenblock.configurableeverything.util.*
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.xjs.XjsConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.annotation.EntrySyncData
import net.frozenblock.lib.config.api.sync.annotation.UnsyncableConfig
import net.minecraft.world.item.Items
import net.minecraft.world.level.storage.loot.BuiltInLootTables
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator

private val LOOT_MODIFICATIONS: TypedEntryType<MutableList<LootModification>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        LootModification.CODEC.mutListOf()
    )
)

data class LootConfig(
    @JvmField
    @EntrySyncData("lootModifications")
    var lootModifications: TypedEntry<MutableList<LootModification>> = TypedEntry.create(
        LOOT_MODIFICATIONS,
        mutableListOf(
            LootModification(
                BuiltInLootTables.ANCIENT_CITY,
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
) {
    companion object : CEConfig<LootConfig>(
        LootConfig::class,
        "loot"
    ) {

        init {
            ConfigRegistry.register(this)
        }

        @JvmStatic
        @JvmOverloads
        fun get(real: Boolean = false): LootConfig = if (real) this.instance() else this.config()
    }
}
