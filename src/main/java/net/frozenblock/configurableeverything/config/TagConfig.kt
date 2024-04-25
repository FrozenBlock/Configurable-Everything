package net.frozenblock.configurableeverything.config

import com.mojang.serialization.Codec
import net.frozenblock.configurableeverything.tag.util.RegistryTagModification
import net.frozenblock.configurableeverything.tag.util.TagModification
import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.annotation.EntrySyncData
import net.frozenblock.lib.config.api.sync.annotation.UnsyncableConfig
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Items
import net.minecraft.world.level.storage.loot.BuiltInLootTables
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator

private val TAG_MODIFICATIONS: TypedEntryType<List<RegistryTagModification>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        Codec.list(RegistryTagModification.CODEC)
    )
)

@UnsyncableConfig
data class TagConfig(
    @JvmField
    @EntrySyncData("lootModifications")
    var tagModifications: TypedEntry<List<RegistryTagModification>> = TypedEntry(
        TAG_MODIFICATIONS,
        listOf(
            RegistryTagModification(
                BuiltInRegistries.BLOCK.key().location().toString(),
                listOf(
                    TagModification(
                        "minecraft:piglin_loved",
                        listOf(
                            "diamond_block"
                        ),
                        listOf(
                            "gold_ingot"
                        )
                    )
                )
            )
        )
    )
) {
    companion object : JsonConfig<TagConfig>(
        MOD_ID,
        TagConfig::class.java,
        makeConfigPath("tag"),
        CONFIG_JSONTYPE,
        null,
        null
    ) {

        init {
            ConfigRegistry.register(this)
        }

        @JvmStatic
        @JvmOverloads
        fun get(real: Boolean = false): TagConfig = if (real) this.instance() else this.config()
    }
}
