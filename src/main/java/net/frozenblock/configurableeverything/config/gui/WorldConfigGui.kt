@file:Environment(EnvType.CLIENT)

package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.WorldConfig
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.configurableeverything.util.tooltip

@Environment(EnvType.CLIENT)
object WorldConfigGui {
    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        val config = WorldConfig.get(real = true)
        val defaultConfig = WorldConfig.INSTANCE.defaultInstance()
        category.background = id("textures/config/world.png")
        category.addEntry(
            entryBuilder.startLongSlider(text("day_time_speed"), config.dayTimeSpeedAmplifier ?: defaultConfig.dayTimeSpeedAmplifier!!, 1L, 100L)
                .setDefaultValue(1L)
                .setSaveConsumer { newValue: Long? -> config.dayTimeSpeedAmplifier = newValue!! }
                .setTooltip(tooltip("day_time_speed"))
                .build()
        )
        category.addEntry(
            entryBuilder.startBooleanToggle(text("fix_sun_moon_rotating"), config.fixSunMoonRotating ?: defaultConfig.fixSunMoonRotating!!)
                .setDefaultValue(false)
                .setSaveConsumer { newValue: Boolean? -> config.fixSunMoonRotating = newValue!! }
                .setTooltip(tooltip("fix_sun_moon_rotating"))
                .build()
        )
        category.addEntry(
            entryBuilder.startIntSlider(text("sun_size"), config.sunSize ?: defaultConfig.sunSize!!, 10, 1000)
                .setDefaultValue(300)
                .setSaveConsumer { newValue: Int? -> config.sunSize = newValue!! }
                .setTooltip(tooltip("sun_size"))
                .build()
        )
        category.addEntry(
            entryBuilder.startIntSlider(text("moon_size"), config.moonSize ?: defaultConfig.moonSize!!, 10, 1000)
                .setDefaultValue(200)
                .setSaveConsumer { newValue: Int? -> config.moonSize = newValue!! }
                .setTooltip(tooltip("moon_size"))
                .build()
        )
        category.addEntry(
            entryBuilder.startBooleanToggle(text("disable_experimental_warning"), config.disableExperimentalWarning ?: defaultConfig.disableExperimentalWarning!!)
                .setDefaultValue(false)
                .setSaveConsumer { newValue: Boolean? -> config.disableExperimentalWarning = newValue!! }
                .setTooltip(tooltip("disable_experimental_warning"))
                .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
                .build()
        )
    }
}
