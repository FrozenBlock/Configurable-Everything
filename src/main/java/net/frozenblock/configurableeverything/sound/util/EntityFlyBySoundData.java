package net.frozenblock.configurableeverything.sound.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public record EntityFlyBySoundData(String category, ResourceLocation sound, float volume, float pitch) {

    public static final Codec<EntityFlyBySoundData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("category").forGetter(EntityFlyBySoundData::category),
                    ResourceLocation.CODEC.fieldOf("sound").forGetter(EntityFlyBySoundData::sound),
                    Codec.FLOAT.fieldOf("volume").forGetter(EntityFlyBySoundData::volume),
                    Codec.FLOAT.fieldOf("pitch").forGetter(EntityFlyBySoundData::pitch)
            ).apply(instance, EntityFlyBySoundData::new)
    );
}
