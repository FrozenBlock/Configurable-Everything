package net.frozenblock.configurableeverything.biome_placement.util

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import net.frozenblock.configurableeverything.util.mutListOf
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.dimension.DimensionType

data class DimensionBiomeKeyMap(
	@JvmField var dimension: ResourceKey<DimensionType>,
	@JvmField var biomes: MutableMap<ResourceKey<Biome>, ResourceKey<Biome>>
) {
	companion object {
        @JvmField
		val CODEC: Codec<DimensionBiomeKeyMap> = RecordCodecBuilder.create { instance ->
			instance.group(
				ResourceKey.codec(Registries.DIMENSION_TYPE).fieldOf("dimension").forGetter(DimensionBiomeKeyMap::dimension),
				Codec.unboundedMap(
                    ResourceKey.codec(Registries.BIOME),
                    ResourceKey.codec(Registries.BIOME),
                ).fieldOf("biomes").forGetter(DimensionBiomeKeyMap::biomes)
			).apply(instance, ::DimensionBiomeKeyMap)
		}

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, DimensionBiomeKeyMap> = StreamCodec.composite(
            ResourceKey.streamCodec(Registries.DIMENSION_TYPE),
            DimensionBiomeKeyMap::dimension,
            ByteBufCodecs.map(
                ::Object2ObjectOpenHashMap,
                ResourceKey.streamCodec(Registries.BIOME),
                ResourceKey.streamCodec(Registries.BIOME)
            ),
            DimensionBiomeKeyMap::biomes,
            ::DimensionBiomeKeyMap
        )
	}
}
