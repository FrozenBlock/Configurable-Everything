@file:Environment(EnvType.CLIENT)

package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.clothconfig2.api.Requirement
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.RecipeConfig
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.configurableeverything.util.tooltip
import net.frozenblock.configurableeverything.util.value
import net.frozenblock.lib.config.api.client.gui.EntryBuilder
import net.frozenblock.lib.config.api.client.gui.StringList
import net.minecraft.resources.Identifier

object RecipeConfigGui {

    private inline val mainToggleReq: Requirement
        get() = Requirement.isTrue(MainConfigGui.INSTANCE!!.recipe)

    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        val config = RecipeConfig.get(real = true)
        val defaultConfig = RecipeConfig.defaultInstance()

        val removed = EntryBuilder(text("removed_recipes"), StringList(config.removedRecipes.value().map { it.toString() }),
            StringList(defaultConfig.removedRecipes.value().map { it.toString() }),
            { newValue -> config.removedRecipes.value = newValue.list.map { Identifier.parse(it) }.toMutableList() },
            tooltip("removed_recipes"),
            true,
            requirement = mainToggleReq,
        ).build(entryBuilder).apply {
            category.addEntry(this)
        }
    }
}
