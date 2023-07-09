package net.frozenblock.configurableeverything.biome.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import java.util.List;

public interface BiomeSourceExtension {

	void updateBiomesList(List<Holder<Biome>> biomesToAdd, List<Holder<Biome>> biomesToRemove);
}
