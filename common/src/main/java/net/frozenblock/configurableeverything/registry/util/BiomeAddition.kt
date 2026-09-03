package net.frozenblock.configurableeverything.registry.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.Identifier
import net.minecraft.world.level.biome.Biome

data class BiomeAddition(
    override var key: Identifier,
    override var value: Biome
) : DynamicRegistryAddition<Biome>(REGISTRY, key, value) {
    companion object {
        @JvmField
        val REGISTRY: ResourceKey<Registry<Biome>> = Registries.BIOME

        @JvmField
        val CODEC: Codec<BiomeAddition> = RecordCodecBuilder.create { instance ->
            instance.group(
                Identifier.CODEC.fieldOf("key").forGetter(BiomeAddition::key),
                Biome.DIRECT_CODEC.fieldOf("value").forGetter(BiomeAddition::value)
            ).apply(instance, ::BiomeAddition)
        }

        // todo more compact stream codec
        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, BiomeAddition> = StreamCodec.composite(
            Identifier.STREAM_CODEC,
            BiomeAddition::key,
            ByteBufCodecs.fromCodec(Biome.DIRECT_CODEC),
            BiomeAddition::value,
            ::BiomeAddition
        )
    }
}
