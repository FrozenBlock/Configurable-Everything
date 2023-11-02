package net.frozenblock.configurableeverything.biome.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import net.frozenblock.configurableeverything.util.serialization.*
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.Music
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome

@Serializable
data class BiomeMusic(
    @JvmField var biome: Either<@Contextual ResourceKey<@Contextual Biome>, @Contextual TagKey<@Contextual Biome>>?,
    @JvmField var music: @Contextual Music?
) {
	companion object {
		@JvmField
		val CODEC: Codec<BiomeMusic> = RecordCodecBuilder.create { instance ->
			instance.group(
				either(ResourceKey.codec(Registries.BIOME), TagKey.hashedCodec(Registries.BIOME)).fieldOf("biome").forGetter(BiomeMusic::biome),
				Music.CODEC.fieldOf("music").forGetter(BiomeMusic::music)
			).apply(instance, ::BiomeMusic)
		}
	}

    override fun toString(): String = "BiomeMusic[biome=$biome, music=$music]"
}
