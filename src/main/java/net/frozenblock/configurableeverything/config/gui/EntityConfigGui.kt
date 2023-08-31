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
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

@Environment(EnvType.CLIENT)
object EntityConfigGui {
    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        val config = EntityConfig.get()
        val defaultConfig = EntityConfig.getConfigInstance().defaultInstance()
        category.background = id("textures/config/entity.png")

        category.addEntry(entityAttributeAmplifiers(entryBuilder, config, defaultConfig))
        category.addEntry(experienceOverrides(entryBuilder, config, defaultConfig))
        category.addEntry(entityFlyBySounds(entryBuilder, config, defaultConfig))
        category.addEntry(entityHurtEffects(entryBuilder, config, defaultConfig))
        category.addEntry(entitySpottingIcons(entryBuilder, config, defaultConfig))

        val player = config.player
        val digSpeedAmplifier = entryBuilder.startIntSlider(text("dig_speed_amplifier"), player.digSpeedAmplifier, 1, 5000)
                .setDefaultValue(100)
                .setSaveConsumer { newValue: Int? -> player.digSpeedAmplifier = newValue!! }
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
            entryBuilder.startBooleanToggle(text("baby_zombie_sprint_particles"), zombie.babyZombieSprintParticles)
                .setDefaultValue(false)
                .setSaveConsumer { newValue: Boolean? -> zombie.babyZombieSprintParticles = newValue!! }
                .setTooltip(tooltip("baby_zombie_sprint_particles"))
                .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
                .build()

        val zombiesAvoidSun = entryBuilder.startBooleanToggle(text("zombies_avoid_sun"), zombie.zombiesAvoidSun)
            .setDefaultValue(false)
            .setSaveConsumer { newValue: Boolean? -> zombie.zombiesAvoidSun = newValue!! }
            .setTooltip(tooltip("zombies_avoid_sun"))
            .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
            .build()

        val ignoreZombieDoorBreakDifficulty = entryBuilder.startBooleanToggle(text("ignore_zombie_door_break_difficulty"), zombie.ignoreDoorBreakDifficulty)
            .setDefaultValue(false)
            .setSaveConsumer { newValue: Boolean? -> zombie.ignoreDoorBreakDifficulty = newValue!! }
            .setTooltip(tooltip("ignore_zombie_door_break_difficulty"))
            .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
            .build()

        val allZombiesBreakDoors = entryBuilder.startBooleanToggle(text("all_zombies_break_doors"), zombie.allZombiesBreakDoors)
                .setDefaultValue(false)
                .setSaveConsumer { newValue: Boolean? -> zombie.allZombiesBreakDoors = newValue!! }
                .setTooltip(tooltip("all_zombies_break_doors"))
                .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
                .build()

        val ignoreZombieReinforcementDifficulty = entryBuilder.startBooleanToggle(text("ignore_zombie_reinforcement_difficulty"), zombie.ignoreReinforcementDifficulty)
            .setDefaultValue(false)
            .setSaveConsumer { newValue: Boolean? -> zombie.ignoreReinforcementDifficulty = newValue!! }
            .setTooltip(tooltip("ignore_zombie_reinforcement_difficulty"))
            .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
            .build()

        val fullZombieReinforcementChance = entryBuilder.startBooleanToggle(text("full_zombie_reinforcement_chance"), zombie.fullReinforcementChance)
                .setDefaultValue(false)
                .setSaveConsumer { newValue: Boolean? -> zombie.fullReinforcementChance = newValue!! }
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
        val skeletonAccuracyIgnoresDifficulty = entryBuilder.startBooleanToggle(text("skeleton_accuracy_ignores_difficulty"), skeleton.skeletonAccuracyIgnoresDifficulty)
            .setDefaultValue(false)
            .setSaveConsumer { newValue: Boolean? -> skeleton.skeletonAccuracyIgnoresDifficulty = newValue!! }
            .setTooltip(tooltip("skeleton_accuracy_ignores_difficulty"))
            .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
            .build()

        val skeletonsAvoidSun = entryBuilder.startBooleanToggle(text("skeletons_avoid_sun"), skeleton.skeletonsAvoidSun)
            .setDefaultValue(true)
            .setSaveConsumer { newValue: Boolean? -> skeleton.skeletonsAvoidSun = newValue!! }
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
            entryBuilder.startBooleanToggle(text("flaming_arrows_light_fire"), config.flamingArrowsLightFire)
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
            defaultConfig::entityAttributeAmplifiers,
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
            Component.literal("Experience Overrides"),
            config::experienceOverrides,
            defaultConfig::experienceOverrides,
            false,
            Component.literal("Awesome"),
            { newValue -> config.experienceOverrides = newValue},
            { element, _ ->
                val experienceOverride = element ?: ExperienceOverride(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation("")), 0)
                TypedEntryUtils.makeMultiElementEntry(
                    Component.literal("Experience Override"),
                    experienceOverride,
                    true,

                    EntryBuilder(Component.literal("Entity"), experienceOverride.entity.location().toString(),
                        "",
                        { newValue -> experienceOverride.entity = ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation(newValue)) },
                        Component.literal("Entity ResourceKey")
                    ).build(entryBuilder),

                    EntryBuilder(Component.literal("Amount"), experienceOverride.amount,
                        0,
                        { newValue -> experienceOverride.amount = newValue },
                        Component.literal("the amount of xp to drop")
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
            Component.literal("Entity FlyBy Sounds"),
            config::entityFlyBySounds,
            defaultConfig::entityFlyBySounds,
            false,
            Component.literal("Awesome"),
            { newValue -> config.entityFlyBySounds = newValue},
            { element, _ ->
                val entityFlyBySound = element ?: EntityFlyBySound(ResourceLocation(""), EntityFlyBySoundData("neutral", id("flyby.arrow"), 0.6F, 1F))
                TypedEntryUtils.makeMultiElementEntry(
                    Component.literal("Entity FlyBy Sound"),
                    entityFlyBySound,
                    true,

                    EntryBuilder(Component.literal("Entity"), entityFlyBySound.entity.toString(),
                        "minecraft:",
                        { newValue -> entityFlyBySound.entity = ResourceLocation(newValue) },
                        Component.literal("COOL ENTITY NGL"),
                        requiresRestart = true
                    ).build(entryBuilder),

                    TypedEntryUtils.makeMultiElementEntry(
                        Component.literal("Entity FlyBy Sound Data"),
                        entityFlyBySound.sound,
                        true,

                        EntryBuilder(Component.literal("Category"), entityFlyBySound.sound.category,
                            "neutral",
                            { newValue -> entityFlyBySound.sound.category = newValue },
                            Component.literal("The category the sound is played in"),
                            requiresRestart = true
                        ).build(entryBuilder),

                        EntryBuilder(Component.literal("Sound"), entityFlyBySound.sound.sound.toString(),
                            string("flyby.arrow"),
                            { newValue -> entityFlyBySound.sound.sound = ResourceLocation(newValue) },
                            Component.literal("The sound to play"),
                            requiresRestart = true
                        ).build(entryBuilder),

                        EntryBuilder(Component.literal("Volume"), entityFlyBySound.sound.volume,
                            0.6F,
                            { newValue -> entityFlyBySound.sound.volume = newValue },
                            Component.literal("The volume of the sound"),
                            requiresRestart = true
                        ).build(entryBuilder),

                        EntryBuilder(Component.literal("Pitch"), entityFlyBySound.sound.pitch,
                            1.0F,
                            { newValue -> entityFlyBySound.sound.pitch = newValue },
                            Component.literal("The pitch of the sound"),
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
            defaultConfig::entityHurtEffects,
            false,
            tooltip("entity_hurt_effects"),
            { newValue -> config.entityHurtEffects = newValue},
            { element, _ ->
                val entityHurtEffect = element ?: EntityHurtEffects(ResourceLocation(""), "", listOf(MobEffectHolder(ResourceKey.create(Registries.MOB_EFFECT, ResourceLocation("minecraft:speed")), 0, 0, true, true, true)))
                TypedEntryUtils.makeMultiElementEntry(
                    Component.literal("Entity Hurt Effects"),
                    entityHurtEffect,
                    true,
                    EntryBuilder(Component.literal("Entity"), entityHurtEffect.entity.toString(),
                        "minecraft:",
                        { newValue -> entityHurtEffect.entity = ResourceLocation(newValue) },
                        Component.literal("Entity ResourceKey")
                    ).build(entryBuilder),

                    EntryBuilder(Component.literal("Entity Name"), entityHurtEffect.entityName,
                        "",
                        { newValue-> entityHurtEffect.entityName = newValue },
                        Component.literal("awesome name bro")
                    ).build(entryBuilder),

                    TypedEntryUtils.makeNestedList(
                        entryBuilder,
                        Component.literal("Hurt Effects"),
                        entityHurtEffect::effects,
                        { listOf(MobEffectHolder(ResourceKey.create(Registries.MOB_EFFECT, ResourceLocation("speed")), 5, 10, true, true, true)) },
                        true,
                        Component.literal("Cool effects!!"),
                        { newValue -> entityHurtEffect.effects = newValue },
                        { effect, _ ->
                            TypedEntryUtils.makeMultiElementEntry(
                                Component.literal("Hurt Effect"),
                                effect,
                                true,

                                EntryBuilder(Component.literal("Effect"), effect.effect?.location().toString(),
                                    "minecraft:speed",
                                    { newValue -> effect.effect = ResourceKey.create(Registries.MOB_EFFECT, ResourceLocation(newValue)) },
                                    Component.literal("Mob Effect ID")
                                ).build(entryBuilder),

                                EntryBuilder(Component.literal("Duration"), effect.duration ?: 0,
                                    0,
                                    { newValue -> effect.duration = newValue },
                                    Component.literal("nice duration bro")
                                ).build(entryBuilder),

                                EntryBuilder(Component.literal("Amplifier"), effect.amplifier ?: 0,
                                    0,
                                    { newValue -> effect.amplifier = newValue },
                                    Component.literal("nice amplifier bro")
                                ).build(entryBuilder),

                                EntryBuilder(Component.literal("Ambient"), effect.ambient ?: false,
                                    true,
                                    { newValue -> effect.ambient = newValue },
                                    Component.literal("nice ambient bro")
                                ).build(entryBuilder),

                                EntryBuilder(Component.literal("Visible"), effect.visible ?: false,
                                    true,
                                    { newValue -> effect.visible = newValue },
                                    Component.literal("nice visible bro")
                                ).build(entryBuilder),

                                EntryBuilder(Component.literal("Show Icon"), effect.showIcon ?: false,
                                    true,
                                    { newValue -> effect.showIcon = newValue },
                                    Component.literal("nice show icon bro"),
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
            Component.literal("Entity Spotting Icons"),
            config::entitySpottingIcons,
            defaultConfig::entitySpottingIcons,
            false,
            Component.literal("Awesome"),
            { newValue -> config.entitySpottingIcons = newValue},
            { element, _ ->
                val entitySpottingIcon = element ?: EntitySpottingIcon(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation("example")), ResourceLocation("icon"), 5F, 8F)
                TypedEntryUtils.makeMultiElementEntry(
                    Component.literal("Entity Spotting Icon"),
                    entitySpottingIcon,
                    true,

                    EntryBuilder(Component.literal("Entity"), entitySpottingIcon.entity.location().toString(),
                        "",
                        { newValue -> entitySpottingIcon.entity = ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation(newValue)) },
                        Component.literal("COOL ENTITY NGL"),
                        requiresRestart = true
                    ).build(entryBuilder),

                    EntryBuilder(Component.literal("Texture"), entitySpottingIcon.texture.toString(),
                        "",
                        { newValue -> entitySpottingIcon.texture = ResourceLocation(newValue) },
                        Component.literal("The icon to display"),
                        requiresRestart = true
                    ).build(entryBuilder),

                    EntryBuilder(Component.literal("Start Fade"), entitySpottingIcon.startFade,
                        5F,
                        { newValue -> entitySpottingIcon.startFade = newValue },
                        Component.literal("The distance at which the icon starts to fade"),
                        requiresRestart = true
                    ).build(entryBuilder),

                    EntryBuilder(Component.literal("End Fade"), entitySpottingIcon.endFade,
                        8F,
                        { newValue -> entitySpottingIcon.endFade = newValue },
                        Component.literal("The distance at which the icon is fully faded"),
                        requiresRestart = true
                    ).build(entryBuilder),
                    requiresRestart = true
                )
            },
            requiresRestart = true
        )
    }
}
