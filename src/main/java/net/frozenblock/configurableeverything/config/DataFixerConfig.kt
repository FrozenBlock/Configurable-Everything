package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.datafixer.util.DataFixEntry
import net.frozenblock.configurableeverything.datafixer.util.Fixer
import net.frozenblock.configurableeverything.datafixer.util.RegistryFixer
import net.frozenblock.configurableeverything.datafixer.util.SchemaEntry
import net.frozenblock.configurableeverything.util.CEConfig
import net.frozenblock.configurableeverything.util.CESimpleConfig
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.v2.entry.ConfigEntry
import net.frozenblock.lib.config.v2.entry.EntryType
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier

private val SCHEMA_ENTRY: EntryType<SchemaEntry> = EntryType.create(
    SchemaEntry.CODEC,
    SchemaEntry.STREAM_CODEC,
)

private val REGISTRY_FIXER: EntryType<RegistryFixer> = EntryType.create(
    RegistryFixer.CODEC,
    RegistryFixer.STREAM_CODEC,
)

object DataFixerConfig : CEConfig("datafixer") {
    @JvmField
    var overrideRealEntries: ConfigEntry<Boolean> = this.entry("overrideRealEntries", EntryType.BOOL, false,
        """
        Allows registry fixers (not schemas) to convert all IDs
        whether or not a valid entry exists
        By default, registry fixers will only run if the entry with the ID is missing.
        WARNING: THIS CAN POTENTIALLY CAUSE UNWANTED EFFECTS TO YOUR WORLDS, USE WITH CAUTION
        """.trimIndent()
    )

    @JvmField
    var dataVersion: ConfigEntry<Int> = this.unsyncableEntry("dataVersion", EntryType.INT, 0,
        """
        The data fixer's main data version. Increment this when you add a new schema.
        Any schemas with a data version higher than this will be ignored.
        """.trimIndent()
    )

    @JvmField
    var schemas: ConfigEntry<MutableList<SchemaEntry>> = this.unsyncableEntry("schemas",
        SCHEMA_ENTRY.asList(),
        mutableListOf(
            SchemaEntry(
                1,
                mutableListOf(
                    DataFixEntry(
                        "biome",
                        mutableListOf(
                            Fixer(
                                Identifier.parse("examplemod:example_biome"),
                                Identifier.parse("minecraft:forest")
                            )
                        )
                    ),
                    DataFixEntry(
                        "block",
                        mutableListOf(
                            Fixer(
                                Identifier.parse("examplemod:dark_stone"),
                                Identifier.parse("minecraft:deepslate")
                            )
                        )
                    ),
                    DataFixEntry(
                        "entity",
                        mutableListOf(
                            Fixer(
                                Identifier.parse("examplemod:example_entity"),
                                Identifier.parse("minecraft:cow")
                            )
                        )
                    ),
                    DataFixEntry(
                        "item",
                        mutableListOf(
                            Fixer(
                                Identifier.parse("examplemod:example_item"),
                                Identifier.parse("minecraft:stone")
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
                                Identifier.parse("examplemod:old_block"),
                                Identifier.parse("minecraft:grass_block")
                            )
                        )
                    )
                )
            )
        ),
        """
        The list of schemas to use for data fixing.
        Each schema has a data version and a list of data fix entries.
        Each data fix entry has a type and a list of fixers.
        The four types are "biome", "block", "entity", and "item".
        Although, it is recommended to use a registry fixer for items instead of a schema fixer.
        Each fixer contains an old id and a new id, and will replace all instances of the old id with the new id.
        However, if the old id is still found in the registry, it will not be replaced.
        """.trimIndent()
    )

    @JvmField
    var registryFixers: ConfigEntry<MutableList<RegistryFixer>> = this.entry("registryFixers",
        REGISTRY_FIXER.asList(),
        mutableListOf(
            RegistryFixer(
                Registries.BLOCK.identifier(),
                mutableListOf(
                    Fixer(
                        Identifier.parse("examplemod:example_block"),
                        Identifier.parse("minecraft:stone")
                    )
                )
            ),
            RegistryFixer(
                Registries.ENTITY_TYPE.identifier(),
                mutableListOf(
                    Fixer(
                        Identifier.parse("examplemod:example_entity"),
                        Identifier.parse("minecraft:cow")
                    )
                )
            ),
            RegistryFixer(
                Registries.ITEM.identifier(),
                mutableListOf(
                    Fixer(
                        Identifier.parse("examplemod:example_item"),
                        Identifier.parse("minecraft:stone")
                    )
                )
            )
        ),
        """
        The list of registry fixers to use for data fixing.
        Each registry fixer contains a registry key and a list of fixers.
        Each fixer contains an old id and a new id, and will replace all instances of the old id with the new id.
        However, if the old id is still found in the registry, it will not be replaced (unless the overrideRealEntries option is set to true).
        """.trimIndent()
    )
}
