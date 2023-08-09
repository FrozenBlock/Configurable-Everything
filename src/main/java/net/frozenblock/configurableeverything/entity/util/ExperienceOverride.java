package net.frozenblock.configurableeverything.entity.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;

public record ExperienceOverride(ResourceKey<EntityType<?>> entity, int amount) {

	public static final Codec<ExperienceOverride> CODEC = RecordCodecBuilder.create(instance ->
		instance.group(
			ResourceKey.codec(Registries.ENTITY_TYPE).fieldOf("entity").forGetter(ExperienceOverride::entity),
			Codec.INT.fieldOf("amount").forGetter(ExperienceOverride::amount)
		).apply(instance, ExperienceOverride::new)
	);
}
