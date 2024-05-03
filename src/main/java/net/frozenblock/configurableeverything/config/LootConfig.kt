package net.frozenblock.configurableeverything.config

import com.mojang.serialization.Codec
import net.frozenblock.configurableeverything.loot.util.LootModification
import net.frozenblock.configurableeverything.util.CONFIG_FORMAT
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
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

private val LOOT_MODIFICATIONS: TypedEntryType<List<LootModification>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        Codec.list(LootModification.CODEC)
    )
)

@UnsyncableConfig
data class LootConfig(
    @JvmField
    @EntrySyncData("lootModifications")
    var lootModifications: TypedEntry<List<LootModification>> = TypedEntry.create(
        LOOT_MODIFICATIONS,
        listOf(
            LootModification(
                BuiltInLootTables.ANCIENT_CITY,
                LootPool.lootPool()
                    .add(
                        LootItem.lootTableItem(Items.DIAMOND_BLOCK)
                            .setWeight(10)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1F, 64F)))
                    )
                    .build(),
                listOf()
            )
        )
    )
) {
    companion object : XjsConfig<LootConfig>(
        MOD_ID,
        LootConfig::class.java,
        makeConfigPath("loot"),
        CONFIG_FORMAT
    ) {

        init {
            ConfigRegistry.register(this)
        }

        @JvmStatic
        @JvmOverloads
        fun get(real: Boolean = false): LootConfig = if (real) this.instance() else this.config()
    }
}
