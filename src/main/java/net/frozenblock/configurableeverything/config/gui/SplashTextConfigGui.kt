@file:Environment(EnvType.CLIENT)

package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.SplashTextConfig
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.configurableeverything.util.tooltip
import net.minecraft.world.item.DyeColor

@Environment(EnvType.CLIENT)
object SplashTextConfigGui {
    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        val config = SplashTextConfig.get(real = true)
        category.background = id("textures/config/splash_text.png")

        val added = entryBuilder.startStrList(text("added_splashes"), config.addedSplashes)
            .setDefaultValue(ArrayList(listOf()))
            .setSaveConsumer { newValue: List<String?>? -> config.addedSplashes = newValue }
            .setTooltip(tooltip("added_splashes"))
            .requireRestart()
            .build()

        val removed = entryBuilder.startStrList(text("removed_splashes"), config.removedSplashes)
            .setDefaultValue(ArrayList(listOf()))
            .setSaveConsumer { newValue: List<String?>? -> config.removedSplashes = newValue }
            .setTooltip(tooltip("removed_splashes"))
            .requireRestart()
            .build()

        val splashColor = entryBuilder.startColorField(text("splash_color"), config.splashColor ?: DyeColor.YELLOW.textColor)
            .setDefaultValue(DyeColor.YELLOW.textColor)
            .setSaveConsumer { newValue: Int? -> config.splashColor = newValue }
            .setTooltip(tooltip("splash_color"))
            .build()

        val removeVanilla = entryBuilder.startBooleanToggle(text("remove_vanilla"), config.removeVanilla == true)
            .setDefaultValue(false)
            .setSaveConsumer { newValue: Boolean? -> config.removeVanilla = newValue }
            .setTooltip(tooltip("remove_vanilla"))
            .build()

        category.addEntry(added)
        category.addEntry(removed)
        category.addEntry(splashColor)
        category.addEntry(removeVanilla)
    }
}
