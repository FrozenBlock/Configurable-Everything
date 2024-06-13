@file:Environment(EnvType.CLIENT)
@file:Suppress("UnstableApiUsage")

package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.clothconfig2.api.Requirement
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.EntityConfig
import net.frozenblock.configurableeverything.entity.util.*
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.string
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.configurableeverything.util.tooltip
import net.frozenblock.lib.config.api.client.gui.*
import net.frozenblock.lib.config.clothconfig.FrozenClothConfig
import net.frozenblock.lib.config.clothconfig.synced
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

private val configInstance = EntityConfig

private inline val mainToggleReq: Requirement
    get() = Requirement.isTrue(MainConfigGui.INSTANCE!!.entity)

object EntityConfigGui {

    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        val config = configInstance.instance()
        val defaultConfig = configInstance.defaultInstance()
        category.background = id("textures/config/entity.png")

        category.addEntry(entityAttributeAmplifiers(entryBuilder, config, defaultConfig))
        category.addEntry(experienceOverrides(entryBuilder, config, defaultConfig))
        category.addEntry(entityFlyBySounds(entryBuilder, config, defaultConfig))
        category.addEntry(entityHurtEffects(entryBuilder, config, defaultConfig))
        category.addEntry(entitySpottingIcons(entryBuilder, config, defaultConfig))

        val player = config.player
        val digSpeedAmplifier = EntryBuilder(text("dig_speed_amplifier"), Slider(player.digSpeedAmplifier, 1, 5000, SliderType.INT),
            Slider(defaultConfig.player.digSpeedAmplifier, 1, 5000, SliderType.INT),
            { newValue -> player.digSpeedAmplifier = newValue.value.toInt() },
            tooltip("dig_speed_amplifier"),
            requirement = mainToggleReq,
        ).build(entryBuilder).synced(
            player::class,
            "digSpeedAmplifier",
            configInstance
        )

        val playerCategory = FrozenClothConfig.createSubCategory(
            entryBuilder, category, text("player"),
            false,
            tooltip("player"),
            digSpeedAmplifier
        )

        val zombie = config.zombie
        val babyZombieSprint = EntryBuilder(text("baby_zombie_sprint_particles"), zombie.babyZombieSprintParticles,
            defaultConfig.zombie.babyZombieSprintParticles,
            { newValue -> zombie.babyZombieSprintParticles = newValue },
            tooltip("baby_zombie_sprint_particles"),
            requirement = mainToggleReq,
        ).build(entryBuilder)

        val zombiesAvoidSun = EntryBuilder(text("zombies_avoid_sun"), zombie.zombiesAvoidSun,
            defaultConfig.zombie.zombiesAvoidSun,
            { newValue -> zombie.zombiesAvoidSun = newValue },
            tooltip("zombies_avoid_sun"),
            requirement = mainToggleReq,
        ).build(entryBuilder).synced(
            zombie::class,
            "zombiesAvoidSun",
            configInstance
        )

        val ignoreZombieDoorBreakDifficulty = EntryBuilder(text("ignore_zombie_door_break_difficulty"), zombie.ignoreDoorBreakDifficulty,
            defaultConfig.zombie.ignoreDoorBreakDifficulty,
            { newValue -> zombie.ignoreDoorBreakDifficulty = newValue },
            tooltip("ignore_zombie_door_break_difficulty"),
            requirement = mainToggleReq,
        ).build(entryBuilder).synced(
            zombie::class,
            "ignoreDoorBreakDifficulty",
            configInstance
        )

        val allZombiesBreakDoors = EntryBuilder(text("all_zombies_break_doors"), zombie.allZombiesBreakDoors,
            defaultConfig.zombie.allZombiesBreakDoors,
            { newValue -> zombie.allZombiesBreakDoors = newValue },
            tooltip("all_zombies_break_doors"),
            requirement = mainToggleReq,
        ).build(entryBuilder).synced(
            zombie::class,
            "allZombiesBreakDoors",
            configInstance
        )

        val ignoreZombieReinforcementDifficulty = EntryBuilder(text("ignore_zombie_reinforcement_difficulty"), zombie.ignoreReinforcementDifficulty,
            defaultConfig.zombie.ignoreReinforcementDifficulty,
            { newValue -> zombie.ignoreReinforcementDifficulty = newValue },
            tooltip("ignore_zombie_reinforcement_difficulty"),
            requirement = mainToggleReq,
        ).build(entryBuilder).synced(
            zombie::class,
            "ignoreReinforcementDifficulty",
            configInstance
        )

        val fullZombieReinforcementChance = EntryBuilder(text("full_zombie_reinforcement_chance"), zombie.fullReinforcementChance,
            defaultConfig.zombie.fullReinforcementChance,
            { newValue -> zombie.fullReinforcementChance = newValue },
            tooltip("full_zombie_reinforcement_chance"),
            requirement = mainToggleReq,
        ).build(entryBuilder).synced(
            zombie::class,
            "fullReinforcementChance",
            configInstance
        )

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
        val skeletonAccuracyIgnoresDifficulty = EntryBuilder(text("skeleton_accuracy_ignores_difficulty"), skeleton.skeletonAccuracyIgnoresDifficulty,
            defaultConfig.skeleton.skeletonAccuracyIgnoresDifficulty,
            { newValue -> skeleton.skeletonAccuracyIgnoresDifficulty = newValue },
            tooltip("skeleton_accuracy_ignores_difficulty"),
            requirement = mainToggleReq,
        ).build(entryBuilder).synced(
            skeleton::class,
            "skeletonAccuracyIgnoresDifficulty",
            configInstance
        )

        val skeletonsAvoidSun = EntryBuilder(text("skeletons_avoid_sun"), skeleton.skeletonsAvoidSun,
            defaultConfig.skeleton.skeletonsAvoidSun,
            { newValue -> skeleton.skeletonsAvoidSun = newValue },
            tooltip("skeletons_avoid_sun"),
            requirement = mainToggleReq,
        ).build(entryBuilder).synced(
            skeleton::class,
            "skeletonsAvoidSun",
            configInstance
        )

        val skeletonCategory = FrozenClothConfig.createSubCategory(
            entryBuilder, category, text("skeleton"),
            false,
            tooltip("skeleton"),
            skeletonAccuracyIgnoresDifficulty, skeletonsAvoidSun
        )

        val flamingArrowsLightFire = EntryBuilder(text("flaming_arrows_light_fire"), config.flamingArrowsLightFire,
            defaultConfig.flamingArrowsLightFire,
            { newValue -> config.flamingArrowsLightFire = newValue },
            tooltip("flaming_arrows_light_fire"),
            requirement = mainToggleReq,
        ).build(entryBuilder).apply {
            category.addEntry(this)
        }.synced(
            config::class,
            "flamingArrowsLightFire",
            configInstance
        )
    }
}

private fun entityAttributeAmplifiers(
    entryBuilder: ConfigEntryBuilder,
    config: EntityConfig,
    defaultConfig: EntityConfig
): AbstractConfigListEntry<*> {
    return typedEntryList(
        entryBuilder,
        text("entity_attribute_amplifiers"),
        config::entityAttributeAmplifiers,
        { defaultConfig.entityAttributeAmplifiers },
        false,
        tooltip("entity_attribute_amplifiers"),
        { newValue -> config.entityAttributeAmplifiers = newValue},
        { element: EntityAttributeAmplifier?, _ ->
            val defaultEntity = ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace(""))
            val entityAttributeAmplifier = element ?: EntityAttributeAmplifier(defaultEntity, "", mutableListOf(AttributeAmplifier(ResourceKey.create(Registries.ATTRIBUTE, ResourceLocation.withDefaultNamespace("")), 1.5)))
            multiElementEntry(
                text("entity_attribute_amplifiers.entity_attribute_amplifier"),
                entityAttributeAmplifier,
                true,

                EntryBuilder(text("entity_attribute_amplifiers.entity"), entityAttributeAmplifier.entity.location().toString(),
                    "",
                    { newValue -> entityAttributeAmplifier.entity = ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse(newValue)) },
                    tooltip("entity_attribute_amplifiers.entity")
                ).build(entryBuilder),

                EntryBuilder(text("entity_attribute_amplifiers.entity_name"), entityAttributeAmplifier.entityName,
                    "",
                    { newValue-> entityAttributeAmplifier.entityName = newValue },
                    tooltip("entity_attribute_amplifiers.entity_name")
                ).build(entryBuilder),

                nestedList(
                    entryBuilder,
                    text("entity_attribute_amplifiers.amplifiers"),
                    entityAttributeAmplifier::amplifiers,
                    { mutableListOf(AttributeAmplifier(ResourceKey.create(Registries.ATTRIBUTE, ResourceLocation.parse("minecraft:generic.movement_speed")), 1.0)) },
                    true,
                    tooltip("entity_attribute_amplifiers.amplifiers"),
                    { newValue -> entityAttributeAmplifier.amplifiers = newValue },
                    { amplifierElement: AttributeAmplifier?, _ ->
                        val amplifier = amplifierElement ?: AttributeAmplifier(ResourceKey.create(Registries.ATTRIBUTE, ResourceLocation.parse("")), 1.5)
                        val attribute = amplifier.attribute
                        val numAmplifier = amplifier.amplifier
                        multiElementEntry(
                            text("entity_attribute_amplifiers.attribute_amplifier"),
                            amplifier,
                            true,

                            EntryBuilder(text("entity_attribute_amplifiers.attribute"), attribute.location().toString(),
                                "minecraft:generic.movement_speed",
                                { newValue -> amplifier.attribute = ResourceKey.create(Registries.ATTRIBUTE, ResourceLocation.parse(newValue)) },
                                tooltip("entity_attribute_amplifiers.attribute")
                            ).build(entryBuilder),

                            EntryBuilder(text("entity_attribute_amplifiers.amplifier"), numAmplifier,
                                1.0,
                                { newValue -> amplifier.amplifier = newValue },
                                tooltip("entity_attribute_amplifiers.amplifier")
                            ).build(entryBuilder)
                        )
                    }
                )
            )
        }
    ).apply {
        this.requirement = mainToggleReq
    }.synced(
        config::class,
        "entityAttributeAmplifiers",
        configInstance
    )
}

private fun experienceOverrides(
    entryBuilder: ConfigEntryBuilder,
    config: EntityConfig,
    defaultConfig: EntityConfig
): AbstractConfigListEntry<*> {
    return typedEntryList(
        entryBuilder,
        text("entity_xp_overrides"),
        config::experienceOverrides,
        { defaultConfig.experienceOverrides },
        false,
        tooltip("entity_xp_overrides"),
        { newValue -> config.experienceOverrides = newValue},
        { element: ExperienceOverride?, _ ->
            val experienceOverride = element ?: ExperienceOverride(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("")), 0)
            multiElementEntry(
                text("entity_xp_override"),
                experienceOverride,
                true,
                EntryBuilder(text("entity_xp_override.entity"), experienceOverride.entity.location().toString(),
                    "",
                    { newValue -> experienceOverride.entity = ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse(newValue)) },
                    tooltip("entity_xp_override.entity")
                ).build(entryBuilder),

                EntryBuilder(text("entity_xp_override.amount"), experienceOverride.amount,
                    0,
                    { newValue -> experienceOverride.amount = newValue },
                    tooltip("entity_xp_override.amount")
                ).build(entryBuilder)
            )
        }
    ).apply {
        this.requirement = mainToggleReq
    }.synced(
        config::class,
        "experienceOverrides",
        configInstance
    )
}

private fun entityFlyBySounds(
    entryBuilder: ConfigEntryBuilder,
    config: EntityConfig,
    defaultConfig: EntityConfig
): AbstractConfigListEntry<*> {
    return typedEntryList(
        entryBuilder,
        text("entity_flyby_sounds"),
        config::entityFlyBySounds,
        { defaultConfig.entityFlyBySounds },
        false,
        tooltip("entity_flyby_sounds"),
        { newValue -> config.entityFlyBySounds = newValue},
        { element: EntityFlyBySound?, _ ->
            val entityFlyBySound = element ?: EntityFlyBySound(ResourceLocation.withDefaultNamespace(""), EntityFlyBySoundData("neutral", id("flyby.arrow"), 0.6F, 1F))
            multiElementEntry(
                text("entity_flyby_sound"),
                entityFlyBySound,
                true,

                EntryBuilder(text("entity_flyby_sound.entity"), entityFlyBySound.entity.toString(),
                    "minecraft:",
                    { newValue -> entityFlyBySound.entity = ResourceLocation.parse(newValue) },
                    tooltip("entity_flyby_sound.entity"),
                    requiresRestart = true
                ).build(entryBuilder),

                multiElementEntry(
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
                        { newValue -> entityFlyBySound.sound.sound = ResourceLocation.parse(newValue) },
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
    ).apply {
        this.requirement = mainToggleReq
    }.synced(
        config::class,
        "entityFlyBySounds",
        configInstance
    )
}

private fun entityHurtEffects(
    entryBuilder: ConfigEntryBuilder,
    config: EntityConfig,
    defaultConfig: EntityConfig
): AbstractConfigListEntry<*> {
    return typedEntryList(
        entryBuilder,
        text("entity_hurt_effects"),
        config::entityHurtEffects,
        { defaultConfig.entityHurtEffects },
        false,
        tooltip("entity_hurt_effects"),
        { newValue -> config.entityHurtEffects = newValue},
        { element: EntityHurtEffects?, _ ->
            val entityHurtEffect = element ?: EntityHurtEffects(ResourceLocation.withDefaultNamespace(""), "", mutableListOf(MobEffectHolder(ResourceKey.create(Registries.MOB_EFFECT, ResourceLocation.parse("minecraft:speed")), 0, 0, true, true, true)))
            multiElementEntry(
                text("entity_hurt_effects.dropdown"),
                entityHurtEffect,
                true,
                EntryBuilder(text("entity_hurt_effects.entity"), entityHurtEffect.entity.toString(),
                    "minecraft:",
                    { newValue -> entityHurtEffect.entity = ResourceLocation.parse(newValue) },
                    tooltip("entity_hurt_effects.entity")
                ).build(entryBuilder),

                EntryBuilder(text("entity_hurt_effects.entity_name"), entityHurtEffect.entityName,
                    "",
                    { newValue-> entityHurtEffect.entityName = newValue },
                    tooltip("entity_hurt_effects.entity_name")
                ).build(entryBuilder),

                nestedList(
                    entryBuilder,
                    text("entity_hurt_effects.hurt_effects"),
                    entityHurtEffect::effects,
                    { mutableListOf(MobEffectHolder(ResourceKey.create(Registries.MOB_EFFECT, ResourceLocation.withDefaultNamespace("speed")), 5, 10, true, true, true)) },
                    true,
                    tooltip("entity_hurt_effects.hurt_effects"),
                    { newValue -> entityHurtEffect.effects = newValue },
                    { effect, _ ->
                        multiElementEntry(
                            text("entity_hurt_effects.hurt_effect"),
                            effect,
                            true,

                            EntryBuilder(text("entity_hurt_effects.effect"), effect.effect.location().toString(),
                                "minecraft:speed",
                                { newValue -> effect.effect = ResourceKey.create(Registries.MOB_EFFECT, ResourceLocation.parse(newValue)) },
                                tooltip("entity_hurt_effects.effect")
                            ).build(entryBuilder),

                            EntryBuilder(text("entity_hurt_effects.duration"), effect.duration,
                                0,
                                { newValue -> effect.duration = newValue },
                                tooltip("entity_hurt_effects.duration")
                            ).build(entryBuilder),

                            EntryBuilder(text("entity_hurt_effects.amplifier"), effect.amplifier,
                                0,
                                { newValue -> effect.amplifier = newValue },
                                tooltip("entity_hurt_effects.amplifier")
                            ).build(entryBuilder),

                            EntryBuilder(text("entity_hurt_effects.ambient"), effect.ambient,
                                true,
                                { newValue -> effect.ambient = newValue },
                                tooltip("entity_hurt_effects.ambient")
                            ).build(entryBuilder),

                            EntryBuilder(text("entity_hurt_effects.visible"), effect.visible,
                                true,
                                { newValue -> effect.visible = newValue },
                                tooltip("entity_hurt_effects.visible")
                            ).build(entryBuilder),

                            EntryBuilder(text("entity_hurt_effects.show_icon"), effect.showIcon,
                                true,
                                { newValue -> effect.showIcon = newValue },
                                tooltip("entity_hurt_effects.show_icon")
                            ).build(entryBuilder),
                        )
                    },
                ),
            )
        },
    ).apply {
        this.requirement = mainToggleReq
    }.synced(
        config::class,
        "entityHurtEffects",
        configInstance
    )
}

private fun entitySpottingIcons(
    entryBuilder: ConfigEntryBuilder,
    config: EntityConfig,
    defaultConfig: EntityConfig
): AbstractConfigListEntry<*> {
    return typedEntryList(
        entryBuilder,
        text("entity_spotting_icons"),
        config::entitySpottingIcons,
        { defaultConfig.entitySpottingIcons },
        false,
        tooltip("entity_spotting_icons"),
        { newValue -> config.entitySpottingIcons = newValue},
        { element: EntitySpottingIcon?, _ ->
            val entitySpottingIcon = element ?: EntitySpottingIcon(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("example")), ResourceLocation.withDefaultNamespace("icon"), 5F, 8F)
            multiElementEntry(
                text("entity_spotting_icons.spotting_icon"),
                entitySpottingIcon,
                true,

                EntryBuilder(text("entity_spotting_icons.entity"), entitySpottingIcon.entity.location().toString(),
                    "",
                    { newValue -> entitySpottingIcon.entity = ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse(newValue)) },
                    tooltip("entity_spotting_icons.entity"),
                    requiresRestart = true
                ).build(entryBuilder),

                EntryBuilder(text("entity_spotting_icons.texture"), entitySpottingIcon.texture.toString(),
                    "",
                    { newValue -> entitySpottingIcon.texture = ResourceLocation.parse(newValue) },
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
    ).apply {
        this.requirement = mainToggleReq
    }
}
