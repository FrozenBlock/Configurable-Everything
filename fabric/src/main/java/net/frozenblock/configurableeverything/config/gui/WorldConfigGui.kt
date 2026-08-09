@file:Environment(EnvType.CLIENT)

package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.clothconfig2.api.Requirement
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.WorldConfig
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.configurableeverything.util.tooltip
import net.frozenblock.lib.config.api.client.gui.EntryBuilder
import net.frozenblock.lib.config.api.client.gui.Slider
import net.frozenblock.lib.config.api.client.gui.SliderType
import net.frozenblock.lib.config.clothconfig.synced

private inline val mainToggleReq: Requirement
    get() = Requirement.isTrue(MainConfigGui.INSTANCE!!.world)

object WorldConfigGui {

    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        category.addEntry(
            EntryBuilder(WorldConfig.dayTimeSpeedAmplifier,
                text("day_time_speed"),
                tooltip("day_time_speed"),
                Slider(WorldConfig.dayTimeSpeedAmplifier.withSync, 1, 100, SliderType.LONG),
                Slider(WorldConfig.dayTimeSpeedAmplifier.defaultValue(), 0, 0, SliderType.LONG),
                { newValue -> WorldConfig.dayTimeSpeedAmplifier.setValue((newValue as Slider<*>).value.toLong()) },
                requirement = mainToggleReq
            ).build(entryBuilder)
        )
        category.addEntry(
            entryBuilder.startIntSlider(text("sun_size"), WorldConfig.sunSize.withSync, 10, 1000)
                .setDefaultValue(WorldConfig.sunSize.defaultValue())
                .setSaveConsumer(WorldConfig.sunSize::setValue)
                .setTooltip(tooltip("sun_size"))
                .setRequirement(mainToggleReq)
                .build()
        )
        category.addEntry(
            entryBuilder.startIntSlider(text("moon_size"), WorldConfig.moonSize.withSync, 10, 1000)
                .setDefaultValue(WorldConfig.moonSize.defaultValue())
                .setSaveConsumer(WorldConfig.moonSize::setValue)
                .setTooltip(tooltip("moon_size"))
                .setRequirement(mainToggleReq)
                .build()
        )
        category.addEntry(
            EntryBuilder(WorldConfig.disableExperimentalWarning,
                text("disable_experimental_warning"),
                tooltip("disable_experimental_warning"),
                requirement = mainToggleReq,
            ).build(entryBuilder)
        )
    }
}
