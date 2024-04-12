package net.frozenblock.configurableeverything.biome.util

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.frozenblock.lib.sound.api.MutableMusic
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome

data class BiomeMusic(
    @JvmField var biome: Either<ResourceKey<Biome>, TagKey<Biome>>,
    @JvmField var music: MutableMusic
) {
	companion object {
		@JvmField
		val CODEC: Codec<BiomeMusic> = RecordCodecBuilder.create { instance ->
			instance.group(
				Codec.either(ResourceKey.codec(Registries.BIOME), TagKey.hashedCodec(Registries.BIOME)).fieldOf("biome").forGetter(BiomeMusic::biome),
				MutableMusic.CODEC.fieldOf("music").forGetter(BiomeMusic::music)
			).apply(instance, ::BiomeMusic)
		}
	}
}
