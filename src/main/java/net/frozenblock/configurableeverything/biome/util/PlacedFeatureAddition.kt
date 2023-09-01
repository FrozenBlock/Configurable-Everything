package net.frozenblock.configurableeverything.biome.util

import net.frozenblock.configurableeverything.util.DynamicRegistryAddition
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.levelgen.placement.PlacedFeature

data class PlacedFeatureAddition(key: ResourceKey<PlacedFeature>, value: PlacedFeature) : DynamicRegistryAddition<PlacedFeature>(Registries.PLACED_FEATURE, key, value)