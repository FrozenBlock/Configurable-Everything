package net.frozenblock.configurableeverything.biome_placement.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.frozenblock.lib.worldgen.biome.api.MutableParameterPoint
import net.minecraft.resources.ResourceLocation

data class BiomeParameters(
	@JvmField var biome: ResourceLocation?,
	@JvmField var parameters: MutableParameterPoint?
) {
	companion object {
        @JvmField
		val CODEC: Codec<BiomeParameters> = RecordCodecBuilder.create { instance ->
			instance.group(
				ResourceLocation.CODEC.fieldOf("biome").forGetter(BiomeParameters::biome),
				MutableParameterPoint.CODEC.fieldOf("parameters").forGetter(BiomeParameters::parameters)
			).apply(instance, ::BiomeParameters)
		}
	}
}
