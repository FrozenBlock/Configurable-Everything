package net.frozenblock.configurableeverything.entity.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.attributes.Attribute;

public record AttributeAmplifier(ResourceKey<Attribute> attribute, double amplifier) {

	public static final Codec<AttributeAmplifier> CODEC = RecordCodecBuilder.create(instance ->
		instance.group(
			ResourceKey.codec(Registries.ATTRIBUTE).fieldOf("attribute").forGetter(AttributeAmplifier::attribute),
			Codec.DOUBLE.fieldOf("amplifier").forGetter(AttributeAmplifier::amplifier)
		).apply(instance, AttributeAmplifier::new)
	);
}
