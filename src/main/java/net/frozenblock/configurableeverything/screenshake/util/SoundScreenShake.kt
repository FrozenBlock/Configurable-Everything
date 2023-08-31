package net.frozenblock.configurableeverything.screenshake.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public record SoundScreenShake(ResourceLocation sound, float intensity, int duration, int falloffStart, float maxDistance) {

	public static final Codec<SoundScreenShake> CODEC = RecordCodecBuilder.create(instance ->
		instance.group(
			ResourceLocation.CODEC.fieldOf("sound").forGetter(SoundScreenShake::sound),
			Codec.FLOAT.fieldOf("intensity").forGetter(SoundScreenShake::intensity),
			Codec.INT.fieldOf("duration").forGetter(SoundScreenShake::duration),
			Codec.INT.fieldOf("falloffStart").forGetter(SoundScreenShake::falloffStart),
			Codec.FLOAT.fieldOf("maxDistance").forGetter(SoundScreenShake::maxDistance)
		).apply(instance, SoundScreenShake::new)
	);
}
