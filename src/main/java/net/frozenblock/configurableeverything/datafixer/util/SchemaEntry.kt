package net.frozenblock.configurableeverything.datafixer.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;

public record SchemaEntry(int version, List<DataFixEntry> entries) {
	public static final Codec<SchemaEntry> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					Codec.INT.fieldOf("version").forGetter(SchemaEntry::version),
					Codec.list(DataFixEntry.CODEC).fieldOf("data_fixes").forGetter(SchemaEntry::entries)
			).apply(instance, SchemaEntry::new)
	);
}
