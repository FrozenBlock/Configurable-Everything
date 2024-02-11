@file:Environment(EnvType.CLIENT)

package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.clothconfig2.api.Requirement
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.ScriptingConfig
import net.frozenblock.configurableeverything.util.HAS_EXTENSIONS
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.configurableeverything.util.tooltip
import net.frozenblock.lib.config.api.client.gui.EntryBuilder
import net.frozenblock.lib.config.api.client.gui.StringList

object ScriptingConfigGui {

    private inline val mainToggleReq: Requirement
        get() = Requirement.isTrue(MainConfigGui.INSTANCE!!.scripting)

    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        val config = ScriptingConfig.get(real = true)
        val defaultConfig = ScriptingConfig.defaultInstance()
        category.background = id("textures/config/scripting.png")

        val applyKotlinScripts: BooleanListEntry = EntryBuilder(text("apply_kotlin_scripts"), config.applyKotlinScripts,
            defaultConfig.applyKotlinScripts!!,
            { newValue -> config.applyKotlinScripts = newValue },
            tooltip("apply_kotlin_scripts"),
            true,
            requirement = Requirement.all(
                mainToggleReq,
                Requirement.isTrue { HAS_EXTENSIONS }
            )
        ).build(entryBuilder) as BooleanListEntry
        category.addEntry(applyKotlinScripts)

        category.addEntry(EntryBuilder(text("default_imports"), StringList(config.defaultImports ?: emptyList()),
            StringList(defaultConfig.defaultImports!!),
            { newValue -> config.defaultImports = newValue.list },
            tooltip("default_imports"),
            requirement = Requirement.all(
                mainToggleReq,
                Requirement.isTrue { HAS_EXTENSIONS },
                Requirement.isTrue(applyKotlinScripts),
            )
        ).build(entryBuilder))
    }

}
