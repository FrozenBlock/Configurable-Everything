package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.entity.util.*
import net.frozenblock.configurableeverything.util.*
import net.frozenblock.configurableeverything.util.CONFIG_FORMAT
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.xjs.XjsConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.SyncBehavior
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.Identifier

private val ENTITY_ATTRIBUTE_AMPLIFIERS: TypedEntryType<MutableList<EntityAttributeAmplifier>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        EntityAttributeAmplifier.CODEC.mutListOf()
    )
)

private val ENTITY_FLYBY_SOUNDS: TypedEntryType<MutableList<EntityFlyBySound>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        EntityFlyBySound.CODEC.mutListOf()
    )
)

private val ENTITY_HURT_EFFECTS: TypedEntryType<MutableList<EntityHurtEffects>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        EntityHurtEffects.CODEC.mutListOf()
    )
)

private val EXPERIENCE_OVERRIDES: TypedEntryType<MutableList<ExperienceOverride>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        ExperienceOverride.CODEC.mutListOf()
    )
)

private val SPOTTING_ICONS: TypedEntryType<MutableList<EntitySpottingIcon>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        EntitySpottingIcon.CODEC.mutListOf()
    )
)

data class EntityConfig(
    @JvmField
    var entityAttributeAmplifiers: TypedEntry<MutableList<EntityAttributeAmplifier>> = TypedEntry.create(
        ENTITY_ATTRIBUTE_AMPLIFIERS,
        mutableListOf(
            EntityAttributeAmplifier(
                ResourceKey.create(Registries.ENTITY_TYPE, Identifier.withDefaultNamespace("example")),
                "",
                mutableListOf(
                    AttributeAmplifier(
                        ResourceKey.create(Registries.ATTRIBUTE, Identifier.withDefaultNamespace("generic.movement_speed")),
                        1.5
                    )
                )
            )
        )
    ),

    @JvmField
    var experienceOverrides: TypedEntry<MutableList<ExperienceOverride>> = TypedEntry.create(
        EXPERIENCE_OVERRIDES,
        mutableListOf(
            ExperienceOverride(
                ResourceKey.create(Registries.ENTITY_TYPE, Identifier.withDefaultNamespace("example")),
                5000
            )
        )
    ),

    @JvmField
    var entityFlyBySounds: TypedEntry<MutableList<EntityFlyBySound>> = TypedEntry.create(
        ENTITY_FLYBY_SOUNDS,
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
    ),

    @JvmField
    var entityHurtEffects: TypedEntry<MutableList<EntityHurtEffects>> = TypedEntry.create(
        ENTITY_HURT_EFFECTS,
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
    ),

    @JvmField
    // UNSYNCABLE
    var entitySpottingIcons: TypedEntry<MutableList<EntitySpottingIcon>> = TypedEntry.create(
        SPOTTING_ICONS,
        mutableListOf(
            EntitySpottingIcon(
                ResourceKey.create(Registries.ENTITY_TYPE, Identifier.withDefaultNamespace("example")),
                id("textures/spotting_icon/icon.png"),
                5f,
                8f
            )
        )
    ),

    @JvmField
    var flamingArrowsLightFire: Boolean = false,

    @JvmField
    var player: PlayerConfig = PlayerConfig(),

    @JvmField
    var zombie: ZombieConfig = ZombieConfig(),

    @JvmField
    var skeleton: SkeletonConfig = SkeletonConfig()
) {
    companion object : CESimpleConfig<EntityConfig>(
        EntityConfig::class,
        "entity"
    ) {

        init {
            ConfigRegistry.register(this)
        }

        @JvmStatic
        @JvmOverloads
        fun get(real: Boolean = false): EntityConfig = if (real) this.instance() else this.config()
    }

    data class PlayerConfig(
        @JvmField
        var digSpeedAmplifier: Int = 100
    ) {
        inline val digSpeed: Double // acts as a getter method
            get() {
                val amplifier: Double = digSpeedAmplifier.toDouble()
                return amplifier / 100.0
            }
    }

    data class ZombieConfig(
        @JvmField
        // UNSYNCABLE
		var babyZombieSprintParticles: Boolean = false,

        @JvmField
		var zombiesAvoidSun: Boolean = false,

        @JvmField
		var ignoreDoorBreakDifficulty: Boolean = false,

        @JvmField
		var allZombiesBreakDoors: Boolean = false,

        @JvmField
        var ignoreReinforcementDifficulty: Boolean = false,

        @JvmField
		var fullReinforcementChance: Boolean = false
    )

    data class SkeletonConfig(
        @JvmField
		var skeletonAccuracyIgnoresDifficulty: Boolean = false,

        @JvmField
		var skeletonsAvoidSun: Boolean = true
    )
}
