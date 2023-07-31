package net.frozenblock.configurableeverything.entity.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public record EntityFlyBySound(ResourceLocation entity, EntityFlyBySoundData sound) {

    public static final Codec<EntityFlyBySound> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("entity").forGetter(EntityFlyBySound::entity),
                    EntityFlyBySoundData.CODEC.fieldOf("sound").forGetter(EntityFlyBySound::sound)
            ).apply(instance, EntityFlyBySound::new)
    );
}
