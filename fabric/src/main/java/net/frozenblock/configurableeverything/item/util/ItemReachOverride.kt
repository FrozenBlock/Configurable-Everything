package net.frozenblock.configurableeverything.item.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier

data class ItemReachOverride(
    @JvmField
    var item: Identifier,

    @JvmField
    var reach: Double
) {
    companion object {
        @JvmField
        val CODEC: Codec<ItemReachOverride> = RecordCodecBuilder.create { instance ->
            instance.group(
                Identifier.CODEC.fieldOf("item").forGetter(ItemReachOverride::item),
                Codec.DOUBLE.fieldOf("reach").forGetter(ItemReachOverride::reach)
            ).apply(instance, ::ItemReachOverride)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, ItemReachOverride> = StreamCodec.composite(
            Identifier.STREAM_CODEC, ItemReachOverride::item,
            ByteBufCodecs.DOUBLE, ItemReachOverride::reach,
            ::ItemReachOverride
        )
    }
}
