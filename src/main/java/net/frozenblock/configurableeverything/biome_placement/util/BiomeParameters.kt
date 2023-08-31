package net.frozenblock.configurableeverything.biome_placement.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.Climate

data class BiomeParameters(
	@JvmField var biome: ResourceKey<Biome>?,
	@JvmField var parameters: Climate.ParameterPoint?
) {
	companion object {
		val CODEC: Codec<BiomeParameters> = RecordCodecBuilder.create { instance ->
			instance.group(
				ResourceKey.codec(Registries.BIOME).fieldOf("biome").forGetter(BiomeParameters::biome),
				Climate.ParameterPoint.CODEC.fieldOf("parameters").forGetter(BiomeParameters::parameters)
			).apply(instance, ::BiomeParameters)
		}
	}

	override fun toString(): String = "BiomeParameters[$biome, $parameters]"
}
