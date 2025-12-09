package net.frozenblock.configurableeverything.biome_placement.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.frozenblock.lib.worldgen.biome.api.MutableParameterPoint
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
	}
}
