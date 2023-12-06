@file:Environment(EnvType.CLIENT)

package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.clothconfig2.api.Requirement
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.ScreenShakeConfig
import net.frozenblock.configurableeverything.screenshake.util.SoundScreenShake
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.configurableeverything.util.tooltip
import net.frozenblock.lib.config.api.client.gui.EntryBuilder
import net.frozenblock.lib.config.api.client.gui.multiElementEntry
import net.frozenblock.lib.config.api.client.gui.typedEntryList
import net.minecraft.resources.ResourceLocation

private val mainToggleReq: Requirement
    get() = Requirement.isTrue(MainConfigGui.INSTANCE!!.screenShake)

object ScreenShakeConfigGui {

    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        val config = ScreenShakeConfig.get(real = true)
        val defaultConfig = ScreenShakeConfig.defaultInstance()
        category.background = id("textures/config/screen_shake.png")

        category.addEntry(soundScreenShakes(entryBuilder, config, defaultConfig))

        category.addEntry(EntryBuilder(text("dragon_respawn_screen_shake"), config.dragonRespawnScreenShake,
            defaultConfig.dragonRespawnScreenShake!!,
            { newValue -> config.dragonRespawnScreenShake = newValue },
            tooltip("dragon_respawn_screen_shake"),
            requirement = mainToggleReq
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(text("explosion_screen_shake"), config.explosionScreenShake,
            defaultConfig.explosionScreenShake!!,
            { newValue -> config.explosionScreenShake = newValue },
            tooltip("explosion_screen_shake"),
            requirement = mainToggleReq
        ).build(entryBuilder))
    }
}

private fun soundScreenShakes(
    entryBuilder: ConfigEntryBuilder,
    config: ScreenShakeConfig,
    defaultConfig: ScreenShakeConfig
): AbstractConfigListEntry<*> {
    return typedEntryList(
        entryBuilder,
        text("sound_screen_shakes"),
        config::soundScreenShakes,
        {defaultConfig.soundScreenShakes!!},
        false,
        tooltip("sound_screen_shakes"),
        { newValue -> config.soundScreenShakes = newValue},
        { element, _ ->
            val soundScreenShake = element ?: SoundScreenShake(ResourceLocation(""), 1F, 25, 1, 20F)
            multiElementEntry(
                text("sound_screen_shakes.sound_screen_shake"),
                soundScreenShake,
                true,

                EntryBuilder(text("sound_screen_shakes.sound"), soundScreenShake.sound.toString(),
                    "",
                    { newValue -> soundScreenShake.sound = ResourceLocation(newValue) },
                    tooltip("sound_screen_shakes.sound")
                ).build(entryBuilder),

                EntryBuilder(text("sound_screen_shakes.intensity"), soundScreenShake.intensity,
                    1F,
                    { newValue -> soundScreenShake.intensity = newValue },
                    tooltip("sound_screen_shakes.intensity")
                ).build(entryBuilder),

                EntryBuilder(text("sound_screen_shakes.duration"), soundScreenShake.duration,
                    25,
                    { newValue -> soundScreenShake.duration = newValue },
                    tooltip("sound_screen_shakes.duration")
                ).build(entryBuilder),

                EntryBuilder(text("sound_screen_shakes.fall_off_start"), soundScreenShake.falloffStart,
                    1,
                    { newValue -> soundScreenShake.falloffStart = newValue },
                    tooltip("sound_screen_shakes.fall_off_start")
                ).build(entryBuilder),

                EntryBuilder(text("sound_screen_shakes.max_distance"), soundScreenShake.maxDistance,
                    20F,
                    { newValue -> soundScreenShake.maxDistance = newValue },
                    tooltip("sound_screen_shakes.max_distance")
                ).build(entryBuilder),
            )
        }
    ).apply {
        this.requirement = mainToggleReq
    }
}
