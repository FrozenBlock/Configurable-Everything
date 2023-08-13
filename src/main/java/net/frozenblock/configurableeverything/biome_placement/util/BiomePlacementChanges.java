package net.frozenblock.configurableeverything.biome_placement.util;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class BiomePlacementChanges {

	private BiomePlacementChanges() {
		throw new UnsupportedOperationException("BiomePlacementChanges contains only static declarations.");
	}

	private static final BiomePlacementChangeManager MANAGER = BiomePlacementChangeManager.INSTANCE;

	@Nullable
	public static List<BiomePlacementChange> getChanges() {
		return MANAGER.getChanges();
	}

	@Nullable
	public static BiomePlacementChange getChange(ResourceLocation id) {
		return MANAGER.getChange(id);
	}

	public static void addChange(ResourceLocation key, List<DimensionBiomeList> addedBiomes, List<DimensionBiomeKeyList> removedBiomes) {
		MANAGER.addChange(key, addedBiomes, removedBiomes);
	}

	public static void addChange(ResourceLocation key, BiomePlacementChange change) {
		MANAGER.addChange(key, change);
	}
}
