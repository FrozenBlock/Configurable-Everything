package net.frozenblock.configurableeverything.biome.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import net.frozenblock.configurableeverything.util.serialization.Either
import net.frozenblock.configurableeverything.util.serialization.either
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome

@Serializable
data class BiomePlacedFeatureList(
    var biome: Either<@Contextual ResourceKey<@Contextual Biome>, @Contextual TagKey<@Contextual Biome>>?,
    var features: List<DecorationStepPlacedFeature?>?
) {
    companion object {
        @JvmField
		val CODEC: Codec<BiomePlacedFeatureList> = RecordCodecBuilder.create { instance ->
            instance.group(
                either(
                    ResourceKey.codec(Registries.BIOME), TagKey.hashedCodec(Registries.BIOME)
                ).fieldOf("biome").forGetter(BiomePlacedFeatureList::biome),
                DecorationStepPlacedFeature.CODEC.listOf().fieldOf("placed_features").forGetter(BiomePlacedFeatureList::features)
            ).apply(instance, ::BiomePlacedFeatureList)
        }
    }

    override fun toString(): String = "BiomePlacedFeatureList[biome=$biome, placed_features=$features]"
}
