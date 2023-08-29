package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.clothconfig2.gui.entries.MultiElementListEntry
import me.shedaniel.clothconfig2.gui.entries.NestedListListEntry
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.EntityConfig
import net.frozenblock.configurableeverything.entity.util.AttributeAmplifier
import net.frozenblock.configurableeverything.entity.util.EntityAttributeAmplifier
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.configurableeverything.util.tooltip
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.clothconfig.FrozenClothConfig
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.Vec3
import java.util.*

@Environment(EnvType.CLIENT)
object EntityConfigGui {
    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        val config = EntityConfig.get()
        val defaultConfig = EntityConfig.getConfigInstance().defaultInstance()
        category.background = id("textures/config/entity.png")

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
}
