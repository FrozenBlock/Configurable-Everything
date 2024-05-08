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
import net.frozenblock.lib.config.api.sync.annotation.EntrySyncData
import net.frozenblock.lib.config.api.sync.annotation.UnsyncableConfig
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

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

@UnsyncableConfig
data class EntityConfig(
    @JvmField
    @EntrySyncData("entityAttributeAmplifiers")
    var entityAttributeAmplifiers: TypedEntry<MutableList<EntityAttributeAmplifier>> = TypedEntry.create(
        ENTITY_ATTRIBUTE_AMPLIFIERS,
        mutableListOf(
            EntityAttributeAmplifier(
                ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation("example")),
                "",
                mutableListOf(
                    AttributeAmplifier(
                        ResourceKey.create(Registries.ATTRIBUTE, ResourceLocation("generic.movement_speed")),
                        1.5
                    )
                )
            )
        )
    ),

    @JvmField
    @EntrySyncData("experienceOverrides")
    var experienceOverrides: TypedEntry<MutableList<ExperienceOverride>> = TypedEntry.create(
        EXPERIENCE_OVERRIDES,
        mutableListOf(
            ExperienceOverride(
                ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation("example")),
                5000
            )
        )
    ),

    @JvmField
    @EntrySyncData("entityFlyBySounds")
    var entityFlyBySounds: TypedEntry<MutableList<EntityFlyBySound>> = TypedEntry.create(
        ENTITY_FLYBY_SOUNDS,
        mutableListOf(
            EntityFlyBySound(
                ResourceLocation("minecraft:arrow"),
                EntityFlyBySoundData(
                    "neutral",
                    id("flyby.arrow"),
                    0.6f,
                    1f
                )
            ),
            EntityFlyBySound(
                ResourceLocation("minecraft:tipped_arrow"),
                EntityFlyBySoundData(
                    "neutral",
                    id("flyby.arrow"),
                    0.6f,
                    1f
                )
            ),
            EntityFlyBySound(
                ResourceLocation("minecraft:spectral"),
                EntityFlyBySoundData(
                    "neutral",
                    id("flyby.arrow"),
                    0.6f,
                    1f
                )
            ),
            EntityFlyBySound(
                ResourceLocation("minecraft:trident"),
                EntityFlyBySoundData(
                    "neutral",
                    id("flyby.trident"),
                    0.7f,
                    1f
                )
            ),
            EntityFlyBySound(
                ResourceLocation("minecraft:egg"),
                EntityFlyBySoundData(
                    "neutral",
                    id("flyby.egg"),
                    0.4f,
                    1f
                )
            ),
            EntityFlyBySound(
                ResourceLocation("minecraft:snowball"),
                EntityFlyBySoundData(
                    "neutral",
                    id("flyby.snowball"),
                    0.4f,
                    1f
                )
            ),  //TODO: Burning flyby sound
            EntityFlyBySound(
                ResourceLocation("minecraft:fireball"),
                EntityFlyBySoundData(
                    "neutral",
                    id("flyby.fireball"),
                    0.4f,
                    1f
                )
            ),
            EntityFlyBySound(
                ResourceLocation("minecraft:potion"),
                EntityFlyBySoundData(
                    "neutral",
                    id("flyby.potion"),
                    0.2f,
                    1f
                )
            ),
            EntityFlyBySound(
                ResourceLocation("minecraft:experience_bottle"),
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
    @EntrySyncData("entityHurtEffects")
    var entityHurtEffects: TypedEntry<MutableList<EntityHurtEffects>> = TypedEntry.create(
        ENTITY_HURT_EFFECTS,
        mutableListOf(
            EntityHurtEffects(
                ResourceLocation("cow"),
                "",
                mutableListOf(
                    MobEffectHolder(
                        ResourceKey.create(Registries.MOB_EFFECT, ResourceLocation("speed")),
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
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    var entitySpottingIcons: TypedEntry<MutableList<EntitySpottingIcon>> = TypedEntry.create(
        SPOTTING_ICONS,
        mutableListOf(
            EntitySpottingIcon(
                ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation("example")),
                id("textures/spotting_icon/icon.png"),
                5f,
                8f
            )
        )
    ),

    @JvmField
    @EntrySyncData("flamingArrowsLightFire")
    var flamingArrowsLightFire: Boolean = false,

    @JvmField
    var player: PlayerConfig = PlayerConfig(),

    @JvmField
    var zombie: ZombieConfig = ZombieConfig(),

    @JvmField
    var skeleton: SkeletonConfig = SkeletonConfig()
) {
    companion object : CEConfig<EntityConfig>(
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
        @EntrySyncData("digSpeedAmplifier")
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
        @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
		var babyZombieSprintParticles: Boolean = false,

        @JvmField
        @EntrySyncData("zombiesAvoidSun")
		var zombiesAvoidSun: Boolean = false,

        @JvmField
        @EntrySyncData("ignoreDoorBreakDifficulty")
		var ignoreDoorBreakDifficulty: Boolean = false,

        @JvmField
        @EntrySyncData("allZombiesBreakDoors")
		var allZombiesBreakDoors: Boolean = false,

        @JvmField
        @EntrySyncData("ignoreReinforcementDifficulty")
        var ignoreReinforcementDifficulty: Boolean = false,

        @JvmField
        @EntrySyncData("fullReinforcementChance")
		var fullReinforcementChance: Boolean = false
    )

    data class SkeletonConfig(
        @JvmField
        @EntrySyncData("skeletonAccuracyIgnoresDifficulty")
		var skeletonAccuracyIgnoresDifficulty: Boolean = false,

        @JvmField
        @EntrySyncData("skeletonsAvoidSun")
		var skeletonsAvoidSun: Boolean = true
    )
}
