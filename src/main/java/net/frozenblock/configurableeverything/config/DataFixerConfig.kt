package net.frozenblock.configurableeverything.config

import com.mojang.serialization.Codec
import net.frozenblock.configurableeverything.datafixer.util.DataFixEntry
import net.frozenblock.configurableeverything.datafixer.util.Fixer
import net.frozenblock.configurableeverything.datafixer.util.RegistryFixer
import net.frozenblock.configurableeverything.datafixer.util.SchemaEntry
import net.frozenblock.configurableeverything.util.CONFIG_FORMAT
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.xjs.XjsConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.SyncBehavior
import net.frozenblock.lib.config.api.sync.annotation.EntrySyncData
import net.frozenblock.lib.config.api.sync.annotation.UnsyncableConfig
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation

private val SCHEMA_ENTRY_LIST: TypedEntryType<List<SchemaEntry>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        Codec.list(SchemaEntry.CODEC)
    )
)

private val REGISTRY_FIXER_LIST: TypedEntryType<List<RegistryFixer>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        Codec.list(RegistryFixer.CODEC)
    )
)

@UnsyncableConfig
data class DataFixerConfig(
    @JvmField
    @EntrySyncData("overrideRealEntries")
    @Comment(
"""
Allows registry fixers (not schemas) to convert all IDs
whether or not a valid entry exists
By default, registry fixers will only run if the entry with the ID is missing.
WARNING: THIS CAN POTENTIALLY CAUSE UNWANTED EFFECTS TO YOUR WORLDS, USE WITH CAUTION
"""
    )
    var overrideRealEntries: Boolean = false,

    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    @Comment(
"""
The data fixer's main data version. Increment this when you add a new schema.
Any schemas with a data version higher than this will be ignored.
"""
    )
    var dataVersion: Int = 0,

    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    @Comment(
"""
The list of schemas to use for data fixing.
Each schema has a data version and a list of data fix entries.
Each data fix entry has a type and a list of fixers.
The four types are "biome", "block", "entity", and "item".
Although, it is recommended to use a registry fixer for items instead of a schema fixer.
Each fixer contains an old id and a new id, and will replace all instances of the old id with the new id.
However, if the old id is still found in the registry, it will not be replaced.
"""
    )
    var schemas: TypedEntry<List<SchemaEntry>> = TypedEntry.create(
        SCHEMA_ENTRY_LIST,
        listOf(
            SchemaEntry(
                1,
                listOf(
                    DataFixEntry(
                        "biome",
                        listOf(
                            Fixer(
                                ResourceLocation("examplemod:example_biome"),
                                ResourceLocation("minecraft:forest")
                            )
                        )
                    ),
                    DataFixEntry(
                        "block",
                        listOf(
                            Fixer(
                                ResourceLocation("examplemod:dark_stone"),
                                ResourceLocation("minecraft:deepslate")
                            )
                        )
                    ),
                    DataFixEntry(
                        "entity",
                        listOf(
                            Fixer(
                                ResourceLocation("examplemod:example_entity"),
                                ResourceLocation("minecraft:cow")
                            )
                        )
                    ),
                    DataFixEntry(
                        "item",
                        listOf(
                            Fixer(
                                ResourceLocation("examplemod:example_item"),
                                ResourceLocation("minecraft:stone")
                            )
                        )
                    )
                )
            ),
            SchemaEntry(
                2,
                listOf(
                    DataFixEntry(
                        "block",
                        listOf(
                            Fixer(
                                ResourceLocation("examplemod:old_block"),
                                ResourceLocation("minecraft:grass_block")
                            )
                        )
                    )
                )
            )
        )
    ),

    @JvmField
    @EntrySyncData("registryFixers")
    @Comment(
"""
The list of registry fixers to use for data fixing.
Each registry fixer contains a registry key and a list of fixers.
Each fixer contains an old id and a new id, and will replace all instances of the old id with the new id.
However, if the old id is still found in the registry, it will not be replaced (unless the overrideRealEntries option is set to true).
"""
    )
    var registryFixers: TypedEntry<List<RegistryFixer>> = TypedEntry.create(
        REGISTRY_FIXER_LIST,
        listOf(
            RegistryFixer(
                Registries.BLOCK.location(),
                listOf(
                    Fixer(
                        ResourceLocation("examplemod:example_block"),
                        ResourceLocation("minecraft:stone")
                    )
                )
            ),
            RegistryFixer(
                Registries.ENTITY_TYPE.location(),
                listOf(
                    Fixer(
                        ResourceLocation("examplemod:example_entity"),
                        ResourceLocation("minecraft:cow")
                    )
                )
            ),
            RegistryFixer(
                Registries.ITEM.location(),
                listOf(
                    Fixer(
                        ResourceLocation("examplemod:example_item"),
                        ResourceLocation("minecraft:stone")
                    )
                )
            )
        )
)
) {
    companion object : XjsConfig<DataFixerConfig>(
        MOD_ID,
        DataFixerConfig::class.java,
        makeConfigPath("datafixer"),
        CONFIG_FORMAT
    ) {

        init {
            ConfigRegistry.register(this)
        }

        @JvmStatic
        @JvmOverloads
        fun get(real: Boolean = false): DataFixerConfig = if (real) this.instance() else this.config()
    }
}
