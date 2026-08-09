package net.frozenblock.configurableeverything.biome_placement.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.frozenblock.configurableeverything.util.mutListOf
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.dimension.DimensionType

data class DimensionBiomeList(
	@JvmField var dimension: ResourceKey<DimensionType>,
	@JvmField var biomes: MutableList<BiomeParameters>
) {
	companion object {
        @JvmField
		val CODEC: Codec<DimensionBiomeList> = RecordCodecBuilder.create { instance ->
			instance.group(
				ResourceKey.codec(Registries.DIMENSION_TYPE).fieldOf("dimension").forGetter(DimensionBiomeList::dimension),
				BiomeParameters.CODEC.mutListOf().fieldOf("biomes").forGetter(DimensionBiomeList::biomes)
			).apply(instance, ::DimensionBiomeList)
		}

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, DimensionBiomeList> = StreamCodec.composite(
            ResourceKey.streamCodec(Registries.DIMENSION_TYPE), DimensionBiomeList::dimension,
            ByteBufCodecs.list<ByteBuf, BiomeParameters>().apply(BiomeParameters.STREAM_CODEC), DimensionBiomeList::biomes,
            ::DimensionBiomeList
        )
	}
}
