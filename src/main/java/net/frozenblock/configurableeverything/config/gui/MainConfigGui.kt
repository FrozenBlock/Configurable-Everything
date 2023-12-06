@file:Environment(EnvType.CLIENT)

package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.clothconfig2.api.Requirement
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry
import me.shedaniel.clothconfig2.gui.entries.StringListListEntry
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.*
import net.frozenblock.lib.config.api.client.gui.EntryBuilder
import net.frozenblock.lib.config.clothconfig.FrozenClothConfig.createSubCategory
import net.frozenblock.lib.config.clothconfig.synced

private val configInstance = MainConfig

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
    ).build(entryBuilder).synced(
        config::class,
        "biome",
        configInstance
    ) as BooleanListEntry

    val biomePlacement: BooleanListEntry = EntryBuilder(text("biome_placement"), config.biome_placement,
        defaultConfig.biome_placement!!,
        { newValue -> config.biome_placement = newValue },
        tooltip("biome_placement"),
        true
    ).build(entryBuilder).synced(
        config::class,
        "biome_placement",
        configInstance
    ) as BooleanListEntry

    val block: BooleanListEntry = EntryBuilder(text("block"), config.block,
        defaultConfig.block!!,
        { newValue -> config.block = newValue },
        tooltip("block"),
        true
    ).build(entryBuilder).synced(
        config::class,
        "block",
        configInstance
    ) as BooleanListEntry

    val datafixer: BooleanListEntry = EntryBuilder(text("datafixer"), config.datafixer,
        defaultConfig.datafixer!!,
        { newValue -> config.datafixer = newValue },
        tooltip("datafixer"),
        true
    ).build(entryBuilder).synced(
        config::class,
        "datafixer",
        configInstance
    ) as BooleanListEntry

    val entity: BooleanListEntry = EntryBuilder(text("entity"), config.entity,
        defaultConfig.entity!!,
        { newValue -> config.entity = newValue },
        tooltip("entity"),
        true
    ).build(entryBuilder).synced(
        config::class,
        "entity",
        configInstance
    ) as BooleanListEntry

    val fluid: BooleanListEntry = EntryBuilder(text("fluid"), config.fluid,
        defaultConfig.fluid!!,
        { newValue -> config.fluid = newValue },
        tooltip("fluid"),
        true
    ).build(entryBuilder).synced(
        config::class,
        "fluid",
        configInstance
    ) as BooleanListEntry

    val game: BooleanListEntry = EntryBuilder(text("game"), config.game,
        defaultConfig.game!!,
        { newValue -> config.game = newValue },
        tooltip("game"),
        true
    ).build(entryBuilder) as BooleanListEntry

    val gravity: BooleanListEntry? = ifExperimental {
        EntryBuilder(text("gravity"), config.gravity,
            defaultConfig.gravity!!,
            { newValue -> config.gravity = newValue },
            tooltip("gravity"),
            true
        ).build(entryBuilder).synced(
            config::class,
            "gravity",
            configInstance
        ) as BooleanListEntry
    }

    val item: BooleanListEntry? = ifExperimental {
        EntryBuilder(text("item"), config.item,
            defaultConfig.item!!,
            { newValue -> config.item = newValue },
            tooltip("item")
        ).build(entryBuilder).synced(
            config::class,
            "item",
            configInstance
        ) as BooleanListEntry
    }

    val music: BooleanListEntry? = ifExperimental {
        EntryBuilder(text("music"), config.music,
            defaultConfig.music!!,
            { newValue -> config.music = newValue },
            tooltip("music")
        ).build(entryBuilder) as BooleanListEntry
    }

    val registry: BooleanListEntry = EntryBuilder(text("registry"), config.registry,
        defaultConfig.registry!!,
        { newValue -> config.registry = newValue },
        tooltip("registry"),
        true
    ).build(entryBuilder).synced(
        config::class,
        "registry",
        configInstance
    ) as BooleanListEntry

    val screenShake: BooleanListEntry = EntryBuilder(text("screen_shake"), config.screen_shake,
        defaultConfig.screen_shake!!,
        { newValue -> config.screen_shake = newValue },
        tooltip("screen_shake"),
        true
    ).build(entryBuilder) as BooleanListEntry

    val scripting: BooleanListEntry = EntryBuilder(text("scripting"), config.scripting,
        defaultConfig.scripting!!,
        { newValue -> config.scripting = newValue },
        tooltip("scripting"),
        true,
        requirement = Requirement.isTrue { HAS_EXTENSIONS }
    ).build(entryBuilder) as BooleanListEntry

    val sculkSpreading: BooleanListEntry? = ifExperimental {
        EntryBuilder(text("sculk_spreading"), config.sculk_spreading,
            defaultConfig.sculk_spreading!!,
            { newValue -> config.sculk_spreading = newValue },
            tooltip("sculk_spreading"),
            true
        ).build(entryBuilder).synced(
            config::class,
            "sculk_spreading",
            configInstance
        ) as BooleanListEntry
    }

    val splashText: BooleanListEntry = EntryBuilder(text("splash_text"), config.splash_text,
        defaultConfig.splash_text!!,
        { newValue -> config.splash_text = newValue },
        tooltip("splash_text"),
        true
    ).build(entryBuilder) as BooleanListEntry

    val structure: BooleanListEntry? = ifExperimental {
        EntryBuilder(text("structure"), config.structure,
            defaultConfig.structure!!,
            { newValue -> config.structure = newValue },
            tooltip("structure"),
            true
        ).build(entryBuilder).synced(
            config::class,
            "structure",
            configInstance
        ) as BooleanListEntry
    }

    val surfaceRule: BooleanListEntry = EntryBuilder(text("surface_rule"), config.surface_rule,
        defaultConfig.surface_rule!!,
        { newValue -> config.surface_rule = newValue },
        tooltip("surface_rule"),
        true
    ).build(entryBuilder).synced(
        config::class,
        "surface_rule",
        configInstance
    ) as BooleanListEntry

    val world: BooleanListEntry = EntryBuilder(text("world"), config.world,
        defaultConfig.world!!,
        { newValue -> config.world = newValue },
        tooltip("world"),
        true
    ).build(entryBuilder).synced(
        config::class,
        "world",
        configInstance
    ) as BooleanListEntry

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

    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        category.background = id("textures/config/main.png")

        category.addEntry(biome)

        category.addEntry(biomePlacement)

        category.addEntry(block)

        category.addEntry(datafixer)

        category.addEntry(entity)

        category.addEntry(fluid)

        category.addEntry(game)

        gravity?.apply { category.addEntry(this) }
        item?.apply { category.addEntry(this) }
        music?.apply { category.addEntry(this) }

        category.addEntry(registry)

        category.addEntry(screenShake)

        category.addEntry(scripting)

        sculkSpreading?.apply { category.addEntry(this) }

        category.addEntry(splashText)

        structure?.apply { category.addEntry(this) }

        category.addEntry(surfaceRule)

        category.addEntry(world)

        createSubCategory(
            entryBuilder, category, text("datapack"), false, tooltip("datapack"),
            applyDatapackFolders, datapackFolders, datapackBiome, datapackBiomePlacement, json5Support
        )
    }
}
