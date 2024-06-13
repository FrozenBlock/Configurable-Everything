package net.frozenblock.configurableeverything.config

import com.mojang.serialization.Codec
import net.frozenblock.configurableeverything.datafixer.util.DataFixEntry
import net.frozenblock.configurableeverything.datafixer.util.Fixer
import net.frozenblock.configurableeverything.datafixer.util.RegistryFixer
import net.frozenblock.configurableeverything.datafixer.util.SchemaEntry
import net.frozenblock.configurableeverything.util.CEConfig
import net.frozenblock.configurableeverything.util.CONFIG_FORMAT
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.configurableeverything.util.mutListOf
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

private val SCHEMA_ENTRY_LIST: TypedEntryType<MutableList<SchemaEntry>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        SchemaEntry.CODEC.mutListOf()
    )
)

private val REGISTRY_FIXER_LIST: TypedEntryType<MutableList<RegistryFixer>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        RegistryFixer.CODEC.mutListOf()
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
    var schemas: TypedEntry<MutableList<SchemaEntry>> = TypedEntry.create(
        SCHEMA_ENTRY_LIST,
        mutableListOf(
            SchemaEntry(
                1,
                mutableListOf(
                    DataFixEntry(
                        "biome",
                        mutableListOf(
                            Fixer(
                                ResourceLocation.parse("examplemod:example_biome"),
                                ResourceLocation.parse("minecraft:forest")
                            )
                        )
                    ),
                    DataFixEntry(
                        "block",
                        mutableListOf(
                            Fixer(
                                ResourceLocation.parse("examplemod:dark_stone"),
                                ResourceLocation.parse("minecraft:deepslate")
                            )
                        )
                    ),
                    DataFixEntry(
                        "entity",
                        mutableListOf(
                            Fixer(
                                ResourceLocation.parse("examplemod:example_entity"),
                                ResourceLocation.parse("minecraft:cow")
                            )
                        )
                    ),
                    DataFixEntry(
                        "item",
                        mutableListOf(
                            Fixer(
                                ResourceLocation.parse("examplemod:example_item"),
                                ResourceLocation.parse("minecraft:stone")
                            )
                        )
                    )
                )
            ),
            SchemaEntry(
                2,
                mutableListOf(
                    DataFixEntry(
                        "block",
                        mutableListOf(
                            Fixer(
                                ResourceLocation.parse("examplemod:old_block"),
                                ResourceLocation.parse("minecraft:grass_block")
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
    var registryFixers: TypedEntry<MutableList<RegistryFixer>> = TypedEntry.create(
        REGISTRY_FIXER_LIST,
        mutableListOf(
            RegistryFixer(
                Registries.BLOCK.location(),
                mutableListOf(
                    Fixer(
                        ResourceLocation.parse("examplemod:example_block"),
                        ResourceLocation.parse("minecraft:stone")
                    )
                )
            ),
            RegistryFixer(
                Registries.ENTITY_TYPE.location(),
                mutableListOf(
                    Fixer(
                        ResourceLocation.parse("examplemod:example_entity"),
                        ResourceLocation.parse("minecraft:cow")
                    )
                )
            ),
            RegistryFixer(
                Registries.ITEM.location(),
                mutableListOf(
                    Fixer(
                        ResourceLocation.parse("examplemod:example_item"),
                        ResourceLocation.parse("minecraft:stone")
                    )
                )
            )
        )
)
) {
    companion object : CEConfig<DataFixerConfig>(
        DataFixerConfig::class,
        "datafixer"
    ) {

        init {
            ConfigRegistry.register(this)
        }

        @JvmStatic
        @JvmOverloads
        fun get(real: Boolean = false): DataFixerConfig = if (real) this.instance() else this.config()
    }
}
