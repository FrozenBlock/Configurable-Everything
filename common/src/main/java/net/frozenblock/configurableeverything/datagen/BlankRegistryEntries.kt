@file:JvmName("BlankRegistryEntries")

package net.frozenblock.configurableeverything.datagen

import net.frozenblock.configurableeverything.util.id
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.levelgen.placement.PlacedFeature

@JvmField
val BLANK_BIOME: ResourceKey<Biome> = ResourceKey.create(Registries.BIOME, id("blank_biome"))

@JvmField
val BLANK_PLACED_FEATURE: ResourceKey<PlacedFeature> = ResourceKey.create(Registries.PLACED_FEATURE, id("blank_placed_feature"))

@JvmField
val BLANK_TAG: TagKey<Biome> = TagKey.create(Registries.BIOME, id("blank_tag"))
