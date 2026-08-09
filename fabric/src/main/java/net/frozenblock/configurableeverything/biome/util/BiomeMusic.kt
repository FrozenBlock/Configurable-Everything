package net.frozenblock.configurableeverything.biome.util

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.frozenblock.configurableeverything.util.mutListOf
import net.frozenblock.lib.sound.api.MutableMusic
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
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

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, BiomeMusic> = StreamCodec.composite(
            ByteBufCodecs.either(
                ResourceKey.streamCodec(Registries.BIOME),
                TagKey.streamCodec(Registries.BIOME)
            ),
            BiomeMusic::biome,
            MutableMusic.STREAM_CODEC, BiomeMusic::music,
            ::BiomeMusic
        )
	}
}
