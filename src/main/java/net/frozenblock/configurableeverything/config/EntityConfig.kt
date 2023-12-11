package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.entity.util.*
import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.annotation.FieldIdentifier
import net.frozenblock.lib.config.api.annotation.UnsyncableConfig
import net.frozenblock.lib.config.api.annotation.UnsyncableEntry
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

private val ENTITY_ATTRIBUTE_AMPLIFIERS: TypedEntryType<List<EntityAttributeAmplifier?>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        EntityAttributeAmplifier.CODEC.listOf()
    )
)

private val ENTITY_FLYBY_SOUNDS: TypedEntryType<List<EntityFlyBySound?>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        EntityFlyBySound.CODEC.listOf()
    )
)

private val ENTITY_HURT_EFFECTS: TypedEntryType<List<EntityHurtEffects?>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        EntityHurtEffects.CODEC.listOf()
    )
)

private val EXPERIENCE_OVERRIDES: TypedEntryType<List<ExperienceOverride?>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        ExperienceOverride.CODEC.listOf()
    )
)

private val SPOTTING_ICONS: TypedEntryType<List<EntitySpottingIcon?>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        EntitySpottingIcon.CODEC.listOf()
    )
)

@UnsyncableConfig
data class EntityConfig(
    @JvmField
    @FieldIdentifier(identifier = "entityAttributeAmplifiers")
    var entityAttributeAmplifiers: TypedEntry<List<EntityAttributeAmplifier?>>? = TypedEntry(
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
    @FieldIdentifier(identifier = "experienceOverrides")
    var experienceOverrides: TypedEntry<List<ExperienceOverride?>>? = TypedEntry(
        EXPERIENCE_OVERRIDES,
        listOf(
            ExperienceOverride(
                ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation("example")),
                5000
            )
        )
    ),

    @JvmField
    @FieldIdentifier(identifier = "entityFlyBySounds")
    var entityFlyBySounds: TypedEntry<List<EntityFlyBySound?>>? = TypedEntry(
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
    @FieldIdentifier(identifier = "entityHurtEffects")
    var entityHurtEffects: TypedEntry<List<EntityHurtEffects?>>? = TypedEntry(
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
    @UnsyncableEntry
    var entitySpottingIcons: TypedEntry<List<EntitySpottingIcon?>>? = TypedEntry(
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
    @FieldIdentifier(identifier = "flamingArrowsLightFire")
    var flamingArrowsLightFire: Boolean? = false,

    @JvmField
    var player: PlayerConfig? = PlayerConfig(),

    @JvmField
    var zombie: ZombieConfig? = ZombieConfig(),

    @JvmField
    var skeleton: SkeletonConfig? = SkeletonConfig()
) {
    companion object : JsonConfig<EntityConfig>(
        MOD_ID,
        EntityConfig::class.java,
        makeConfigPath("entity"),
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
        @FieldIdentifier(identifier = "digSpeedAmplifier")
        var digSpeedAmplifier: Int? = 100
    ) {
        inline val digSpeed: Double // acts as a getter method
            get() {
                val amplifier: Double = digSpeedAmplifier?.toDouble() ?: Companion.defaultInstance().player!!.digSpeedAmplifier!!.toDouble()
                return amplifier / 100.0
            }
    }

    data class ZombieConfig(
        @JvmField
        @UnsyncableEntry
		var babyZombieSprintParticles: Boolean? = false,

        @JvmField
        @FieldIdentifier(identifier = "zombiesAvoidSun")
		var zombiesAvoidSun: Boolean? = false,

        @JvmField
        @FieldIdentifier(identifier = "ignoreDoorBreakDifficulty")
		var ignoreDoorBreakDifficulty: Boolean? = false,

        @JvmField
        @FieldIdentifier(identifier = "allZombiesBreakDoors")
		var allZombiesBreakDoors: Boolean? = false,

        @JvmField
        @FieldIdentifier(identifier = "ignoreReinforcementDifficulty")
        var ignoreReinforcementDifficulty: Boolean? = false,

        @JvmField
        @FieldIdentifier(identifier = "fullReinforcementChance")
		var fullReinforcementChance: Boolean? = false
    )

    data class SkeletonConfig(
        @JvmField
        @FieldIdentifier(identifier = "skeletonAccuracyIgnoresDifficulty")
		var skeletonAccuracyIgnoresDifficulty: Boolean? = false,

        @JvmField
        @FieldIdentifier(identifier = "skeletonsAvoidSun")
		var skeletonsAvoidSun: Boolean? = true
    )
}
