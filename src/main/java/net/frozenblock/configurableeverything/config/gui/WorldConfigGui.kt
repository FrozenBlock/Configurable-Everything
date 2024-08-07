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

private val configInstance = WorldConfig

private inline val mainToggleReq: Requirement
    get() = Requirement.isTrue(MainConfigGui.INSTANCE!!.world)

object WorldConfigGui {

    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        val config = configInstance.instance()
        val syncConfig = configInstance.configWithSync()
        val defaultConfig = configInstance.defaultInstance()

        category.addEntry(
            EntryBuilder(text("day_time_speed"), Slider(syncConfig.dayTimeSpeedAmplifier, 1, 100, SliderType.LONG),
                Slider(defaultConfig.dayTimeSpeedAmplifier, 0, 0, SliderType.LONG),
                { newValue -> config.dayTimeSpeedAmplifier = newValue.value.toLong() },
                tooltip("day_time_speed"),
                requirement = mainToggleReq
            ).build(entryBuilder).synced(
                config::class,
                "dayTimeSpeedAmplifier",
                configInstance
            )
        )
        category.addEntry(
            EntryBuilder(text("fix_sun_moon_rotating"), syncConfig.fixSunMoonRotating,
                defaultConfig.fixSunMoonRotating,
                { newValue -> config.fixSunMoonRotating = newValue },
                tooltip("fix_sun_moon_rotating"),
                requirement = mainToggleReq,
            ).build(entryBuilder)
        )
        category.addEntry(
            entryBuilder.startIntSlider(text("sun_size"), syncConfig.sunSize, 10, 1000)
                .setDefaultValue(defaultConfig.sunSize)
                .setSaveConsumer { newValue: Int? -> config.sunSize = newValue!! }
                .setTooltip(tooltip("sun_size"))
                .setRequirement(mainToggleReq)
                .build()
        )
        category.addEntry(
            entryBuilder.startIntSlider(text("moon_size"), syncConfig.moonSize, 10, 1000)
                .setDefaultValue(defaultConfig.moonSize)
                .setSaveConsumer { newValue: Int? -> config.moonSize = newValue!! }
                .setTooltip(tooltip("moon_size"))
                .setRequirement(mainToggleReq)
                .build()
        )
        category.addEntry(
            EntryBuilder(text("disable_experimental_warning"), syncConfig.disableExperimentalWarning,
                defaultConfig.disableExperimentalWarning,
                { newValue -> config.disableExperimentalWarning = newValue },
                tooltip("disable_experimental_warning"),
                requirement = mainToggleReq,
            ).build(entryBuilder)
        )
    }
}
