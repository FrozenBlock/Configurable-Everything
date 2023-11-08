@file:Environment(EnvType.CLIENT)

package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry
import me.shedaniel.clothconfig2.gui.entries.StringListListEntry
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.ENABLE_EXPERIMENTAL_FEATURES
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.configurableeverything.util.tooltip
import net.frozenblock.lib.config.api.client.gui.EntryBuilder
import net.frozenblock.lib.config.clothconfig.FrozenClothConfig.createSubCategory

@Environment(EnvType.CLIENT)
class MainConfigGui(private val entryBuilder: ConfigEntryBuilder, private val config: MainConfig, private val defaultConfig: MainConfig) {
    companion object {
        var INSTANCE: MainConfigGui? = null

        fun createInstance(entryBuilder: ConfigEntryBuilder, config: MainConfig, defaultConfig: MainConfig): MainConfigGui {
            INSTANCE = MainConfigGui(entryBuilder, config, defaultConfig)
            return INSTANCE!!
        }
    }

    val biome: BooleanListEntry = EntryBuilder(text("biome"), config.biome,
        defaultConfig.biome!!,
        { newValue -> config.biome = newValue },
        tooltip("biome"),
        true
    ).build(entryBuilder) as BooleanListEntry

    val biomePlacement: BooleanListEntry = EntryBuilder(text("biome_placement"), config.biome_placement,
        defaultConfig.biome_placement!!,
        { newValue -> config.biome_placement = newValue },
        tooltip("biome_placement"),
        true
    ).build(entryBuilder) as BooleanListEntry

    val block: BooleanListEntry = EntryBuilder(text("block"), config.block,
        defaultConfig.block!!,
        { newValue -> config.block = newValue },
        tooltip("block"),
        true
    ).build(entryBuilder) as BooleanListEntry

    val datafixer: BooleanListEntry = EntryBuilder(text("datafixer"), config.datafixer,
        defaultConfig.datafixer!!,
        { newValue -> config.datafixer = newValue },
        tooltip("datafixer"),
        true
    ).build(entryBuilder) as BooleanListEntry

    val entity: BooleanListEntry = EntryBuilder(text("entity"), config.entity,
        defaultConfig.entity!!,
        { newValue -> config.entity = newValue },
        tooltip("entity"),
        true
    ).build(entryBuilder) as BooleanListEntry

    val fluid: BooleanListEntry = EntryBuilder(text("fluid"), config.fluid,
        defaultConfig.fluid!!,
        { newValue -> config.fluid = newValue },
        tooltip("fluid"),
        true
    ).build(entryBuilder) as BooleanListEntry

    val game: BooleanListEntry = EntryBuilder(text("game"), config.game,
        defaultConfig.game!!,
        { newValue -> config.game = newValue },
        tooltip("game"),
        true
    ).build(entryBuilder) as BooleanListEntry

    val screenShake: BooleanListEntry = EntryBuilder(text("screen_shake"), config.screen_shake,
        defaultConfig.screen_shake!!,
        { newValue -> config.screen_shake = newValue },
        tooltip("screen_shake"),
        true
    ).build(entryBuilder) as BooleanListEntry

    val splashText: BooleanListEntry = EntryBuilder(text("splash_text"), config.splash_text,
        defaultConfig.splash_text!!,
        { newValue -> config.splash_text = newValue },
        tooltip("splash_text"),
        true
    ).build(entryBuilder) as BooleanListEntry

    val surfaceRule: BooleanListEntry = EntryBuilder(text("surface_rule"), config.surface_rule,
        defaultConfig.surface_rule!!,
        { newValue -> config.surface_rule = newValue },
        tooltip("surface_rule"),
        true
    ).build(entryBuilder) as BooleanListEntry

    val world: BooleanListEntry = EntryBuilder(text("world"), config.world,
        defaultConfig.world!!,
        { newValue -> config.world = newValue },
        tooltip("world"),
        true
    ).build(entryBuilder) as BooleanListEntry

    // datapack

    val applyDatapackFolders: BooleanListEntry = EntryBuilder(text("apply_datapack_folders"), config.datapack?.applyDatapackFolders,
        defaultConfig.datapack!!.applyDatapackFolders!!,
        { newValue -> config.datapack?.applyDatapackFolders = newValue },
        tooltip("apply_datapack_folders"),
        true
    ).build(entryBuilder) as BooleanListEntry

    val datapackFolders: StringListListEntry = entryBuilder.startStrList(text("datapack_folders"), config.datapack?.datapackFolders ?: defaultConfig.datapack!!.datapackFolders!!)
        .setDefaultValue(defaultConfig.datapack!!.datapackFolders!!)
        .setSaveConsumer { newValue -> config.datapack?.datapackFolders = newValue }
        .setTooltip(tooltip("datapack_folders"))
        .requireRestart()
        .build()

    val datapackBiome: BooleanListEntry = EntryBuilder(text("datapack_biome"), config.datapack?.biome,
        defaultConfig.datapack!!.biome!!,
        { newValue -> config.datapack?.biome = newValue },
        tooltip("datapack_biome"),
        true
    ).build(entryBuilder) as BooleanListEntry

    val datapackBiomePlacement: BooleanListEntry = EntryBuilder(text("datapack_biome_placement"), config.datapack?.biome_placement,
        defaultConfig.datapack!!.biome_placement!!,
        { newValue -> config.datapack?.biome_placement = newValue },
        tooltip("datapack_biome_placement"),
        true
    ).build(entryBuilder) as BooleanListEntry

    val json5Support: BooleanListEntry = EntryBuilder(text("json5_support"), config.datapack?.json5Support,
        defaultConfig.datapack!!.json5Support!!,
        { newValue -> config.datapack?.json5Support = newValue },
        tooltip("json5_support"),
        true
    ).build(entryBuilder) as BooleanListEntry

    // kotlin scripting

    val applyKotlinScripts: BooleanListEntry = EntryBuilder(text("apply_kotlin_scripts"), config.kotlinScripting?.applyKotlinScripts,
        defaultConfig.kotlinScripting!!.applyKotlinScripts!!,
        { newValue -> config.kotlinScripting?.applyKotlinScripts = newValue },
        tooltip("apply_kotlin_scripts"),
        true
    ).build(entryBuilder) as BooleanListEntry

    val defaultImports: StringListListEntry = entryBuilder.startStrList(text("default_imports"), config.kotlinScripting?.defaultImports)
        .setDefaultValue { defaultConfig.kotlinScripting!!.defaultImports!! }
        .setSaveConsumer { newValue -> config.kotlinScripting?.defaultImports = newValue }
        .setTooltip(tooltip("default_imports"))
        .requireRestart()
        .build()

    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        category.background = id("textures/config/main.png")

        category.addEntry(biome)

        category.addEntry(biomePlacement)

        category.addEntry(block)

        category.addEntry(datafixer)

        category.addEntry(entity)

        category.addEntry(fluid)

        category.addEntry(game)

        if (ENABLE_EXPERIMENTAL_FEATURES) {
            category.addEntry(EntryBuilder(text("gravity"), config.gravity,
                defaultConfig.gravity!!,
                { newValue -> config.gravity = newValue },
                tooltip("gravity"),
                true
            ).build(entryBuilder))

            category.addEntry(EntryBuilder(text("item"), config.item,
                defaultConfig.item!!,
                { newValue -> config.item = newValue },
                tooltip("item"),
                true
            ).build(entryBuilder))

            category.addEntry(EntryBuilder(text("music"), config.music,
                defaultConfig.music!!,
                { newValue -> config.music = newValue },
                tooltip("music"),
                true
            ).build(entryBuilder))
        }

        category.addEntry(screenShake)

        if (ENABLE_EXPERIMENTAL_FEATURES)
            category.addEntry(EntryBuilder(text("sculk_spreading"), config.sculk_spreading,
                defaultConfig.sculk_spreading!!,
                { newValue -> config.sculk_spreading = newValue },
                tooltip("sculk_spreading"),
                true
            ).build(entryBuilder))

        category.addEntry(splashText)

        if (ENABLE_EXPERIMENTAL_FEATURES)
            category.addEntry(EntryBuilder(text("structrue"), config.structure,
                defaultConfig.structure!!,
                { newValue -> config.structure = newValue },
                tooltip("structure"),
                true
            ).build(entryBuilder))

        category.addEntry(surfaceRule)

        category.addEntry(world)

        createSubCategory(
            entryBuilder, category, text("datapack"), false, tooltip("datapack"),
            applyDatapackFolders, datapackFolders, datapackBiome, datapackBiomePlacement, json5Support
        )

        createSubCategory(
            entryBuilder, category, text("kotlin_scripting"), false, tooltip("kotlin_scripting"),
            applyKotlinScripts, defaultImports
        )
    }
}
