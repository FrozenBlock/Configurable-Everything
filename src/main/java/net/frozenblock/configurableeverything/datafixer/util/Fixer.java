package net.frozenblock.configurableeverything.datafixer.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public record Fixer(ResourceLocation oldId, ResourceLocation newId) {

	public static final Codec<Fixer> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					ResourceLocation.CODEC.fieldOf("old_id").forGetter(Fixer::oldId),
					ResourceLocation.CODEC.fieldOf("new_id").forGetter(Fixer::newId)
			).apply(instance, Fixer::new)
	);
}
