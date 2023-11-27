@file:Environment(EnvType.CLIENT)

package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.clothconfig2.api.Requirement
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry
import me.shedaniel.clothconfig2.gui.entries.IntegerListEntry
import me.shedaniel.clothconfig2.gui.entries.StringListEntry
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.DataFixerConfig
import net.frozenblock.configurableeverything.datafixer.util.DataFixEntry
import net.frozenblock.configurableeverything.datafixer.util.Fixer
import net.frozenblock.configurableeverything.datafixer.util.RegistryFixer
import net.frozenblock.configurableeverything.datafixer.util.SchemaEntry
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.configurableeverything.util.tooltip
import net.frozenblock.lib.config.api.client.gui.EntryBuilder
import net.frozenblock.lib.config.api.client.gui.multiElementEntry
import net.frozenblock.lib.config.api.client.gui.nestedList
import net.frozenblock.lib.config.api.client.gui.typedEntryList
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation

@Environment(EnvType.CLIENT)
object DataFixerConfigGui {
    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        val config = DataFixerConfig.get(real = true)
        val defaultConfig = DataFixerConfig.defaultInstance()
        category.background = id("textures/config/datafixer.png")

        val overrideRealEntries = EntryBuilder(
            text("override_real_entries"),
            config.overrideRealEntries,
            defaultConfig.overrideRealEntries!!,
            { newValue -> config.overrideRealEntries = newValue },
            tooltip("override_real_entries"),
            true
        ).build(entryBuilder).apply { category.addEntry(this) }

        val dataVersion = EntryBuilder(
            text("data_version"),
            config.dataVersion,
            defaultConfig.dataVersion!!,
            { newValue -> config.dataVersion = newValue },
            tooltip("data_version"),
            true
        ).build(entryBuilder).apply { category.addEntry(this) }

        category.addEntry(schemas(entryBuilder, config, defaultConfig, dataVersion as IntegerListEntry))
        category.addEntry(registryFixers(entryBuilder, config, defaultConfig))
    }
}

private fun schemas(
    entryBuilder: ConfigEntryBuilder,
    config: DataFixerConfig,
    defaultConfig: DataFixerConfig,
    dataVersion: IntegerListEntry
): AbstractConfigListEntry<*> {
    val defaultFixEntryList = listOf(
        DataFixEntry(
            "biome",
            listOf(
                Fixer(
                    ResourceLocation("example:example"),
                    ResourceLocation("minecraft:forest")
                )
            )
        ),
        DataFixEntry(
            "block",
            listOf(
                Fixer(
                    ResourceLocation("example:example"),
                    ResourceLocation("minecraft:deepslate")
                )
            )
        ),
        DataFixEntry(
            "entity",
            listOf(
                Fixer(
                    ResourceLocation("example:example"),
                    ResourceLocation("minecraft:cow")
                )
            )
        ),
        DataFixEntry(
            "item",
            listOf(
                Fixer(
                    ResourceLocation("example:example"),
                    ResourceLocation("minecraft:stone")
                )
            )
        )
    )
    val defaultSchema = SchemaEntry(1, defaultFixEntryList)
    return typedEntryList(
        entryBuilder,
        text("schemas"),
        config::schemas,
        { defaultConfig.schemas!! },
        false,
        tooltip("schemas"),
        { newValue -> config.schemas = newValue },
        { element, _ ->
            val schema = element ?: defaultSchema
            lateinit var versionEntry: IntegerListEntry
            multiElementEntry(
                text("schemas.schema"),
                schema,
                true,

                EntryBuilder(
                    text("schemas.version"),
                    schema.version,
                    0,
                    { newValue -> schema.version = newValue },
                    tooltip("schemas.version")
                ).build(entryBuilder).apply { versionEntry = this as IntegerListEntry },

                nestedList(
                    entryBuilder,
                    text("schemas.entries"),
                    schema::entries,
                    { defaultFixEntryList },
                    true,
                    tooltip("schemas.entries"),
                    { newValue -> schema.entries = newValue },
                    { entry, _ ->
                        multiElementEntry(
                            text("schemas.entry"),
                            entry,
                            true,

                            EntryBuilder(
                                text("schemas.type"),
                                entry.type,
                                "",
                                { newValue -> entry.type = newValue },
                                tooltip("schemas.type")
                            ).build(entryBuilder),

                            nestedList(
                                entryBuilder,
                                text("datafixer.fixers"),
                                entry::fixers,
                                { listOf(Fixer(ResourceLocation(""), ResourceLocation(""))) },
                                true,
                                tooltip("datafixer.fixers"),
                                { newValue -> entry.fixers = newValue },
                                { entry, _ ->
                                    multiElementEntry(
                                        text("datafixer.fixer"),
                                        entry,
                                        true,

                                        EntryBuilder(
                                            text("datafixer.old_id"),
                                            entry.oldId.toString(),
                                            "",
                                            { newValue -> entry.oldId = ResourceLocation(newValue) },
                                            tooltip("datafixer.old_id")
                                        ).build(entryBuilder),

                                        EntryBuilder(
                                            text("datafixer.new_id"),
                                            entry.newId.toString(),
                                            "",
                                            { newValue -> entry.newId = ResourceLocation(newValue) },
                                            tooltip("datafixer.new_id")
                                        ).build(entryBuilder)
                                    )
                                }
                            )
                        )
                    }
                ).apply {
                    this.requirement = Requirement.isTrue {
                        val globalVersion: Int? = dataVersion.value
                        val localVersion: Int? = versionEntry.value
                        globalVersion != null && localVersion != null && localVersion > 0 && localVersion <= globalVersion
                    }
                }
            )
        }
    ).apply {
        this.requirement = Requirement.isTrue {
            val dataVersionInt: Int? = dataVersion.value
            dataVersionInt != null && dataVersionInt > 0
        }
    }
}

private fun registryFixers(
    entryBuilder: ConfigEntryBuilder,
    config: DataFixerConfig,
    defaultConfig: DataFixerConfig
): AbstractConfigListEntry<*> {
    val defaultFixers = listOf(
        Fixer(
            ResourceLocation("examplemod:example_block"),
            ResourceLocation("minecraft:stone")
        )
    )
    val defaultRegistryFixer = RegistryFixer(Registries.BLOCK.location(), defaultFixers)
    return typedEntryList(
        entryBuilder,
        text("registry_fixers"),
        config::registryFixers,
        { defaultConfig.registryFixers!! },
        false,
        tooltip("registry_fixers"),
        { newValue -> config.registryFixers = newValue },
        { element, _ ->
            val registryFixer = element ?: defaultRegistryFixer
            lateinit var registryKeyEntry: StringListEntry
            multiElementEntry(
                text("registry_fixers.registry_fixer"),
                registryFixer,
                true,

                EntryBuilder(
                    text("registry_fixers.registry_key"),
                    registryFixer.registryKey.toString(),
                    "",
                    { newValue -> registryFixer.registryKey = ResourceLocation(newValue) },
                    tooltip("registry_fixers.registry_key")
                ).build(entryBuilder).apply { registryKeyEntry = this as StringListEntry },

                nestedList(
                    entryBuilder,
                    text("datafixer.fixers"),
                    registryFixer::fixers,
                    { defaultFixers },
                    true,
                    tooltip("datafixer.fixers"),
                    { newValue -> registryFixer.fixers = newValue },
                    { element, _ ->
                        val entry = element ?: Fixer(ResourceLocation(""), ResourceLocation(""))
                        lateinit var oldIdEntry: StringListEntry
                        multiElementEntry(
                            text("datafixer.fixer"),
                            entry,
                            true,

                            EntryBuilder(
                                text("datafixer.old_id"),
                                entry.oldId.toString(),
                                "",
                                { newValue -> entry.oldId = ResourceLocation(newValue) },
                                tooltip("datafixer.old_id")
                            ).build(entryBuilder).apply {
                                oldIdEntry = this as StringListEntry
                            },

                            EntryBuilder(
                                text("datafixer.new_id"),
                                entry.newId.toString(),
                                "",
                                { newValue -> entry.newId = ResourceLocation(newValue) },
                                tooltip("datafixer.new_id"),
                                requirement = Requirement.none(Requirement.matches(oldIdEntry) { null }, Requirement.matches(oldIdEntry) { "" })
                            ).build(entryBuilder)
                        )
                    }
                )
            )
        }
    )
}
