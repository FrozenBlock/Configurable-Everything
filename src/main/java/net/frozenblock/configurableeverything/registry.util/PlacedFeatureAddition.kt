package net.frozenblock.configurableeverything.registry.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.levelgen.placement.PlacedFeature

data class PlacedFeatureAddition(
    override var key: ResourceLocation,
    override var value: PlacedFeature
) : DynamicRegistryAddition<PlacedFeature>(Registries.PLACED_FEATURE, key, value) {
    companion object {
        @JvmField
        val CODEC: Codec<PlacedFeatureAddition> = RecordCodecBuilder.create { instance ->
            instance.group(
                ResourceLocation.CODEC.fieldOf("key").forGetter(PlacedFeatureAddition::key),
                PlacedFeature.DIRECT_CODEC.fieldOf("value").forGetter(PlacedFeatureAddition::value)
            ).apply(instance, ::PlacedFeatureAddition)
        }
    }
}
