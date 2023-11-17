@file:Environment(EnvType.CLIENT)

package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry
import me.shedaniel.clothconfig2.gui.entries.StringListListEntry
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.GameConfig
import net.frozenblock.configurableeverything.config.ScriptingConfig
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.configurableeverything.util.tooltip
import net.frozenblock.lib.config.api.client.gui.EntryBuilder
import net.frozenblock.lib.config.api.client.gui.StringList

@Environment(EnvType.CLIENT)
object ScriptingConfigGui {

    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        val config = ScriptingConfig.get(real = true)
        val defaultConfig = ScriptingConfig.defaultInstance()
        category.background = id("textures/config/scripting.png")

        EntryBuilder(text("apply_kotlin_scripts"), config.applyKotlinScripts,
            defaultConfig.applyKotlinScripts!!,
            { newValue -> config.applyKotlinScripts = newValue },
            tooltip("apply_kotlin_scripts"),
            true
        ).build(entryBuilder)

        category.addEntry(EntryBuilder(text("default_imports"), StringList(config.defaultImports ?: emptyList()),
            StringList(config.defaultImports!!),
            { newValue -> config.defaultImports = newValue.list },
            tooltip("default_imports")
        ).build(entryBuilder))
    }

}
