@file:Environment(EnvType.CLIENT)
@file:Suppress("UnstableApiUsage", "COMPATIBILITY_WARNING")

package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.clothconfig2.api.Requirement
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
import net.frozenblock.lib.config.clothconfig.synced
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation

private val configInstance = DataFixerConfig

private inline val mainToggleReq: Requirement
    get() = Requirement.isTrue(MainConfigGui.INSTANCE!!.datafixer)

object DataFixerConfigGui {
    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        val config = configInstance.instance()
        val syncConfig = configInstance.configWithSync()
        val defaultConfig = configInstance.defaultInstance()
        category.background = id("textures/config/datafixer.png")

        val overrideRealEntries = EntryBuilder(
            text("override_real_entries"),
            syncConfig.overrideRealEntries,
            defaultConfig.overrideRealEntries,
            { newValue -> config.overrideRealEntries = newValue },
            tooltip("override_real_entries"),
            true,
            requirement = mainToggleReq
        ).build(entryBuilder).synced(
            config::class,
            "overrideRealEntries",
            configInstance
        ).apply { category.addEntry(this) }

        val dataVersion = EntryBuilder(
            text("data_version"),
            config.dataVersion,
            defaultConfig.dataVersion,
            { newValue -> config.dataVersion = newValue },
            tooltip("data_version"),
            true,
            requirement = mainToggleReq
        ).build(entryBuilder).apply { category.addEntry(this) }

        category.addEntry(schemas(entryBuilder, config, defaultConfig, dataVersion as IntegerListEntry))
        category.addEntry(registryFixers(entryBuilder, config, syncConfig, defaultConfig))
    }
}

private fun schemas(
    entryBuilder: ConfigEntryBuilder,
    config: DataFixerConfig,
    defaultConfig: DataFixerConfig,
    dataVersion: IntegerListEntry
): AbstractConfigListEntry<*> {
    val defaultFixEntryList = mutableListOf(
        DataFixEntry(
            "biome",
            mutableListOf(
                Fixer(
                    ResourceLocation.parse("example:example"),
                    ResourceLocation.parse("minecraft:forest")
                )
            )
        ),
        DataFixEntry(
            "block",
            mutableListOf(
                Fixer(
                    ResourceLocation.parse("example:example"),
                    ResourceLocation.parse("minecraft:deepslate")
                )
            )
        ),
        DataFixEntry(
            "entity",
            mutableListOf(
                Fixer(
                    ResourceLocation.parse("example:example"),
                    ResourceLocation.parse("minecraft:cow")
                )
            )
        ),
        DataFixEntry(
            "item",
            mutableListOf(
                Fixer(
                    ResourceLocation.parse("example:example"),
                    ResourceLocation.parse("minecraft:stone")
                )
            )
        )
    )
    val defaultSchema = SchemaEntry(1, defaultFixEntryList)
    return typedEntryList(
        entryBuilder,
        text("schemas"),
        config::schemas,
        { defaultConfig.schemas },
        false,
        tooltip("schemas"),
        { newValue -> config.schemas = newValue },
        { element: SchemaEntry?, _ ->
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
                                { mutableListOf(Fixer(ResourceLocation.withDefaultNamespace(""), ResourceLocation.withDefaultNamespace(""))) },
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
                                            { newValue -> entry.oldId = ResourceLocation.parse(newValue) },
                                            tooltip("datafixer.old_id")
                                        ).build(entryBuilder),

                                        EntryBuilder(
                                            text("datafixer.new_id"),
                                            entry.newId.toString(),
                                            "",
                                            { newValue -> entry.newId = ResourceLocation.parse(newValue) },
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
        this.requirement = Requirement.all(
            mainToggleReq,
            Requirement.isTrue {
                val dataVersionInt: Int? = dataVersion.value
                dataVersionInt != null && dataVersionInt > 0
            }
        )
    }
}

private fun registryFixers(
    entryBuilder: ConfigEntryBuilder,
    config: DataFixerConfig,
    syncConfig: DataFixerConfig,
    defaultConfig: DataFixerConfig
): AbstractConfigListEntry<*> {
    val defaultFixers = mutableListOf(
        Fixer(
            ResourceLocation.parse("examplemod:example_block"),
            ResourceLocation.parse("minecraft:stone")
        )
    )
    val defaultRegistryFixer = RegistryFixer(Registries.BLOCK.location(), defaultFixers)
    return typedEntryList(
        entryBuilder,
        text("registry_fixers"),
        syncConfig::registryFixers,
        { defaultConfig.registryFixers },
        false,
        tooltip("registry_fixers"),
        { newValue -> config.registryFixers = newValue },
        { element: RegistryFixer?, _ ->
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
                    { newValue -> registryFixer.registryKey = ResourceLocation.parse(newValue) },
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
                    { element: Fixer?, _ ->
                        val entry = element ?: Fixer(ResourceLocation.withDefaultNamespace(""), ResourceLocation.withDefaultNamespace(""))
                        lateinit var oldIdEntry: StringListEntry
                        multiElementEntry(
                            text("datafixer.fixer"),
                            entry,
                            true,

                            EntryBuilder(
                                text("datafixer.old_id"),
                                entry.oldId.toString(),
                                "",
                                { newValue -> entry.oldId = ResourceLocation.parse(newValue) },
                                tooltip("datafixer.old_id")
                            ).build(entryBuilder).apply {
                                oldIdEntry = this as StringListEntry
                            },

                            EntryBuilder(
                                text("datafixer.new_id"),
                                entry.newId.toString(),
                                "",
                                { newValue -> entry.newId = ResourceLocation.parse(newValue) },
                                tooltip("datafixer.new_id"),
                                requirement = Requirement.none(Requirement.matches(oldIdEntry) { null }, Requirement.matches(oldIdEntry) { "" })
                            ).build(entryBuilder)
                        )
                    }
                )
            )
        }
    ).apply {
        this.requirement = mainToggleReq
    }.synced(
        config::class,
        "registryFixers",
        configInstance
    )
}
