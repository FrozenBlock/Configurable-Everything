package net.frozenblock.configurableeverything.biome_placement.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.frozenblock.lib.worldgen.biome.api.MutableParameterPoint
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier

data class BiomeParameters(
	@JvmField var biome: Identifier,
	@JvmField var parameters: MutableParameterPoint
) {
	companion object {
        @JvmField
		val CODEC: Codec<BiomeParameters> = RecordCodecBuilder.create { instance ->
			instance.group(
				Identifier.CODEC.fieldOf("biome").forGetter(BiomeParameters::biome),
				MutableParameterPoint.CODEC.fieldOf("parameters").forGetter(BiomeParameters::parameters)
			).apply(instance, ::BiomeParameters)
		}

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, BiomeParameters> = StreamCodec.composite(
            Identifier.STREAM_CODEC, BiomeParameters::biome,
            MutableParameterPoint.STREAM_CODEC, BiomeParameters::parameters,
            ::BiomeParameters
        )
	}
}
