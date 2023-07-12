package net.frozenblock.configurableeverything.biome.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import java.util.List;

public record BiomePlacedFeatureReplacementList(ResourceKey<Biome> biome, List<PlacedFeatureReplacement> replacements) {

	public static final Codec<BiomePlacedFeatureReplacementList> CODEC = RecordCodecBuilder.create(instance ->
		instance.group(
			ResourceKey.codec(Registries.BIOME).fieldOf("biome").forGetter(BiomePlacedFeatureReplacementList::biome),
			PlacedFeatureReplacement.CODEC.listOf().fieldOf("replacements").forGetter(BiomePlacedFeatureReplacementList::replacements)
		).apply(instance, BiomePlacedFeatureReplacementList::new)
	);
}
