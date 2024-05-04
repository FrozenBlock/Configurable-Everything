package net.frozenblock.configurableeverything.biome.util

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome

data class BiomePlacedFeatureList(
    @JvmField var biome: Either<ResourceKey<Biome>, TagKey<Biome>>,
    @JvmField var features: List<DecorationStepPlacedFeature>
) {
    companion object {
        @JvmField
		val CODEC: Codec<BiomePlacedFeatureList> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.either(
                    ResourceKey.codec(Registries.BIOME), TagKey.hashedCodec(Registries.BIOME)
                ).fieldOf("biome").forGetter(BiomePlacedFeatureList::biome),
                DecorationStepPlacedFeature.CODEC.listOf().fieldOf("placed_features").forGetter(BiomePlacedFeatureList::features)
            ).apply(instance, ::BiomePlacedFeatureList)
        }
    }
}
