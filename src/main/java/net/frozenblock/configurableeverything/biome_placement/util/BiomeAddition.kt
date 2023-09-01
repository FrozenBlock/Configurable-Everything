package net.frozenblock.configurableeverything.biome_placement.util

import net.frozenblock.configurableeverything.util.DynamicRegistryAddition
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.biome.Biome

data class BiomeAddition(key: ResourceKey<Biome>, value: Biome) : DynamicRegistryAddition<Biome>(REGISTRY, key, value) {
    companion object {
        @JvmField
        val REGISTRY = Registries.BIOME

        @JvmField
        val CODEC: Codec<BiomeAddition> = DynamicRegistryAddition.codec(
            REGISTRY,
            Biome.CODEC
        )
    }
}