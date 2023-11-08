@file:Environment(EnvType.CLIENT)

package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.block.util.MutableBlockSoundGroupOverwrite
import net.frozenblock.configurableeverything.block.util.MutableSoundType
import net.frozenblock.configurableeverything.config.BlockConfig
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.configurableeverything.util.tooltip
import net.frozenblock.configurableeverything.util.vanillaId
import net.frozenblock.lib.config.api.client.gui.EntryBuilder
import net.frozenblock.lib.config.api.client.gui.makeMultiElementEntry
import net.frozenblock.lib.config.api.client.gui.makeTypedEntryList
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents

@Environment(EnvType.CLIENT)
object BlockConfigGui {
    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        val config = BlockConfig.get(real = true)
        val defaultConfig = BlockConfig.defaultInstance()
        category.background = id("textures/config/block.png")

        category.addEntry(soundGroupOverwrites(entryBuilder, config, defaultConfig))
    }

    private fun soundGroupOverwrites(
        entryBuilder: ConfigEntryBuilder,
        config: BlockConfig,
        defaultConfig: BlockConfig
    ): AbstractConfigListEntry<*> {
        return makeTypedEntryList(
            entryBuilder,
            text("sound_group_overwrites"),
            config::soundGroupOverwrites,
            {defaultConfig.soundGroupOverwrites!!},
            false,
            tooltip("sound_group_overwrites"),
            { newValue -> config.soundGroupOverwrites = newValue},
            { element, _ ->
                val defaultSoundOverwrite = MutableSoundType(
                    100F,
                    1F,
                    SoundEvents.HORSE_DEATH,
                    SoundEvents.HORSE_DEATH,
                    SoundEvents.HORSE_DEATH,
                    SoundEvents.HORSE_DEATH,
                    SoundEvents.HORSE_DEATH
                )
                val defaultOverwrite = MutableBlockSoundGroupOverwrite(
                    vanillaId("grass_block"),
                    defaultSoundOverwrite
                ) { true }
                val overwrite: MutableBlockSoundGroupOverwrite = element ?: defaultOverwrite
                val soundOverwrite = overwrite.soundOverwrite ?: defaultSoundOverwrite
                makeMultiElementEntry(
                    text("sound_group_overwrites.sound_group_overwrite"),
                    overwrite,
                    true,

                    EntryBuilder(text("sound_group_overwrites.id"), overwrite.blockId.toString(),
                        "",
                        { newValue -> overwrite.blockId = ResourceLocation(newValue) },
                        tooltip("sound_group_overwrites.id")
                    ).build(entryBuilder),

                    makeMultiElementEntry(
                        text("sound_group_overwrites.sound_type"),
                        soundOverwrite,
                        true,

                        EntryBuilder(text("sound_group_overwrites.volume"), soundOverwrite.volume,
                            1F,
                            { newValue -> soundOverwrite.volume = newValue },
                            tooltip("sound_group_overwrites.volume")
                        ).build(entryBuilder),

                        EntryBuilder(text("sound_group_overwrites.pitch"), soundOverwrite.pitch,
                            1F,
                            { newValue -> soundOverwrite.pitch = newValue },
                            tooltip("sound_group_overwrites.pitch")
                        ).build(entryBuilder),

                        EntryBuilder(text("sound_group_overwrites.break_sound"), soundId(soundOverwrite.breakSound),
                            "",
                            { newValue -> soundOverwrite.breakSound = sound(newValue) },
                            tooltip("sound_group_overwrites.break_sound")
                        ).build(entryBuilder),

                        EntryBuilder(text("sound_group_overwrites.step_sound"), soundId(soundOverwrite.stepSound),
                            "",
                            { newValue -> soundOverwrite.stepSound = sound(newValue) },
                            tooltip("sound_group_overwrites.step_sound")
                        ).build(entryBuilder),

                        EntryBuilder(text("sound_group_overwrites.place_sound"), soundId(soundOverwrite.placeSound),
                            "",
                            { newValue -> soundOverwrite.placeSound = sound(newValue) },
                            tooltip("sound_group_overwrites.place_sound")
                        ).build(entryBuilder),

                        EntryBuilder(text("sound_group_overwrites.hit_sound"), soundId(soundOverwrite.hitSound),
                            "",
                            { newValue -> soundOverwrite.hitSound = sound(newValue) },
                            tooltip("sound_group_overwrites.hit_sound")
                        ).build(entryBuilder),

                        EntryBuilder(text("sound_group_overwrites.fall_sound"), soundId(soundOverwrite.fallSound),
                            "",
                            { newValue -> soundOverwrite.fallSound = sound(newValue) },
                            tooltip("sound_group_overwrites.fall_sound")
                        ).build(entryBuilder)
                    )
                )
            }
        )
    }

    private fun soundId(sound: SoundEvent?): String? {
        if (sound == null) return null
        return BuiltInRegistries.SOUND_EVENT.getKey(sound).toString()
    }

    private fun sound(id: String): SoundEvent? {
        return BuiltInRegistries.SOUND_EVENT[ResourceLocation(id)]
    }
}
