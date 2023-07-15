package net.frozenblock.configurableeverything.biome_placement.util;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import java.util.List;

public interface BiomeSourceExtension {

	void updateBiomesList(List<Holder<Biome>> biomesToAdd, List<Holder<Biome>> biomesToRemove);
}
