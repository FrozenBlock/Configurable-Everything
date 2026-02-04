package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.entity.util.*
import net.frozenblock.configurableeverything.util.*
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.v2.entry.ConfigEntry
import net.frozenblock.lib.config.v2.entry.EntryType
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.Identifier

private val ENTITY_ATTRIBUTE_AMPLIFIERS: EntryType<EntityAttributeAmplifier> = EntryType.create(
    EntityAttributeAmplifier.CODEC,
    EntityAttributeAmplifier.STREAM_CODEC,
)

private val ENTITY_FLYBY_SOUNDS: EntryType<EntityFlyBySound> = EntryType.create(
    EntityFlyBySound.CODEC,
    EntityFlyBySound.STREAM_CODEC,
)

private val ENTITY_HURT_EFFECTS: EntryType<EntityHurtEffects> = EntryType.create(
    EntityHurtEffects.CODEC,
    EntityHurtEffects.STREAM_CODEC,
)

private val EXPERIENCE_OVERRIDES: EntryType<ExperienceOverride> = EntryType.create(
    ExperienceOverride.CODEC,
    ExperienceOverride.STREAM_CODEC,
)

private val SPOTTING_ICON: EntryType<EntitySpottingIcon> = EntryType.create(
    EntitySpottingIcon.CODEC,
    EntitySpottingIcon.STREAM_CODEC,
)

object EntityConfig : CEConfig("entity") {
    @JvmField
    var entityAttributeAmplifiers: ConfigEntry<MutableList<EntityAttributeAmplifier>> = this.entry("entityAttributeAmplifiers",
        ENTITY_ATTRIBUTE_AMPLIFIERS.asList(),
        mutableListOf(
            EntityAttributeAmplifier(
                ResourceKey.create(Registries.ENTITY_TYPE, Identifier.withDefaultNamespace("example")),
                "",
                mutableListOf(
                    AttributeAmplifier(
                        ResourceKey.create(
                            Registries.ATTRIBUTE,
                            Identifier.withDefaultNamespace("generic.movement_speed")
                        ),
                        1.5
                    )
                )
            )
        )
    )

    @JvmField
    var experienceOverrides: ConfigEntry<MutableList<ExperienceOverride>> = this.entry("experienceOverrides",
        EXPERIENCE_OVERRIDES.asList(),
        mutableListOf(
            ExperienceOverride(
                ResourceKey.create(Registries.ENTITY_TYPE, Identifier.withDefaultNamespace("example")),
                5000
            )
        )
    )

    @JvmField
    var entityFlyBySounds: ConfigEntry<MutableList<EntityFlyBySound>> = this.entry("entityFlyBySounds",
        ENTITY_FLYBY_SOUNDS.asList(),
        mutableListOf(
            EntityFlyBySound(
                Identifier.parse("minecraft:arrow"),
                EntityFlyBySoundData(
                    "neutral",
                    id("flyby.arrow"),
                    0.6f,
                    1f
                )
            ),
            EntityFlyBySound(
                Identifier.parse("minecraft:tipped_arrow"),
                EntityFlyBySoundData(
                    "neutral",
                    id("flyby.arrow"),
                    0.6f,
                    1f
                )
            ),
            EntityFlyBySound(
                Identifier.parse("minecraft:spectral"),
                EntityFlyBySoundData(
                    "neutral",
                    id("flyby.arrow"),
                    0.6f,
                    1f
                )
            ),
            EntityFlyBySound(
                Identifier.parse("minecraft:trident"),
                EntityFlyBySoundData(
                    "neutral",
                    id("flyby.trident"),
                    0.7f,
                    1f
                )
            ),
            EntityFlyBySound(
                Identifier.parse("minecraft:egg"),
                EntityFlyBySoundData(
                    "neutral",
                    id("flyby.egg"),
                    0.4f,
                    1f
                )
            ),
            EntityFlyBySound(
                Identifier.parse("minecraft:snowball"),
                EntityFlyBySoundData(
                    "neutral",
                    id("flyby.snowball"),
                    0.4f,
                    1f
                )
            ),  //TODO: Burning flyby sound
            EntityFlyBySound(
                Identifier.parse("minecraft:fireball"),
                EntityFlyBySoundData(
                    "neutral",
                    id("flyby.fireball"),
                    0.4f,
                    1f
                )
            ),
            EntityFlyBySound(
                Identifier.parse("minecraft:potion"),
                EntityFlyBySoundData(
                    "neutral",
                    id("flyby.potion"),
                    0.2f,
                    1f
                )
            ),
            EntityFlyBySound(
                Identifier.parse("minecraft:experience_bottle"),
                EntityFlyBySoundData(
                    "neutral",
                    id("flyby.experience_bottle"),
                    0.2f,
                    1f
                )
            )
        )
    )

    @JvmField
    var entityHurtEffects: ConfigEntry<MutableList<EntityHurtEffects>> = this.entry("entityHurtEffects",
        ENTITY_HURT_EFFECTS.asList(),
        mutableListOf(
            EntityHurtEffects(
                Identifier.withDefaultNamespace("cow"),
                "",
                mutableListOf(
                    MobEffectHolder(
                        ResourceKey.create(Registries.MOB_EFFECT, Identifier.withDefaultNamespace("speed")),
                        5,
                        10,
                        true,
                        true,
                        true
                    )
                )
            )
        )
    )

    @JvmField
    // UNSYNCABLE
    var entitySpottingIcons: ConfigEntry<MutableList<EntitySpottingIcon>> = this.unsyncableEntry("entitySpottingIcons",
        SPOTTING_ICON.asList(),
        mutableListOf(
            EntitySpottingIcon(
                ResourceKey.create(Registries.ENTITY_TYPE, Identifier.withDefaultNamespace("example")),
                id("textures/spotting_icon/icon.png"),
                5f,
                8f
            )
        )
    )

    @JvmField
    var flamingArrowsLightFire: ConfigEntry<Boolean> = this.entry("flamingArrowsLightFire",
        EntryType.BOOL,
        false
    )

    // PLAYER

    @JvmField
    var digSpeedAmplifier: ConfigEntry<Int> = this.entry("player/digSpeedAmplifier",
        EntryType.INT,
        100
    )

    inline val digSpeed: Double // acts as a getter method
        get() {
            val amplifier: Double = digSpeedAmplifier.get().toDouble()
            return amplifier / 100.0
        }

    // ZOMBIE

    @JvmField
    val babyZombieSprintParticles: ConfigEntry<Boolean> = this.unsyncableEntry("zombie/babyZombieSprintParticles",
        EntryType.BOOL,
        false
    )

    @JvmField
    val zombiesAvoidSun: ConfigEntry<Boolean> = this.entry("zombie/zombiesAvoidSun",
        EntryType.BOOL,
        false
    )

    @JvmField
    val ignoreDoorBreakDifficulty: ConfigEntry<Boolean> = this.entry("zombie/ignoreDoorBreakDifficulty",
        EntryType.BOOL,
        false
    )

    @JvmField
    val allZombiesBreakDoors: ConfigEntry<Boolean> = this.entry("zombie/allZombiesBreakDoors",
        EntryType.BOOL,
        false
    )

    @JvmField
    val ignoreReinforcementDifficulty: ConfigEntry<Boolean> = this.entry("zombie/ignoreReinforcementDifficulty",
        EntryType.BOOL,
        false
    )

    @JvmField
    val fullReinforcementChance: ConfigEntry<Boolean> = this.entry("zombie/fullReinforcementChance",
        EntryType.BOOL,
        false
    )

    // SKELETON

    @JvmField
    val skeletonAccuracyIgnoresDifficulty: ConfigEntry<Boolean> = this.entry("skeleton/skeletonAccuracyIgnoresDifficulty",
        EntryType.BOOL,
        false
    )

    @JvmField
    val skeletonsAvoidSun: ConfigEntry<Boolean> = this.entry("skeleton/skeletonsAvoidSun",
        EntryType.BOOL,
        true
    )
}
