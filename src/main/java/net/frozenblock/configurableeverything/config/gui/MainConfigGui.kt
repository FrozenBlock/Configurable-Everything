@file:Environment(EnvType.CLIENT)

package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.clothconfig2.api.Requirement
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry
import me.shedaniel.clothconfig2.gui.entries.StringListListEntry
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.EarlyMainConfig
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.*
import net.frozenblock.lib.config.api.client.gui.EntryBuilder
import net.frozenblock.lib.config.clothconfig.FrozenClothConfig.createSubCategory
import net.frozenblock.lib.config.clothconfig.synced

class MainConfigGui(private val entryBuilder: ConfigEntryBuilder) {
    companion object {
        var INSTANCE: MainConfigGui? = null

        fun createInstance(entryBuilder: ConfigEntryBuilder): MainConfigGui {
            INSTANCE = MainConfigGui(entryBuilder)
            return INSTANCE!!
        }
    }

    val biome: BooleanListEntry = EntryBuilder(MainConfig.biome,
        text("biome"),
        tooltip("biome"),
        requiresRestart = true
    ).build(entryBuilder) as BooleanListEntry

    val biomePlacement: BooleanListEntry = EntryBuilder(MainConfig.biome_placement,
        text("biome_placement"),
        tooltip("biome_placement"),
        requiresRestart = true
    ).build(entryBuilder) as BooleanListEntry

    val block: BooleanListEntry = EntryBuilder(MainConfig.block,
        text("block"),
        tooltip("block"),
        requiresRestart = true
    ).build(entryBuilder) as BooleanListEntry

    val datafixer: BooleanListEntry = EntryBuilder(MainConfig.datafixer,
        text("datafixer"),
        tooltip("datafixer"),
        requiresRestart = true
    ).build(entryBuilder) as BooleanListEntry

    val entity: BooleanListEntry = EntryBuilder(MainConfig.entity,
        text("entity"),
        tooltip("entity"),
        requiresRestart = true
    ).build(entryBuilder) as BooleanListEntry

    val fluid: BooleanListEntry = EntryBuilder(MainConfig.fluid,
        text("fluid"),
        tooltip("fluid"),
        requiresRestart = true
    ).build(entryBuilder) as BooleanListEntry

    val game: BooleanListEntry = entryBuilder.startBooleanToggle(text("game"), EarlyMainConfig.instance().game)
        .setDefaultValue(EarlyMainConfig.defaultInstance().game)
        .setSaveConsumer { newValue -> EarlyMainConfig.instance().game = newValue }
        .setTooltip(tooltip("game"))
        .requireRestart()
        .build()

    val gravity: BooleanListEntry = EntryBuilder(MainConfig.gravity,
        text("gravity"),
        tooltip("gravity"),
        requiresRestart = true
    ).build(entryBuilder) as BooleanListEntry

    val item: BooleanListEntry = EntryBuilder(MainConfig.item,
        text("item"),
        tooltip("item")
    ).build(entryBuilder) as BooleanListEntry

    val loot: BooleanListEntry = EntryBuilder(MainConfig.loot,
        text("loot"),
        tooltip("loot")
    ).build(entryBuilder) as BooleanListEntry

    val music: BooleanListEntry? = ifExperimental {
        EntryBuilder(MainConfig.music,
            text("music"),
            tooltip("music")
        ).build(entryBuilder) as BooleanListEntry
    }

    val recipe: BooleanListEntry = EntryBuilder(MainConfig.recipe,
        text("recipe"),
        tooltip("recipe"),
        requiresRestart = true
    ).build(entryBuilder) as BooleanListEntry

    val registry: BooleanListEntry = EntryBuilder(MainConfig.registry,
        text("registry"),
        tooltip("registry"),
        requiresRestart = true
    ).build(entryBuilder) as BooleanListEntry

    val screenShake: BooleanListEntry = EntryBuilder(MainConfig.screen_shake,
        text("screen_shake"),
        tooltip("screen_shake"),
        requiresRestart = true
    ).build(entryBuilder) as BooleanListEntry

    val scripting: BooleanListEntry = EntryBuilder(MainConfig.scripting,
        text("scripting"),
        tooltip("scripting"),
        requiresRestart = true,
        requirement = Requirement.isTrue { HAS_EXTENSIONS }
    ).build(entryBuilder) as BooleanListEntry

    val sculkSpreading: BooleanListEntry = EntryBuilder(MainConfig.sculk_spreading,
        text("sculk_spreading"),
        tooltip("sculk_spreading"),
        requiresRestart = true
    ).build(entryBuilder) as BooleanListEntry

    val splashText: BooleanListEntry = EntryBuilder(MainConfig.splash_text,
        text("splash_text"),
        tooltip("splash_text"),
        requiresRestart = true
    ).build(entryBuilder) as BooleanListEntry

    val structure: BooleanListEntry = EntryBuilder(MainConfig.structure,
        text("structure"),
        tooltip("structure"),
        requiresRestart = true
    ).build(entryBuilder) as BooleanListEntry

    val surfaceRule: BooleanListEntry = EntryBuilder(MainConfig.surface_rule,
        text("surface_rule"),
        tooltip("surface_rule"),
        true
    ).build(entryBuilder) as BooleanListEntry

    val tag: BooleanListEntry = EntryBuilder(MainConfig.tag,
        text("tag"),
        tooltip("tag"),
        true
    ).build(entryBuilder) as BooleanListEntry

    val world: BooleanListEntry = EntryBuilder(MainConfig.world,
        text("world"),
        tooltip("world"),
        requiresRestart = true
    ).build(entryBuilder) as BooleanListEntry

    // datapack

    val applyDatapackFolders: BooleanListEntry = EntryBuilder(MainConfig.applyDatapackFolders,
        text("apply_datapack_folders"),
        tooltip("apply_datapack_folders"),
        requiresRestart = true
    ).build(entryBuilder) as BooleanListEntry

    val datapackFolders: StringListListEntry = entryBuilder.startStrList(text("datapack_folders"), MainConfig.datapackFolders.actual)
        .setDefaultValue(MainConfig.datapackFolders.defaultValue())
        .setSaveConsumer(MainConfig.datapackFolders::setValue)
        .setTooltip(tooltip("datapack_folders"))
        .requireRestart()
        .build()

    val datapackBiome: BooleanListEntry = EntryBuilder(MainConfig.datapackBiome,
        text("datapack_biome"),
        tooltip("datapack_biome"),
        requiresRestart = true
    ).build(entryBuilder) as BooleanListEntry

    val datapackBiomePlacement: BooleanListEntry = EntryBuilder(MainConfig.datapackBiomePlacement,
        text("datapack_biome_placement"),
        tooltip("datapack_biome_placement"),
        requiresRestart = true
    ).build(entryBuilder) as BooleanListEntry

    val moreJsonSupport: BooleanListEntry = EntryBuilder(MainConfig.moreJsonSupport,
        text("more_json_support"),
        tooltip("more_json_support"),
        requiresRestart = true
    ).build(entryBuilder) as BooleanListEntry

    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        category.addEntry(biome)

        category.addEntry(biomePlacement)

        category.addEntry(block)

        category.addEntry(datafixer)

        category.addEntry(entity)

        category.addEntry(fluid)

        category.addEntry(game)

        category.addEntry(gravity)

        category.addEntry(item)

        category.addEntry(loot)

        music?.apply { category.addEntry(this) }

        category.addEntry(recipe)

        category.addEntry(registry)

        category.addEntry(screenShake)

        category.addEntry(scripting)

        category.addEntry(sculkSpreading)

        category.addEntry(splashText)

        category.addEntry(structure)

        category.addEntry(surfaceRule)

        category.addEntry(tag)

        category.addEntry(world)

        createSubCategory(
            entryBuilder, category, text("datapack"), false, tooltip("datapack"),
            applyDatapackFolders, datapackFolders, datapackBiome, datapackBiomePlacement, moreJsonSupport
        )
    }
}
