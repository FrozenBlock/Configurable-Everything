package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.EntityConfig
import net.frozenblock.configurableeverything.config.gui.api.EntryBuilder
import net.frozenblock.configurableeverything.config.gui.api.TypedEntryUtils
import net.frozenblock.configurableeverything.entity.util.*
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.string
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.configurableeverything.util.tooltip
import net.frozenblock.lib.config.clothconfig.FrozenClothConfig
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

@Environment(EnvType.CLIENT)
object EntityConfigGui {
    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        val config = EntityConfig.get()
        val defaultConfig = EntityConfig.INSTANCE.defaultInstance()
        category.background = id("textures/config/entity.png")

        category.addEntry(entityAttributeAmplifiers(entryBuilder, config, defaultConfig))
        category.addEntry(experienceOverrides(entryBuilder, config, defaultConfig))
        category.addEntry(entityFlyBySounds(entryBuilder, config, defaultConfig))
        category.addEntry(entityHurtEffects(entryBuilder, config, defaultConfig))
        category.addEntry(entitySpottingIcons(entryBuilder, config, defaultConfig))

        val player = config.player
        val digSpeedAmplifier = entryBuilder.startIntSlider(text("dig_speed_amplifier"), player?.digSpeedAmplifier ?: defaultConfig.player!!.digSpeedAmplifier!!, 1, 5000)
                .setDefaultValue(100)
                .setSaveConsumer { newValue: Int? -> player?.digSpeedAmplifier = newValue!! }
                .setTooltip(tooltip("dig_speed_amplifier"))
                .build()

        val playerCategory = FrozenClothConfig.createSubCategory(
            entryBuilder, category, text("player"),
            false,
            tooltip("player"),
            digSpeedAmplifier
        )

        val zombie = config.zombie
        val babyZombieSprint =
            entryBuilder.startBooleanToggle(text("baby_zombie_sprint_particles"), zombie?.babyZombieSprintParticles ?: defaultConfig.zombie!!.babyZombieSprintParticles!!)
                .setDefaultValue(false)
                .setSaveConsumer { newValue: Boolean? -> zombie?.babyZombieSprintParticles = newValue!! }
                .setTooltip(tooltip("baby_zombie_sprint_particles"))
                .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
                .build()

        val zombiesAvoidSun = entryBuilder.startBooleanToggle(text("zombies_avoid_sun"), zombie?.zombiesAvoidSun ?: defaultConfig.zombie!!.zombiesAvoidSun!!)
            .setDefaultValue(false)
            .setSaveConsumer { newValue: Boolean? -> zombie?.zombiesAvoidSun = newValue!! }
            .setTooltip(tooltip("zombies_avoid_sun"))
            .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
            .build()

        val ignoreZombieDoorBreakDifficulty = entryBuilder.startBooleanToggle(text("ignore_zombie_door_break_difficulty"), zombie?.ignoreDoorBreakDifficulty ?: defaultConfig.zombie!!.ignoreDoorBreakDifficulty!!)
            .setDefaultValue(false)
            .setSaveConsumer { newValue: Boolean? -> zombie?.ignoreDoorBreakDifficulty = newValue!! }
            .setTooltip(tooltip("ignore_zombie_door_break_difficulty"))
            .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
            .build()

        val allZombiesBreakDoors = entryBuilder.startBooleanToggle(text("all_zombies_break_doors"), zombie?.allZombiesBreakDoors ?: defaultConfig.zombie!!.allZombiesBreakDoors!!)
                .setDefaultValue(false)
                .setSaveConsumer { newValue: Boolean? -> zombie?.allZombiesBreakDoors = newValue!! }
                .setTooltip(tooltip("all_zombies_break_doors"))
                .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
                .build()

        val ignoreZombieReinforcementDifficulty = entryBuilder.startBooleanToggle(text("ignore_zombie_reinforcement_difficulty"), zombie?.ignoreReinforcementDifficulty ?: defaultConfig.zombie!!.ignoreReinforcementDifficulty!!)
            .setDefaultValue(false)
            .setSaveConsumer { newValue: Boolean? -> zombie?.ignoreReinforcementDifficulty = newValue!! }
            .setTooltip(tooltip("ignore_zombie_reinforcement_difficulty"))
            .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
            .build()

        val fullZombieReinforcementChance = entryBuilder.startBooleanToggle(text("full_zombie_reinforcement_chance"), zombie?.fullReinforcementChance ?: defaultConfig.zombie!!.fullReinforcementChance!!)
                .setDefaultValue(false)
                .setSaveConsumer { newValue: Boolean? -> zombie?.fullReinforcementChance = newValue!! }
                .setTooltip(tooltip("full_zombie_reinforcement_chance"))
                .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
                .build()

        val zombieCategory = FrozenClothConfig.createSubCategory(
            entryBuilder,
            category,
            text("zombie"),
            false,
            tooltip("zombie"),
            babyZombieSprint,
            zombiesAvoidSun,
            ignoreZombieDoorBreakDifficulty,
            allZombiesBreakDoors,
            ignoreZombieReinforcementDifficulty,
            fullZombieReinforcementChance
        )

        val skeleton = config.skeleton
        val skeletonAccuracyIgnoresDifficulty = entryBuilder.startBooleanToggle(text("skeleton_accuracy_ignores_difficulty"), skeleton?.skeletonAccuracyIgnoresDifficulty ?: defaultConfig.skeleton!!.skeletonAccuracyIgnoresDifficulty!!)
            .setDefaultValue(false)
            .setSaveConsumer { newValue: Boolean? -> skeleton?.skeletonAccuracyIgnoresDifficulty = newValue!! }
            .setTooltip(tooltip("skeleton_accuracy_ignores_difficulty"))
            .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
            .build()

        val skeletonsAvoidSun = entryBuilder.startBooleanToggle(text("skeletons_avoid_sun"), skeleton?.skeletonsAvoidSun ?: defaultConfig.skeleton!!.skeletonsAvoidSun!!)
            .setDefaultValue(true)
            .setSaveConsumer { newValue: Boolean? -> skeleton?.skeletonsAvoidSun = newValue!! }
            .setTooltip(tooltip("skeletons_avoid_sun"))
            .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
            .build()

        val skeletonCategory = FrozenClothConfig.createSubCategory(
            entryBuilder, category, text("skeleton"),
            false,
            tooltip("skeleton"),
            skeletonAccuracyIgnoresDifficulty, skeletonsAvoidSun
        )

        val flamingArrowsLightFire = category.addEntry(
            entryBuilder.startBooleanToggle(text("flaming_arrows_light_fire"), config.flamingArrowsLightFire ?: defaultConfig.flamingArrowsLightFire!!)
                .setDefaultValue(false)
                .setSaveConsumer { newValue: Boolean? -> config.flamingArrowsLightFire = newValue!! }
                .setTooltip(tooltip("flaming_arrows_light_fire"))
                .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
                .build()
        )
    }

    private fun entityAttributeAmplifiers(
        entryBuilder: ConfigEntryBuilder,
        config: EntityConfig,
        defaultConfig: EntityConfig
    ): AbstractConfigListEntry<*> {
        return TypedEntryUtils.makeTypedEntryList(
            entryBuilder,
            text("entity_attribute_amplifiers"),
            config::entityAttributeAmplifiers,
            {defaultConfig.entityAttributeAmplifiers!!},
            false,
            tooltip("entity_attribute_amplifiers"),
            { newValue -> config.entityAttributeAmplifiers = newValue},
            { element, _ ->
                val entityAttributeAmplifier = element ?: EntityAttributeAmplifier(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation("")), "", listOf(AttributeAmplifier(ResourceKey.create(Registries.ATTRIBUTE, ResourceLocation("")), 1.5)))
                TypedEntryUtils.makeMultiElementEntry(
                    text("entity_attribute_amplifiers.entity_attribute_amplifier"),
                    entityAttributeAmplifier,
                    true,

                    EntryBuilder(text("entity_attribute_amplifiers.entity"), entityAttributeAmplifier.entity.location().toString(),
                        "",
                        { newValue -> entityAttributeAmplifier.entity = ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation(newValue)) },
                        tooltip("entity_attribute_amplifiers.entity")
                    ).build(entryBuilder),

                    EntryBuilder(text("entity_attribute_amplifiers.entity_name"), entityAttributeAmplifier.entityName,
                        "",
                        { newValue-> entityAttributeAmplifier.entityName = newValue },
                        tooltip("entity_attribute_amplifiers.entity_name")
                    ).build(entryBuilder),

                    TypedEntryUtils.makeNestedList(
                        entryBuilder,
                        text("entity_attribute_amplifiers.amplifiers"),
                        entityAttributeAmplifier::amplifiers,
                        { listOf(AttributeAmplifier(ResourceKey.create(Registries.ATTRIBUTE, ResourceLocation("minecraft:generic.movement_speed")), 1.0)) },
                        true,
                        tooltip("entity_attribute_amplifiers.amplifiers"),
                        { newValue -> entityAttributeAmplifier.amplifiers = newValue },
                        { amplifier, _ ->
                            TypedEntryUtils.makeMultiElementEntry(
                                text("entity_attribute_amplifiers.attribute_amplifier"),
                                amplifier,
                                true,

                                EntryBuilder(text("entity_attribute_amplifiers.attribute"), amplifier.attribute.location().toString(),
                                    "minecraft:generic.movement_speed",
                                    { newValue -> amplifier.attribute = ResourceKey.create(Registries.ATTRIBUTE, ResourceLocation(newValue)) },
                                    tooltip("entity_attribute_amplifiers.attribute")
                                ).build(entryBuilder),

                                EntryBuilder(text("entity_attribute_amplifiers.amplifier"), amplifier.amplifier,
                                    1.0,
                                    { newValue -> amplifier.amplifier = newValue },
                                    text("entity_attribute_amplifiers.amplifier")
                                ).build(entryBuilder)
                            )
                        }
                    )
                )
            }
        )
    }

    private fun experienceOverrides(
        entryBuilder: ConfigEntryBuilder,
        config: EntityConfig,
        defaultConfig: EntityConfig
    ): AbstractConfigListEntry<*> {
        return TypedEntryUtils.makeTypedEntryList(
            entryBuilder,
            text("entity_xp_overrides"),
            config::experienceOverrides,
            {defaultConfig.experienceOverrides!!},
            false,
            text("entity_xp_overrides"),
            { newValue -> config.experienceOverrides = newValue},
            { element, _ ->
                val experienceOverride = element ?: ExperienceOverride(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation("")), 0)
                TypedEntryUtils.makeMultiElementEntry(
                    text("entity_xp_override"),
                    experienceOverride,
                    true,
                    EntryBuilder(text("entity_xp_override.entity"), experienceOverride.entity.location().toString(),
                        "",
                        { newValue -> experienceOverride.entity = ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation(newValue)) },
                        text("entity_xp_override.entity")
                    ).build(entryBuilder),

                    EntryBuilder(text("entity_xp_override.amount"), experienceOverride.amount,
                        0,
                        { newValue -> experienceOverride.amount = newValue },
                        text("entity_xp_override.amount")
                    ).build(entryBuilder)
                )
            }
        )
    }

    private fun entityFlyBySounds(
        entryBuilder: ConfigEntryBuilder,
        config: EntityConfig,
        defaultConfig: EntityConfig
    ): AbstractConfigListEntry<*> {
        return TypedEntryUtils.makeTypedEntryList(
            entryBuilder,
            text("entity_flyby_sounds"),
            config::entityFlyBySounds,
            {defaultConfig.entityFlyBySounds!!},
            false,
            text("entity_flyby_sounds"),
            { newValue -> config.entityFlyBySounds = newValue},
            { element, _ ->
                val entityFlyBySound = element ?: EntityFlyBySound(ResourceLocation(""), EntityFlyBySoundData("neutral", id("flyby.arrow"), 0.6F, 1F))
                TypedEntryUtils.makeMultiElementEntry(
                    text("entity_flyby_sound"),
                    entityFlyBySound,
                    true,

                    EntryBuilder(text("entity_flyby_sound.entity"), entityFlyBySound.entity.toString(),
                        "minecraft:",
                        { newValue -> entityFlyBySound.entity = ResourceLocation(newValue) },
                        tooltip("entity_flyby_sound.entity"),
                        requiresRestart = true
                    ).build(entryBuilder),

                    TypedEntryUtils.makeMultiElementEntry(
                        text("entity_flyby_sound.data"),
                        entityFlyBySound.sound,
                        true,

                        EntryBuilder(text("entity_flyby_sound.category"), entityFlyBySound.sound.category,
                            "neutral",
                            { newValue -> entityFlyBySound.sound.category = newValue },
                            tooltip("entity_flyby_sound.category"),
                            requiresRestart = true
                        ).build(entryBuilder),

                        EntryBuilder(text("entity_flyby_sound.sound"), entityFlyBySound.sound.sound.toString(),
                            string("flyby.arrow"),
                            { newValue -> entityFlyBySound.sound.sound = ResourceLocation(newValue) },
                            tooltip("entity_flyby_sound.sound"),
                            requiresRestart = true
                        ).build(entryBuilder),

                        EntryBuilder(text("entity_flyby_sound.volume"), entityFlyBySound.sound.volume,
                            0.6F,
                            { newValue -> entityFlyBySound.sound.volume = newValue },
                            tooltip("entity_flyby_sound.volume"),
                            requiresRestart = true
                        ).build(entryBuilder),

                        EntryBuilder(text("entity_flyby_sound.pitch"), entityFlyBySound.sound.pitch,
                            1.0F,
                            { newValue -> entityFlyBySound.sound.pitch = newValue },
                            tooltip("entity_flyby_sound.pitch"),
                            requiresRestart = true
                        ).build(entryBuilder),
                    ),
                    requiresRestart = true
                )
            },
            requiresRestart = true
        )
    }

    private fun entityHurtEffects(
        entryBuilder: ConfigEntryBuilder,
        config: EntityConfig,
        defaultConfig: EntityConfig
    ): AbstractConfigListEntry<*> {
        return TypedEntryUtils.makeTypedEntryList(
            entryBuilder,
            text("entity_hurt_effects"),
            config::entityHurtEffects,
            {defaultConfig.entityHurtEffects!!},
            false,
            tooltip("entity_hurt_effects"),
            { newValue -> config.entityHurtEffects = newValue},
            { element, _ ->
                val entityHurtEffect = element ?: EntityHurtEffects(ResourceLocation(""), "", listOf(MobEffectHolder(ResourceKey.create(Registries.MOB_EFFECT, ResourceLocation("minecraft:speed")), 0, 0, true, true, true)))
                TypedEntryUtils.makeMultiElementEntry(
                    text("entity_hurt_effects.dropdown"),
                    entityHurtEffect,
                    true,
                    EntryBuilder(text("entity_hurt_effects.entity"), entityHurtEffect.entity.toString(),
                        "minecraft:",
                        { newValue -> entityHurtEffect.entity = ResourceLocation(newValue) },
                        tooltip("entity_hurt_effects.entity")
                    ).build(entryBuilder),

                    EntryBuilder(text("entity_hurt_effects.entity_name"), entityHurtEffect.entityName,
                        "",
                        { newValue-> entityHurtEffect.entityName = newValue },
                        tooltip("entity_hurt_effects.entity_name")
                    ).build(entryBuilder),

                    TypedEntryUtils.makeNestedList(
                        entryBuilder,
                        text("entity_hurt_effects.hurt_effects"),
                        entityHurtEffect::effects,
                        { listOf(MobEffectHolder(ResourceKey.create(Registries.MOB_EFFECT, ResourceLocation("speed")), 5, 10, true, true, true)) },
                        true,
                        tooltip("entity_hurt_effects.hurt_effects"),
                        { newValue -> entityHurtEffect.effects = newValue },
                        { effect, _ ->
                            TypedEntryUtils.makeMultiElementEntry(
                                text("entity_hurt_effects.hurt_effect"),
                                effect,
                                true,

                                EntryBuilder(text("entity_hurt_effects.effect"), effect.effect?.location().toString(),
                                    "minecraft:speed",
                                    { newValue -> effect.effect = ResourceKey.create(Registries.MOB_EFFECT, ResourceLocation(newValue)) },
                                    tooltip("entity_hurt_effects.effect")
                                ).build(entryBuilder),

                                EntryBuilder(text("entity_hurt_effects.duration"), effect.duration ?: 0,
                                    0,
                                    { newValue -> effect.duration = newValue },
                                    tooltip("entity_hurt_effects.duration")
                                ).build(entryBuilder),

                                EntryBuilder(text("entity_hurt_effects.amplifier"), effect.amplifier ?: 0,
                                    0,
                                    { newValue -> effect.amplifier = newValue },
                                    tooltip("entity_hurt_effects.amplifier")
                                ).build(entryBuilder),

                                EntryBuilder(text("entity_hurt_effects.ambient"), effect.ambient ?: false,
                                    true,
                                    { newValue -> effect.ambient = newValue },
                                    tooltip("entity_hurt_effects.ambient")
                                ).build(entryBuilder),

                                EntryBuilder(text("entity_hurt_effects.visible"), effect.visible ?: false,
                                    true,
                                    { newValue -> effect.visible = newValue },
                                    tooltip("entity_hurt_effects.visible")
                                ).build(entryBuilder),

                                EntryBuilder(text("entity_hurt_effects.show_icon"), effect.showIcon ?: false,
                                    true,
                                    { newValue -> effect.showIcon = newValue },
                                    tooltip("entity_hurt_effects.show_icon")
                                ).build(entryBuilder),
                            )
                        },
                    ),
                )
            },
        )
    }

    private fun entitySpottingIcons(
        entryBuilder: ConfigEntryBuilder,
        config: EntityConfig,
        defaultConfig: EntityConfig
    ): AbstractConfigListEntry<*> {
        return TypedEntryUtils.makeTypedEntryList(
            entryBuilder,
            text("entity_spotting_icons"),
            config::entitySpottingIcons,
            {defaultConfig.entitySpottingIcons!!},
            false,
            tooltip("entity_spotting_icons"),
            { newValue -> config.entitySpottingIcons = newValue},
            { element, _ ->
                val entitySpottingIcon = element ?: EntitySpottingIcon(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation("example")), ResourceLocation("icon"), 5F, 8F)
                TypedEntryUtils.makeMultiElementEntry(
                    text("entity_spotting_icons.spotting_icon"),
                    entitySpottingIcon,
                    true,

                    EntryBuilder(text("entity_spotting_icons.entity"), entitySpottingIcon.entity.location().toString(),
                        "",
                        { newValue -> entitySpottingIcon.entity = ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation(newValue)) },
                        tooltip("entity_spotting_icons.entity"),
                        requiresRestart = true
                    ).build(entryBuilder),

                    EntryBuilder(text("entity_spotting_icons.texture"), entitySpottingIcon.texture.toString(),
                        "",
                        { newValue -> entitySpottingIcon.texture = ResourceLocation(newValue) },
                        tooltip("entity_spotting_icons.texture"),
                        requiresRestart = true
                    ).build(entryBuilder),

                    EntryBuilder(text("entity_spotting_icons.start_fade"), entitySpottingIcon.startFade,
                        5F,
                        { newValue -> entitySpottingIcon.startFade = newValue },
                        tooltip("entity_spotting_icons.start_fade"),
                        requiresRestart = true
                    ).build(entryBuilder),

                    EntryBuilder(text("entity_spotting_icons.end_fade"), entitySpottingIcon.endFade,
                        8F,
                        { newValue -> entitySpottingIcon.endFade = newValue },
                        tooltip("entity_spotting_icons.end_fade"),
                        requiresRestart = true
                    ).build(entryBuilder),
                    requiresRestart = true
                )
            },
            requiresRestart = true
        )
    }
}
