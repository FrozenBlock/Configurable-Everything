package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.DataFixerConfig
import net.frozenblock.configurableeverything.config.gui.api.EntryBuilder
import net.frozenblock.configurableeverything.config.gui.api.TypedEntryUtils
import net.frozenblock.configurableeverything.datafixer.util.DataFixEntry
import net.frozenblock.configurableeverything.datafixer.util.Fixer
import net.frozenblock.configurableeverything.datafixer.util.RegistryFixer
import net.frozenblock.configurableeverything.datafixer.util.SchemaEntry
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.configurableeverything.util.tooltip
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation

@Environment(EnvType.CLIENT)
object DataFixerConfigGui {
    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        val config = DataFixerConfig.get()
        val defaultConfig = DataFixerConfig.getConfigInstance().defaultInstance()
        category.background = id("textures/config/datafixer.png")

        category.addEntry(EntryBuilder(text("override_real_entries"), config.overrideRealEntries,
            defaultConfig.overrideRealEntries,
            { newValue -> config.overrideRealEntries = newValue },
            tooltip("overrideRealEntries"),
            true
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("data_version"), config.dataVersion,
            defaultConfig.dataVersion,
            { newValue -> config.dataVersion = newValue },
            tooltip("dataVersion"),
            true
        ).build(entryBuilder))

        category.addEntry(schemas(entryBuilder, config, defaultConfig))
        category.addEntry(registryFixers(entryBuilder, config, defaultConfig))
    }
}

private fun schemas(
    entryBuilder: ConfigEntryBuilder,
    config: DataFixerConfig,
    defaultConfig: DataFixerConfig
): AbstractConfigListEntry<*> {
    val defaultFixEntryList = listOf(
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
    val defaultSchema = SchemaEntry(1, defaultFixEntryList)
    return TypedEntryUtils.makeTypedEntryList(
        entryBuilder,
        text("schemas"),
        config::schemas,
        defaultConfig::schemas,
        false,
        tooltip("schemas"),
        { newValue -> config.schemas = newValue},
        { element, _ ->
            val schema = element ?: defaultSchema
            TypedEntryUtils.makeMultiElementEntry(
                text("schemas.schema"),
                schema,
                true,

                EntryBuilder(text("schemas.version"), schema.version,
                    0,
                    { newValue -> schema.version = newValue },
                    tooltip("schemas.version")
                ).build(entryBuilder),

                TypedEntryUtils.makeNestedList(
                    entryBuilder,
                    text("schemas.entries"),
                    schema::entries,
                    { defaultFixEntryList },
                    true,
                    tooltip("schemas.entries"),
                    { newValue -> schema.entries = newValue },
                    { entry, _ ->
                        TypedEntryUtils.makeMultiElementEntry(
                            text("schemas.entry"),
                            entry,
                            true,

                            EntryBuilder(text("schemas.type"), entry.type,
                                "",
                                { newValue -> entry.type = newValue },
                                tooltip("schemas.type")
                            ).build(entryBuilder),

                            TypedEntryUtils.makeNestedList(
                                entryBuilder,
                                text("schemas.fixers"),
                                entry::fixers,
                                { listOf(Fixer(ResourceLocation(""), ResourceLocation(""))) },
                                true,
                                tooltip("datafixer.fixers"),
                                { newValue -> entry.fixers = newValue },
                                { entry, _ ->
                                    TypedEntryUtils.makeMultiElementEntry(
                                        text("datafixer.fixer"),
                                        entry,
                                        true,

                                        EntryBuilder(text("datafixer.old_id"), entry.oldId.toString(),
                                            "",
                                            { newValue -> entry.oldId = ResourceLocation(newValue) },
                                            tooltip("datafixer.old_id")
                                        ).build(entryBuilder),

                                        EntryBuilder(text("datafixer.new_id"), entry.newId.toString(),
                                            "",
                                            { newValue -> entry.newId = ResourceLocation(newValue) },
                                            tooltip("datafixer.new_id")
                                        ).build(entryBuilder)
                                    )
                                }
                            )
                        )
                    }
                )
            )
        }
    )
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
    return TypedEntryUtils.makeTypedEntryList(
        entryBuilder,
        text("registry_fixers"),
        config::registryFixers,
        defaultConfig::registryFixers,
        false,
        tooltip("registry_fixers"),
        { newValue -> config.registryFixers = newValue},
        { element, _ ->
            val registryFixer = element ?: defaultRegistryFixer
            TypedEntryUtils.makeMultiElementEntry(
                text("registry_fixers.registry_fixer"),
                registryFixer,
                true,

                EntryBuilder(text("registry_fixers.registry_key"), registryFixer.registryKey.toString(),
                    "",
                    { newValue -> registryFixer.registryKey = ResourceLocation(newValue) },
                    tooltip("registry_fixers.registry_key")
                ).build(entryBuilder),

                TypedEntryUtils.makeNestedList(
                    entryBuilder,
                    text("datafixer.fixers"),
                    registryFixer::fixers,
                    { defaultFixers },
                    true,
                    tooltip("datafixer.fixers"),
                    { newValue -> registryFixer.fixers = newValue },
                    { element, _ ->
                        val entry = element ?: Fixer(ResourceLocation(""), ResourceLocation(""))
                        TypedEntryUtils.makeMultiElementEntry(
                            text("datafixer.fixer"),
                            entry,
                            true,

                            EntryBuilder(text("datafixer.old_id"), entry.oldId.toString(),
                                "",
                                { newValue -> entry.oldId = ResourceLocation(newValue) },
                                tooltip("datafixer.old_id")
                            ).build(entryBuilder),

                            EntryBuilder(text("datafixer.new_id"), entry.newId.toString(),
                                "",
                                { newValue -> entry.newId = ResourceLocation(newValue) },
                                tooltip("datafixer.new_id")
                            ).build(entryBuilder)
                        )
                    }
                )
            )
        }
    )
}
