package net.frozenblock.configurableeverything.biome_placement.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;

public record BiomeParameters(ResourceKey<Biome> biome, Climate.ParameterPoint parameters) {

	public static final Codec<BiomeParameters> CODEC = RecordCodecBuilder.create(instance ->
		instance.group(
			ResourceKey.codec(Registries.BIOME).fieldOf("biome_placement").forGetter(BiomeParameters::biome),
			Climate.ParameterPoint.CODEC.fieldOf("parameters").forGetter(BiomeParameters::parameters)
		).apply(instance, BiomeParameters::new)
	);
}
