package net.frozenblock.configurableeverything.entity.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import java.util.List;

public record EntityAttributeAmplifier(ResourceKey<EntityType<?>> entity, List<AttributeAmplifier> amplifiers) {

	public static final Codec<EntityAttributeAmplifier> CODEC = RecordCodecBuilder.create(instance ->
		instance.group(
			ResourceKey.codec(Registries.ENTITY_TYPE).fieldOf("entity").forGetter(EntityAttributeAmplifier::entity),
			AttributeAmplifier.CODEC.listOf().fieldOf("amplifiers").forGetter(EntityAttributeAmplifier::amplifiers)
		).apply(instance, EntityAttributeAmplifier::new)
	);
}
