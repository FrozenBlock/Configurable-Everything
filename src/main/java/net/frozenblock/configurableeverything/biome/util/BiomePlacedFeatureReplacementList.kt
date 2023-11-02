package net.frozenblock.configurableeverything.biome.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import net.frozenblock.configurableeverything.util.serialization.*
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome

@Serializable
data class BiomePlacedFeatureReplacementList(
    @JvmField var biome: Either<@Contextual ResourceKey<@Contextual Biome>, @Contextual TagKey<@Contextual Biome>>?,
    @JvmField var replacements: List<PlacedFeatureReplacement?>?
) {
	companion object {
		@JvmField
		val CODEC: Codec<BiomePlacedFeatureReplacementList> = RecordCodecBuilder.create { instance ->
			instance.group(
				either(ResourceKey.codec(Registries.BIOME), TagKey.hashedCodec(Registries.BIOME)).fieldOf("biome").forGetter(BiomePlacedFeatureReplacementList::biome),
				PlacedFeatureReplacement.CODEC.listOf().fieldOf("replacements").forGetter(BiomePlacedFeatureReplacementList::replacements)
			).apply(instance, ::BiomePlacedFeatureReplacementList)
		}
	}

    override fun toString(): String = "BiomePlacedFeatureReplacementList[biome=$biome, replacements=$replacements]"
}
