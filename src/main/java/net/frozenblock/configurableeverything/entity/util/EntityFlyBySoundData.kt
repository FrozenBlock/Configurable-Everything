package net.frozenblock.configurableeverything.entity.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.resources.ResourceLocation

data class EntityFlyBySoundData(
    @JvmField var category: String,
    @JvmField var sound: ResourceLocation,
    @JvmField var volume: Float,
    @JvmField var pitch: Float
) {
    companion object {
        @JvmField
        val CODEC: Codec<EntityFlyBySoundData> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.STRING.fieldOf("category").forGetter(EntityFlyBySoundData::category),
                ResourceLocation.CODEC.fieldOf("sound").forGetter(EntityFlyBySoundData::sound),
                Codec.FLOAT.fieldOf("volume").forGetter(EntityFlyBySoundData::volume),
                Codec.FLOAT.fieldOf("pitch").forGetter(EntityFlyBySoundData::pitch)
            ).apply(instance, ::EntityFlyBySoundData)
        }
    }

    override fun toString(): String = "EntityFlyBySoundData[category=$category, sound=$sound, volume=$volume, pitch=$pitch]"
}
