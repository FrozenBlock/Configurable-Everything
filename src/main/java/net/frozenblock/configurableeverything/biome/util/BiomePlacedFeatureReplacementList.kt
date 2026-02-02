package net.frozenblock.configurableeverything.biome.util

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.frozenblock.configurableeverything.util.mutListOf
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome

data class BiomePlacedFeatureReplacementList(
    @JvmField var biome: Either<ResourceKey<Biome>, TagKey<Biome>>,
    @JvmField var replacements: MutableList<PlacedFeatureReplacement>
) {
	companion object {
		@JvmField
		val CODEC: Codec<BiomePlacedFeatureReplacementList> = RecordCodecBuilder.create { instance ->
			instance.group(
				Codec.either(ResourceKey.codec(Registries.BIOME), TagKey.hashedCodec(Registries.BIOME)).fieldOf("biome").forGetter(BiomePlacedFeatureReplacementList::biome),
				PlacedFeatureReplacement.CODEC.mutListOf().fieldOf("replacements").forGetter(BiomePlacedFeatureReplacementList::replacements)
			).apply(instance, ::BiomePlacedFeatureReplacementList)
		}

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, BiomePlacedFeatureReplacementList> = StreamCodec.composite(
            ByteBufCodecs.either(
                ResourceKey.streamCodec(Registries.BIOME),
                TagKey.streamCodec(Registries.BIOME)
            ),
            BiomePlacedFeatureReplacementList::biome,
            ByteBufCodecs.list<ByteBuf, PlacedFeatureReplacement>().apply(PlacedFeatureReplacement.STREAM_CODEC),
            BiomePlacedFeatureReplacementList::replacements,
            ::BiomePlacedFeatureReplacementList
        )
	}
}
