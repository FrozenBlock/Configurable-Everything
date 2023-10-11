@file:Environment(EnvType.CLIENT)

package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.BlockConfig
import net.frozenblock.configurableeverything.block.util.*
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.string
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.configurableeverything.util.tooltip
import net.frozenblock.lib.config.api.client.gui.EntryBuilder
import net.frozenblock.lib.config.api.client.gui.makeMultiElementEntry
import net.frozenblock.lib.config.api.client.gui.makeNestedList
import net.frozenblock.lib.config.api.client.gui.makeTypedEntryList
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

@Environment(EnvType.CLIENT)
object BlockConfigGui {
    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        val config = BlockConfig.get()
        val defaultConfig = BlockConfig.INSTANCE.defaultInstance()
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
                val defaultOverwrite = MutableBlockSoundGroupOverwrite(
                    vanillaId("grass_block"),
                    MutableSoundType(
                        100F,
                        1F,
                        SoundEvents.HORSE_DEATH,
                        SoundEvents.HORSE_DEATH,
                        SoundEvents.HORSE_DEATH,
                        SoundEvents.HORSE_DEATH,
                        SoundEvents.HORSE_DEATH
                    )
                ) { true }
                val overwrite: MutableBlockSoundGroupOverwrite = element ?: defaultOverwrite
                val soundOverwrite = overwrite.soundOverwrite
                makeMultiElementEntry(
                    text("sound_group_overwrites.sound_group_overwrite"),
                    overwrite,
                    true,

                    EntryBuilder(text("sound_group_overwrites.id"), overwrite.blockId.toString(),
                        "",
                        { newValue -> overwrite.blockId = ResourceLocation(newValue) },
                        tooltip("sound_group_overwrites.id")
                    ).build(entryBuilder),

                    EntryBuilder(text("entity_attribute_amplifiers.entity_name"), entityAttributeAmplifier.entityName,
                        "",
                        { newValue-> entityAttributeAmplifier.entityName = newValue },
                        tooltip("entity_attribute_amplifiers.entity_name")
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
                            { newValue -> soundOverwrite.pitch = newValue }
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

    private fun soundId(sound: SoundEvent): String {
        return BuiltInRegistries.SOUND_EVENT.getKey(sound).location.toString()
    }

    private fun sound(id: String): SoundEvent {
        return BuiltInRegistries.SOUND_EVENT.get(ResourceLocation(id))
    }
}