package net.frozenblock.configurableeverything.registry.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.Identifier
import net.minecraft.world.level.biome.Biome

data class BiomeAddition(
    override var key: Identifier,
    override var value: Biome
) : DynamicRegistryAddition<Biome>(REGISTRY, key, value) {
    companion object {
        @JvmField
        val REGISTRY: ResourceKey<Registry<Biome>> = Registries.BIOME

        @JvmField
        val CODEC: Codec<BiomeAddition> = RecordCodecBuilder.create { instance ->
            instance.group(
                Identifier.CODEC.fieldOf("key").forGetter(BiomeAddition::key),
                Biome.DIRECT_CODEC.fieldOf("value").forGetter(BiomeAddition::value)
            ).apply(instance, ::BiomeAddition)
        }
    }
}
