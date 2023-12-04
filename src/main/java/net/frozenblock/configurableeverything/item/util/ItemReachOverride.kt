package net.frozenblock.configurableeverything.item.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.resources.ResourceLocation

data class ItemReachOverride(
    @JvmField
    var item: ResourceLocation?,

    @JvmField
    var reach: Double?
) {
    companion object {
        @JvmField
        val CODEC: Codec<ItemReachOverride> = RecordCodecBuilder.create { instance ->
            instance.group(
                ResourceLocation.CODEC.fieldOf("item").forGetter(ItemReachOverride::item),
                Codec.DOUBLE.fieldOf("reach").forGetter(ItemReachOverride::reach)
            ).apply(instance, ::ItemReachOverride)
        }
    }
}
