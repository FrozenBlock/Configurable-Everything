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
import net.frozenblock.lib.config.api.client.gui.EnumEntry
import net.frozenblock.lib.config.api.client.gui.StringList

object ScriptingConfigGui {

    private inline val mainToggleReq: Requirement
        get() = Requirement.isTrue(MainConfigGui.INSTANCE!!.scripting)

    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {

        val applyKotlinScripts: BooleanListEntry = EntryBuilder(ScriptingConfig.applyKotlinScripts,
            text("apply_kotlin_scripts"),
            tooltip("apply_kotlin_scripts"),
            requiresRestart = true,
            requirement = Requirement.all(
                mainToggleReq,
                Requirement.isTrue { HAS_EXTENSIONS }
            )
        ).build(entryBuilder).apply {
            category.addEntry(this)
        } as BooleanListEntry

        category.addEntry(EntryBuilder(ScriptingConfig.defaultImports,
            text("default_imports"),
            tooltip("default_imports"),
            StringList(ScriptingConfig.defaultImports.get()),
            StringList(ScriptingConfig.defaultImports.defaultValue()),
            { newValue -> ScriptingConfig.defaultImports.setValue((newValue as StringList).list) },
            requirement = Requirement.all(
                mainToggleReq,
                Requirement.isTrue { HAS_EXTENSIONS },
                Requirement.isTrue(applyKotlinScripts),
            )
        ).build(entryBuilder))
    }

}
