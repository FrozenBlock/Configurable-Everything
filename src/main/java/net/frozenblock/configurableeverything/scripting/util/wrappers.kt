package net.frozenblock.configurableeverything.scripting.util

import net.frozenblock.configurableeverything.biome.util.BiomeMusic
import net.frozenblock.configurableeverything.biome.util.BiomePlacedFeatureList
import net.frozenblock.configurableeverything.biome.util.BiomePlacedFeatureReplacementList
import net.frozenblock.configurableeverything.biome_placement.util.DimensionBiomeKeyList
import net.frozenblock.configurableeverything.biome_placement.util.DimensionBiomeList
import net.frozenblock.configurableeverything.block.util.MutableBlockSoundGroupOverwrite
import net.frozenblock.configurableeverything.config.*
import net.frozenblock.configurableeverything.datafixer.util.RegistryFixer
import net.frozenblock.configurableeverything.datafixer.util.SchemaEntry
import net.frozenblock.configurableeverything.entity.util.*
import net.frozenblock.configurableeverything.fluid.util.FluidFlowSpeed
import net.frozenblock.configurableeverything.gravity.util.DimensionGravityBelt
import net.frozenblock.configurableeverything.item.util.ItemReachOverride
import net.frozenblock.configurableeverything.loot.util.LootModification
import net.frozenblock.configurableeverything.registry.util.BiomeAddition
import net.frozenblock.configurableeverything.registry.util.PlacedFeatureAddition
import net.frozenblock.configurableeverything.screenshake.util.SoundScreenShake
import net.frozenblock.configurableeverything.sculk_spreading.util.SculkGrowth
import net.frozenblock.configurableeverything.util.getValue
import net.frozenblock.configurableeverything.util.setValue
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.worldgen.surface.api.FrozenDimensionBoundRuleSource
import net.minecraft.resources.ResourceLocation

class MainWrapper internal constructor(config: MainConfig) : ConfigWrapper<MainConfig>(config) {

    var biome: Boolean by config::biome
    var biome_placement: Boolean by config::biome_placement
    var block: Boolean by config::block
    var datafixer: Boolean by config::datafixer
    var entity: Boolean by config::entity
    var fluid: Boolean by config::fluid
    var game: Boolean by config::game
    var gravity: Boolean by config::gravity
    var item: Boolean by config::item
    var loot: Boolean by config::loot
    var music: Boolean by config::music
    var registry: Boolean by config::registry
    var screen_shake: Boolean by config::screen_shake
    var scripting: Boolean by config::scripting
    var sculk_spreading: Boolean by config::sculk_spreading
    var splash_text: Boolean by config::splash_text
    var structure: Boolean by config::structure
    var surface_rule: Boolean by config::surface_rule
    var tag: Boolean by config::tag
    var world: Boolean by config::world
    var datapack: MainConfig.DatapackConfig by config::datapack
}

class BiomeWrapper internal constructor(config: BiomeConfig) : ConfigWrapper<BiomeConfig>(config) {

    var addedFeatures: List<BiomePlacedFeatureList> by config.addedFeatures
    var removedFeatures: List<BiomePlacedFeatureList> by config.removedFeatures
    var replacedFeatures: List<BiomePlacedFeatureReplacementList> by config.replacedFeatures
    var musicReplacements: List<BiomeMusic> by config.musicReplacements
}

class BiomePlacementWrapper internal constructor(config: BiomePlacementConfig) : ConfigWrapper<BiomePlacementConfig>(config) {

    var addedBiomes: List<DimensionBiomeList> by config.addedBiomes
    var removedBiomes: List<DimensionBiomeKeyList> by config.removedBiomes
}

class BlockWrapper internal constructor(config: BlockConfig) : ConfigWrapper<BlockConfig>(config) {

    var soundGroupOverwrites: List<MutableBlockSoundGroupOverwrite> by config.soundGroupOverwrites
}

class DataFixerWrapper internal constructor(config: DataFixerConfig) : ConfigWrapper<DataFixerConfig>(config) {

    var overrideRealEntries: Boolean by config::overrideRealEntries
    var dataVersion: Int by config::dataVersion
    var schemas: List<SchemaEntry> by config.schemas
    var registryFixers: List<RegistryFixer> by config.registryFixers
}

class EntityWrapper internal constructor(config: EntityConfig) : ConfigWrapper<EntityConfig>(config) {

    var entityAttributeAmplifiers: List<EntityAttributeAmplifier> by config.entityAttributeAmplifiers
    var experienceOverrides: List<ExperienceOverride> by config.experienceOverrides
    var entityFlyBySounds: List<EntityFlyBySound> by config.entityFlyBySounds
    var entityHurtEffects: List<EntityHurtEffects> by config.entityHurtEffects
    var entitySpottingIcon: List<EntitySpottingIcon> by config.entitySpottingIcons
    var flamingArrowsLightFire: Boolean by config::flamingArrowsLightFire
    var player: EntityConfig.PlayerConfig by config::player
    var zombie: EntityConfig.ZombieConfig by config::zombie
    var skeleton: EntityConfig.SkeletonConfig by config::skeleton
}

class FluidWrapper internal constructor(config: FluidConfig) : ConfigWrapper<FluidConfig>(config) {

    var flowSpeeds: List<FluidFlowSpeed> by config.flowSpeeds
}

class GravityWrapper internal constructor(config: GravityConfig) : ConfigWrapper<GravityConfig>(config) {

    var gravityBelts: List<DimensionGravityBelt> by config.gravityBelts
}

class ItemWrapper internal constructor(config: ItemConfig) : ConfigWrapper<ItemConfig>(config) {

    var reachOverrideds: List<ItemReachOverride> by config.reachOverrides
}

class LootWrapper internal constructor(config: LootConfig) : ConfigWrapper<LootConfig>(config) {

    var lootModifications: List<LootModification> by config.lootModifications
}

class RegistryWrapper internal constructor(config: RegistryConfig) : ConfigWrapper<RegistryConfig>(config) {

    var biomeAdditions: List<BiomeAddition> by config.biomeAdditions
    var placedFeatureAdditions: List<PlacedFeatureAddition> by config.placedFeatureAdditions
}

class ScreenShakeWrapper internal constructor(config: ScreenShakeConfig) : ConfigWrapper<ScreenShakeConfig>(config) {

    var soundScreenShakes: List<SoundScreenShake> by config.soundScreenShakes
    var dragonRespawnScreenShake: Boolean by config::dragonRespawnScreenShake
    var explosionScreenShake: Boolean by config::explosionScreenShake
}

class SculkSpreadingWrapper internal constructor(config: SculkSpreadingConfig) : ConfigWrapper<SculkSpreadingConfig>(config) {

    var growths: List<SculkGrowth> by config.growths
}

class SplashTextWrapper internal constructor(config: SplashTextConfig) : ConfigWrapper<SplashTextConfig>(config) {

    var addedSplashes: List<String> by config::addedSplashes
    var removedSplashes: List<String> by config::removedSplashes
    var splashColor: Int by config::splashColor
    var removeVanilla: Boolean by config::removeVanilla
}

class StructureWrapper internal constructor(config: StructureConfig) : ConfigWrapper<StructureConfig>(config) {

    var removedStructures: List<ResourceLocation> by config.removedStructures
    var removedStructureSets: List<ResourceLocation> by config.removedStructureSets
}

class SurfaceRuleWrapper internal constructor(config: SurfaceRuleConfig) : ConfigWrapper<SurfaceRuleConfig>(config) {

    var addedSurfaceRules: List<FrozenDimensionBoundRuleSource> by config.addedSurfaceRules
}

class WorldWrapper internal constructor(config: WorldConfig) : ConfigWrapper<WorldConfig>(config) {

    var dayTimeSpeedAmplifier: Long by config::dayTimeSpeedAmplifier
    var fixSunMoonRotating: Boolean by config::fixSunMoonRotating
    var sunSize: Int by config::sunSize
    var moonSize: Int by config::moonSize
    var disableExperimentalWarning: Boolean by config::disableExperimentalWarning
}
