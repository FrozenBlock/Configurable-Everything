package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.EntityConfig
import net.frozenblock.configurableeverything.config.gui.api.EntryBuilder
import net.frozenblock.configurableeverything.config.gui.api.TypedEntryUtils
import net.frozenblock.configurableeverything.entity.util.AttributeAmplifier
import net.frozenblock.configurableeverything.entity.util.MobEffectHolder
import net.frozenblock.configurableeverything.util.id
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

        /*category.addEntry(NestedListListEntry<EntityAttributeAmplifier, MultiElementListEntry<EntityAttributeAmplifier>>(
            Component.literal("Test"),
            config.entityAttributeAmplifiers?.value ?: defaultConfig.entityAttributeAmplifiers!!.value,
            false,
            { Optional.empty() },
            { newValue ->
                config.entityAttributeAmplifiers = TypedEntry(defaultConfig.entityAttributeAmplifiers!!.type, newValue)
            },
            { defaultConfig.entityAttributeAmplifiers!!.value },
            entryBuilder.getResetButtonKey(),
            true,
            true,
            { elem, nestedListListEntry ->
                val newDefault = EntityAttributeAmplifier(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation("example")), "", listOf(
                    AttributeAmplifier(ResourceKey.create(Registries.ATTRIBUTE, ResourceLocation("example")), 1.5)
                ))
                MultiElementListEntry(
                    Component.literal("EntityAttributeAmplifier"), elem ?: newDefault,
                    listOf(
                        MultiElementListEntry(
                            Component.literal("Entity ResourceKey"), ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation("example")),
                            listOf(
                                MultiElementListEntry(
                                    Component.literal("Namespace"), ResourceLocation("example").namespace(),
                                    listOf(
                                        entryBuilder.startStrField(Component.literal("Namespace"), ResourceLocation("example").namespace()).setDefaultValue("example").build(),
                                        entryBuilder.startStrField(Component.literal("Path"), ResourceLocation("example").path()).setDefaultValue("example").build()
                                    ),
                                    true
                                )
                            ),
                            true
                        ),
                        entryBuilder.startStrField(Component.literal("Entity Name"), "").setDefaultValue("").build(),
                        NestedListListEntry(
                            Component.literal("Attribute Amplifiers"),
                            elem?.amplifiers ?: listOf(AttributeAmplifier(ResourceKey.create(Registries.ATTRIBUTE, ResourceLocation("example")), 1.5)),
                            false,
                            { Optional.empty() },
                            { newValue ->
                                elem?.amplifiers = newValue
                            },
                            { listOf(AttributeAmplifier(ResourceKey.create(Registries.ATTRIBUTE, ResourceLocation("example")), 1.5)) },
                            entryBuilder.getResetButtonKey(),
                            true,

                            )
                        )
                    )
                )
            }
        ))*/
    }

    private fun entityAttributeAmplifiers(
        entryBuilder: ConfigEntryBuilder,
        config: EntityConfig,
        defaultConfig: EntityConfig
    ): AbstractConfigListEntry<*> {
        return TypedEntryUtils.makeTypedEntryList(
            entryBuilder,
            Component.literal("Entity Attribute Amplifiers"),
            config::entityAttributeAmplifiers,
            defaultConfig::entityAttributeAmplifiers,
            false,
            Component.literal("Awesome"),
            { newValue -> config.entityAttributeAmplifiers = newValue},
            { element, _ ->
                TypedEntryUtils.makeMultiElementEntry(
                    Component.literal("Entity Attribute Amplifier"),
                    element,
                    true,

                    EntryBuilder(Component.literal("Entity"), element.entity.location().toString(),
                        "",
                        { newValue -> element.entity = ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation(newValue)) },
                        Component.literal("awesome entity bro")
                    ).build(entryBuilder),

                    EntryBuilder(Component.literal("Entity Name"), element.entityName,
                        "",
                        { newValue-> element.entityName = newValue },
                        Component.literal("awesome name bro")
                    ).build(entryBuilder),

                    TypedEntryUtils.makeNestedList(
                        entryBuilder,
                        Component.literal("Amplifiers"),
                        element::amplifiers,
                        { listOf(AttributeAmplifier(ResourceKey.create(Registries.ATTRIBUTE, ResourceLocation("example")), 1.5)) },
                        true,
                        Component.literal("Cool amplifiers!!"),
                        { newValue -> element.amplifiers = newValue },
                        { amplifier, _ ->
                            TypedEntryUtils.makeMultiElementEntry(
                                Component.literal("Attribute Amplifier"),
                                amplifier,
                                true,

                                EntryBuilder(Component.literal("Attribute"), amplifier.attribute.location().toString(),
                                    "minecraft:generic.movement_speed",
                                    { newValue -> amplifier.attribute = ResourceKey.create(Registries.ATTRIBUTE, ResourceLocation(newValue)) },
                                    Component.literal("Attribute ResourceKey")
                                ).build(entryBuilder),

                                EntryBuilder(Component.literal("Amplifier"), amplifier.amplifier,
                                    1.0,
                                    { newValue -> amplifier.amplifier = newValue },
                                    Component.literal("Amplifier")
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
                TypedEntryUtils.makeMultiElementEntry(
                    Component.literal("Experience Override"),
                    element,
                    true,

                    EntryBuilder(Component.literal("Entity"), element.entity.location().toString(),
                        "",
                        { newValue -> element.entity = ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation(newValue)) },
                        Component.literal("Entity ResourceKey")
                    ).build(entryBuilder),

                    EntryBuilder(Component.literal("Amount"), element.amount,
                        0,
                        { newValue -> element.amount = newValue },
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
                TypedEntryUtils.makeMultiElementEntry(
                    Component.literal("Entity FlyBy Sound"),
                    element,
                    true,

                    EntryBuilder(Component.literal("Entity"), element.entity.toString(),
                        "",
                        { newValue -> element.entity = ResourceLocation(newValue) },
                        Component.literal("COOL ENTITY NGL"),
                        requiresRestart = true
                    ).build(entryBuilder),

                    TypedEntryUtils.makeMultiElementEntry(
                        Component.literal("Entity FlyBy Sound Data"),
                        element.sound,
                        true,

                        EntryBuilder(Component.literal("Category"), element.sound.category,
                            "",
                            { newValue -> element.sound.category = newValue },
                            Component.literal("The category the sound is played in"),
                            requiresRestart = true
                        ).build(entryBuilder),

                        EntryBuilder(Component.literal("Sound"), element.sound.sound.toString(),
                            "",
                            { newValue -> element.sound.sound = ResourceLocation(newValue) },
                            Component.literal("The sound to play"),
                            requiresRestart = true
                        ).build(entryBuilder),

                        EntryBuilder(Component.literal("Volume"), element.sound.volume,
                            0.6F,
                            { newValue -> element.sound.volume = newValue },
                            Component.literal("The volume of the sound"),
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
            Component.literal("Entity Hurt Effects"),
            config::entityHurtEffects,
            defaultConfig::entityHurtEffects,
            false,
            Component.literal("Awesome"),
            { newValue -> config.entityHurtEffects = newValue},
            { element, _ ->
                TypedEntryUtils.makeMultiElementEntry(
                    Component.literal("Entity Hurt Effects"),
                    element,
                    true,
                    EntryBuilder(Component.literal("Entity"), element.entity.location().toString(),
                        "",
                        { newValue -> element.entity = ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation(newValue)) },
                        Component.literal("Entity ResourceKey")
                    ).build(entryBuilder),

                    EntryBuilder(Component.literal("Entity Name"), element.entityName,
                        "",
                        { newValue-> element.entityName = newValue },
                        Component.literal("awesome name bro")
                    ).build(entryBuilder),

                    TypedEntryUtils.makeNestedList(
                        entryBuilder,
                        Component.literal("Hurt Effects"),
                        element::effects,
                        { listOf(MobEffectHolder(ResourceKey.create(Registries.MOB_EFFECT, ResourceLocation("speed")), 5, 10, true, true, true)) },
                        true,
                        Component.literal("Cool effects!!"),
                        { newValue -> element.effects = newValue },
                        { effect, _ ->
                            TypedEntryUtils.makeMultiElementEntry(
                                Component.literal("Hurt Effect"),
                                effect,
                                true,

                                EntryBuilder(Component.literal("Effect"), effect.effect?.location().toString(),
                                    "minecraft:generic.movement_speed",
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
                                    false,
                                    { newValue -> effect.ambient = newValue },
                                    Component.literal("nice ambient bro")
                                ).build(entryBuilder),

                                EntryBuilder(Component.literal("Visible"), effect.visible ?: false,
                                    false,
                                    { newValue -> effect.visible = newValue },
                                    Component.literal("nice visible bro")
                                ).build(entryBuilder),

                                EntryBuilder(Component.literal("Show Icon"), effect.showIcon ?: false,
                                    false,
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
                TypedEntryUtils.makeMultiElementEntry(
                    Component.literal("Entity Spotting Icon"),
                    element,
                    true,

                    EntryBuilder(Component.literal("Entity"), element.entity.location().toString(),
                        "",
                        { newValue -> element.entity = ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation(newValue)) },
                        Component.literal("COOL ENTITY NGL"),
                        requiresRestart = true
                    ).build(entryBuilder),

                    EntryBuilder(Component.literal("Texture"), element.texture.toString(),
                        "",
                        { newValue -> element.texture = ResourceLocation(newValue) },
                        Component.literal("The icon to display"),
                        requiresRestart = true
                    ).build(entryBuilder),

                    EntryBuilder(Component.literal("Start Fade"), element.startFade,
                        5F,
                        { newValue -> element.startFade = newValue },
                        Component.literal("The distance at which the icon starts to fade"),
                        requiresRestart = true
                    ).build(entryBuilder),

                    EntryBuilder(Component.literal("End Fade"), element.endFade,
                        8F,
                        { newValue -> element.endFade = newValue },
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
