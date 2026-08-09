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
import net.frozenblock.lib.config.api.client.gui.SimpleEntryBuilder
import net.frozenblock.lib.config.api.client.gui.configEntryList
import net.frozenblock.lib.config.api.client.gui.multiElementEntry
import net.frozenblock.lib.config.api.client.gui.typedEntryList
import net.minecraft.resources.Identifier

private inline val mainToggleReq: Requirement
    get() = Requirement.isTrue(MainConfigGui.INSTANCE!!.screenShake)

object ScreenShakeConfigGui {

    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        category.addEntry(soundScreenShakes(entryBuilder))

        category.addEntry(EntryBuilder(ScreenShakeConfig.dragonRespawnScreenShake,
            text("dragon_respawn_screen_shake"),
            tooltip("dragon_respawn_screen_shake"),
            requirement = mainToggleReq
        ).build(entryBuilder))

        category.addEntry(EntryBuilder(ScreenShakeConfig.explosionScreenShake,
            text("explosion_screen_shake"),
            tooltip("explosion_screen_shake"),
            requirement = mainToggleReq
        ).build(entryBuilder))
    }
}

private fun soundScreenShakes(
    entryBuilder: ConfigEntryBuilder,
): AbstractConfigListEntry<*> {
    return configEntryList(
        entryBuilder,
        text("sound_screen_shakes"),
        ScreenShakeConfig.soundScreenShakes,
        false,
        tooltip("sound_screen_shakes"),
        { element: SoundScreenShake?, _ ->
            val soundScreenShake = element ?: SoundScreenShake(Identifier.withDefaultNamespace(""), 1F, 25, 1, 20F)
            multiElementEntry(
                text("sound_screen_shakes.sound_screen_shake"),
                soundScreenShake,
                true,

                SimpleEntryBuilder(text("sound_screen_shakes.sound"), soundScreenShake.sound.toString(),
                    "",
                    { newValue -> soundScreenShake.sound = Identifier.parse(newValue) },
                    tooltip("sound_screen_shakes.sound")
                ).build(entryBuilder),

                SimpleEntryBuilder(text("sound_screen_shakes.intensity"), soundScreenShake.intensity,
                    1F,
                    { newValue -> soundScreenShake.intensity = newValue },
                    tooltip("sound_screen_shakes.intensity")
                ).build(entryBuilder),

                SimpleEntryBuilder(text("sound_screen_shakes.duration"), soundScreenShake.duration,
                    25,
                    { newValue -> soundScreenShake.duration = newValue },
                    tooltip("sound_screen_shakes.duration")
                ).build(entryBuilder),

                SimpleEntryBuilder(text("sound_screen_shakes.fall_off_start"), soundScreenShake.falloffStart,
                    1,
                    { newValue -> soundScreenShake.falloffStart = newValue },
                    tooltip("sound_screen_shakes.fall_off_start")
                ).build(entryBuilder),

                SimpleEntryBuilder(text("sound_screen_shakes.max_distance"), soundScreenShake.maxDistance,
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
