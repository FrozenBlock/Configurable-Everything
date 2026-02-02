package net.frozenblock.configurableeverything.scripting.util.api

import net.frozenblock.configurableeverything.config.*
import net.frozenblock.configurableeverything.loot.util.LootModification
import net.frozenblock.configurableeverything.registry.util.BiomeAddition
import net.frozenblock.configurableeverything.registry.util.PlacedFeatureAddition
import net.frozenblock.configurableeverything.sculk_spreading.util.SculkGrowth
import net.frozenblock.configurableeverything.util.getValue
import net.frozenblock.configurableeverything.util.setValue
import net.frozenblock.lib.worldgen.surface.api.FrozenDimensionBoundRuleSource
import net.minecraft.resources.Identifier

class LootWrapper internal constructor(config: LootConfig) : ConfigWrapper<LootConfig>(config) {

    var lootModifications: MutableList<LootModification> by config.lootModifications
}

class RegistryWrapper internal constructor(config: RegistryConfig) : ConfigWrapper<RegistryConfig>(config) {

    var biomeAdditions: MutableList<BiomeAddition> by config.biomeAdditions
    var placedFeatureAdditions: MutableList<PlacedFeatureAddition> by config.placedFeatureAdditions
}

class SculkSpreadingWrapper internal constructor(config: SculkSpreadingConfig) : ConfigWrapper<SculkSpreadingConfig>(config) {

    var growths: MutableList<SculkGrowth> by config.growths
}

class SurfaceRuleWrapper internal constructor(config: SurfaceRuleConfig) : ConfigWrapper<SurfaceRuleConfig>(config) {

    var addedSurfaceRules: MutableList<FrozenDimensionBoundRuleSource> by config.addedSurfaceRules
}
