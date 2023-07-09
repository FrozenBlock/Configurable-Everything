package net.frozenblock.configurableeverything.util;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.DimensionType;

public interface ParameterListExtension {

	void updateBiomesList(RegistryAccess registryAccess, ResourceKey<DimensionType> dimension);
}
