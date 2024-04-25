package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.entity.util.*
import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.makeLegacyConfigPath
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.SyncBehavior
import net.frozenblock.lib.config.api.sync.annotation.EntrySyncData
import net.frozenblock.lib.config.api.sync.annotation.UnsyncableConfig
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

private val ENTITY_ATTRIBUTE_AMPLIFIERS: TypedEntryType<List<EntityAttributeAmplifier>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        EntityAttributeAmplifier.CODEC.listOf()
    )
)

private val ENTITY_FLYBY_SOUNDS: TypedEntryType<List<EntityFlyBySound>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        EntityFlyBySound.CODEC.listOf()
    )
)

private val ENTITY_HURT_EFFECTS: TypedEntryType<List<EntityHurtEffects>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        EntityHurtEffects.CODEC.listOf()
    )
)

private val EXPERIENCE_OVERRIDES: TypedEntryType<List<ExperienceOverride>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        ExperienceOverride.CODEC.listOf()
    )
)

private val SPOTTING_ICONS: TypedEntryType<List<EntitySpottingIcon>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        EntitySpottingIcon.CODEC.listOf()
    )
)

@UnsyncableConfig
data class EntityConfig(
    @JvmField
    @EntrySyncData("entityAttributeAmplifiers")
    var entityAttributeAmplifiers: TypedEntry<List<EntityAttributeAmplifier>> = TypedEntry(
        ENTITY_ATTRIBUTE_AMPLIFIERS,
        listOf(
            EntityAttributeAmplifier(
                ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation("example")),
                "",
                listOf(
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
    var experienceOverrides: TypedEntry<List<ExperienceOverride>> = TypedEntry(
        EXPERIENCE_OVERRIDES,
        listOf(
            ExperienceOverride(
                ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation("example")),
                5000
            )
        )
    ),

    @JvmField
    @EntrySyncData("entityFlyBySounds")
    var entityFlyBySounds: TypedEntry<List<EntityFlyBySound>> = TypedEntry(
        ENTITY_FLYBY_SOUNDS,
        listOf(
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
    var entityHurtEffects: TypedEntry<List<EntityHurtEffects>> = TypedEntry(
        ENTITY_HURT_EFFECTS,
        listOf(
            EntityHurtEffects(
                ResourceLocation("cow"),
                "",
                listOf(
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
    var entitySpottingIcons: TypedEntry<List<EntitySpottingIcon>> = TypedEntry(
        SPOTTING_ICONS,
        listOf(
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
    companion object : JsonConfig<EntityConfig>(
        MOD_ID,
        EntityConfig::class.java,
        makeLegacyConfigPath("entity"),
        CONFIG_JSONTYPE,
        null,
        null
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
