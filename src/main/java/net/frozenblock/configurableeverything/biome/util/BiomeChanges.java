package net.frozenblock.configurableeverything.biome.util;

import java.util.List;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class BiomeChanges {

	private BiomeChanges() {
		throw new UnsupportedOperationException("BiomeChanges contains only static declarations.");
	}

	private static final BiomeChangeManager MANAGER = BiomeChangeManager.INSTANCE;

	@Nullable
	public static List<BiomeChange> getChanges() {
		return MANAGER.getChanges();
	}

	@Nullable
	public static BiomeChange getChange(ResourceLocation id) {
		return MANAGER.getChange(id);
	}

	public static void addChange(ResourceLocation key, List<BiomePlacedFeatureList> addedFeatures, List<BiomePlacedFeatureList> removedFeatures, List<BiomePlacedFeatureReplacementList> replacedFeatures, List<BiomeMusic> musicReplacements) {
		MANAGER.addChange(key, addedFeatures, removedFeatures, replacedFeatures, musicReplacements);
	}

	public static void addChange(ResourceLocation key, BiomeChange change) {
		MANAGER.addChange(key, change);
	}
}
