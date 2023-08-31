package net.frozenblock.configurableeverything.datafixer.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;

public record DataFixEntry(String type, List<Fixer> fixers) {

	public static final Codec<DataFixEntry> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					Codec.STRING.fieldOf("type").forGetter(DataFixEntry::type),
					Codec.list(Fixer.CODEC).fieldOf("fixers").forGetter(DataFixEntry::fixers)
			).apply(instance, DataFixEntry::new)
	);
}
