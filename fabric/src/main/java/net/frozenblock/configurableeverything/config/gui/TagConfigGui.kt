@file:Environment(EnvType.CLIENT)
@file:Suppress("UnstableApiUsage")

package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.clothconfig2.api.Requirement
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.TagConfig
import net.frozenblock.configurableeverything.tag.util.RegistryTagModification
import net.frozenblock.configurableeverything.tag.util.TagModification
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.configurableeverything.util.tooltip
import net.frozenblock.lib.config.api.client.gui.EntryBuilder
import net.frozenblock.lib.config.api.client.gui.SimpleEntryBuilder
import net.frozenblock.lib.config.api.client.gui.StringList
import net.frozenblock.lib.config.api.client.gui.configEntryList
import net.frozenblock.lib.config.api.client.gui.multiElementEntry
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.clothconfig.synced

private inline val mainToggleReq: Requirement
    get() = Requirement.isTrue(MainConfigGui.INSTANCE!!.tag)

/**
 * An object representing the GUI for the [TagConfig].
 */
object TagConfigGui {
    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        EntryBuilder(TagConfig.ignoreInvalidEntries,
            text("ignore_invalid_entries"),
            tooltip("ignore_invalid_entries"),
            requiresRestart = true,
            requirement = mainToggleReq
        ).build(entryBuilder).apply {
            category.addEntry(this)
        }

        category.addEntry(addedBiomes(entryBuilder))
    }
}

private fun addedBiomes(
    entryBuilder: ConfigEntryBuilder,
): AbstractConfigListEntry<*> {
    return configEntryList(
        entryBuilder,
        text("tag_modifications"),
        TagConfig.tagModifications,
        false,
        tooltip("tag_modifications"),
        { element: RegistryTagModification?, _ ->
            val defaultRegistry = "minecraft:item"
            val defaultTag = "minecraft:piglin_loved"
            val defaultAdditions = mutableListOf(
                "minecraft:diamond_block"
            )
            val defaultRemovals = mutableListOf(
                "minecraft:gold_ingot"
            )
            val defaultModification = TagModification(
                defaultTag,
                defaultAdditions,
                defaultRemovals
            )
            val defaultModifications = mutableListOf(defaultModification)
            val defaultRegistryModification = RegistryTagModification(
                defaultRegistry,
                defaultModifications
            )
            val registryTagModification = element ?: defaultRegistryModification
            multiElementEntry(
                text("tag_modifications.registry_modification"),
                registryTagModification,
                true,

                SimpleEntryBuilder(
                    text("tag_modifications.registry"),
                    registryTagModification.registry,
                    "",
                    { newValue -> registryTagModification.registry = newValue },
                    tooltip("tag_modifications.registry")
                ).build(entryBuilder),

                nestedList(
                    entryBuilder,
                    text("tag_modifications.modifications"),
                    registryTagModification::modifications,
                    { defaultModifications },
                    true,
                    tooltip("tag_modifications.modifications"),
                    { newValue -> registryTagModification.modifications = newValue },
                    { modification, _ ->
                        multiElementEntry(
                            text("tag_modifications.modification"),
                            modification,
                            true,

                            SimpleEntryBuilder(
                                text("tag_modifications.tag"),
                                modification.tag,
                                "",
                                { newValue -> modification.tag = newValue },
                                tooltip("tag_modifications.tag")
                            ).build(entryBuilder),

                            SimpleEntryBuilder(
                                text("tag_modifications.additions"),
                                StringList(modification.additions),
                                StringList(listOf()),
                                { newValue -> modification.additions = newValue.list.toMutableList() },
                                tooltip("tag_modifications.additions")
                            ).build(entryBuilder),

                            SimpleEntryBuilder(
                                text("tag_modifications.removals"),
                                StringList(modification.removals),
                                StringList(listOf()),
                                { newValue -> modification.removals = newValue.list.toMutableList() },
                                tooltip("tag_modifications.removals")
                            ).build(entryBuilder),
                        )
                    }
                )
            )
        }
    ).apply {
        this.requirement = mainToggleReq
    }
}
