package net.frozenblock.configurableeverything.scripting.util.api

import net.frozenblock.configurableeverything.biome_placement.util.DimensionBiomeKeyList
import net.frozenblock.configurableeverything.biome_placement.util.DimensionBiomeList
import net.frozenblock.configurableeverything.block.util.MutableBlockSoundGroupOverwrite
import net.frozenblock.configurableeverything.config.*
import net.frozenblock.configurableeverything.entity.util.*
import net.frozenblock.configurableeverything.loot.util.LootModification
import net.frozenblock.configurableeverything.registry.util.BiomeAddition
import net.frozenblock.configurableeverything.registry.util.PlacedFeatureAddition
import net.frozenblock.configurableeverything.screenshake.util.SoundScreenShake
import net.frozenblock.configurableeverything.sculk_spreading.util.SculkGrowth
import net.frozenblock.configurableeverything.util.getValue
import net.frozenblock.configurableeverything.util.setValue
import net.frozenblock.lib.worldgen.surface.api.FrozenDimensionBoundRuleSource
import net.minecraft.resources.Identifier

class BiomePlacementWrapper internal constructor(config: BiomePlacementConfig) : ConfigWrapper<BiomePlacementConfig>(config) {

    var addedBiomes: MutableList<DimensionBiomeList> by config.addedBiomes
    var removedBiomes: MutableList<DimensionBiomeKeyList> by config.removedBiomes
}

class BlockWrapper internal constructor(config: BlockConfig) : ConfigWrapper<BlockConfig>(config) {

    var soundGroupOverwrites: MutableList<MutableBlockSoundGroupOverwrite> by config.soundGroupOverwrites
}

class EntityWrapper internal constructor(config: EntityConfig) : ConfigWrapper<EntityConfig>(config) {

    var entityAttributeAmplifiers: MutableList<EntityAttributeAmplifier> by config.entityAttributeAmplifiers
    var experienceOverrides: MutableList<ExperienceOverride> by config.experienceOverrides
    var entityFlyBySounds: MutableList<EntityFlyBySound> by config.entityFlyBySounds
    var entityHurtEffects: MutableList<EntityHurtEffects> by config.entityHurtEffects
    var entitySpottingIcon: MutableList<EntitySpottingIcon> by config.entitySpottingIcons
    var flamingArrowsLightFire: Boolean by config::flamingArrowsLightFire
    var player: EntityConfig.PlayerConfig by config::player
    var zombie: EntityConfig.ZombieConfig by config::zombie
    var skeleton: EntityConfig.SkeletonConfig by config::skeleton
}

class LootWrapper internal constructor(config: LootConfig) : ConfigWrapper<LootConfig>(config) {

    var lootModifications: MutableList<LootModification> by config.lootModifications
}

class RegistryWrapper internal constructor(config: RegistryConfig) : ConfigWrapper<RegistryConfig>(config) {

    var biomeAdditions: MutableList<BiomeAddition> by config.biomeAdditions
    var placedFeatureAdditions: MutableList<PlacedFeatureAddition> by config.placedFeatureAdditions
}

class ScreenShakeWrapper internal constructor(config: ScreenShakeConfig) : ConfigWrapper<ScreenShakeConfig>(config) {

    var soundScreenShakes: MutableList<SoundScreenShake> by config.soundScreenShakes
    var dragonRespawnScreenShake: Boolean by config::dragonRespawnScreenShake
    var explosionScreenShake: Boolean by config::explosionScreenShake
}

class SculkSpreadingWrapper internal constructor(config: SculkSpreadingConfig) : ConfigWrapper<SculkSpreadingConfig>(config) {

    var growths: MutableList<SculkGrowth> by config.growths
}

class StructureWrapper internal constructor(config: StructureConfig) : ConfigWrapper<StructureConfig>(config) {

    var removedStructures: MutableList<Identifier> by config.removedStructures
    var removedStructureSets: MutableList<Identifier> by config.removedStructureSets
}

class SurfaceRuleWrapper internal constructor(config: SurfaceRuleConfig) : ConfigWrapper<SurfaceRuleConfig>(config) {

    var addedSurfaceRules: MutableList<FrozenDimensionBoundRuleSource> by config.addedSurfaceRules
}
