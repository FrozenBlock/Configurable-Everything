@file:Environment(EnvType.CLIENT)

package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.clothconfig2.api.Requirement
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.SplashTextConfig
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.configurableeverything.util.tooltip
import net.frozenblock.lib.config.api.client.gui.EntryBuilder
import net.frozenblock.lib.config.api.client.gui.StringList

object SplashTextConfigGui {

    private inline val mainToggleReq: Requirement
        get() = Requirement.isTrue(MainConfigGui.INSTANCE!!.splashText)

    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        val added = EntryBuilder(SplashTextConfig.addedSplashes,
            text("added_splashes"),
            tooltip("added_splashes"),
            StringList(SplashTextConfig.addedSplashes.actual),
            StringList(SplashTextConfig.addedSplashes.defaultValue()),
            { newValue -> SplashTextConfig.addedSplashes.setValue((newValue as StringList).list.toMutableList()) },
            true,
            requirement = mainToggleReq,
        ).build(entryBuilder).apply {
            category.addEntry(this)
        }

        val removed = EntryBuilder(SplashTextConfig.removedSplashes,
            text("removed_splashes"),
            tooltip("removed_splashes"),
            StringList(SplashTextConfig.removedSplashes.actual),
            StringList(SplashTextConfig.removedSplashes.defaultValue()),
            { newValue -> SplashTextConfig.removedSplashes.setValue((newValue as StringList).list.toMutableList()) },
            true,
            requirement = mainToggleReq,
        ).build(entryBuilder).apply {
            category.addEntry(this)
        }

        category.addEntry(
            entryBuilder.startColorField(text("splash_color"), SplashTextConfig.splashColor.actual)
                .setDefaultValue(SplashTextConfig.splashColor.defaultValue())
                .setAlphaMode(true)
                .setSaveConsumer(SplashTextConfig.splashColor::setValue)
                .setTooltip(tooltip("splash_color"))
                .setRequirement(mainToggleReq)
                .build()
        )

        val removeVanilla = EntryBuilder(SplashTextConfig.removeVanilla,
            text("remove_vanilla"),
            tooltip("remove_vanilla"),
            requirement = mainToggleReq,
        ).build(entryBuilder).apply {
            category.addEntry(this)
        }
    }
}
