package net.frozenblock.configurableeverything.util;

import com.mojang.datafixers.util.Pair;
import net.frozenblock.configurableeverything.config.BiomePlacementConfig;
import net.frozenblock.configurableeverything.biome_placement.util.BiomeParameters;
import net.frozenblock.configurableeverything.biome_placement.util.DimensionBiomeKeyList;
import net.frozenblock.configurableeverything.biome_placement.util.DimensionBiomeList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.DimensionType;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstants.*;

public final class ConfigurableEverythingUtils {
	private ConfigurableEverythingUtils() {
		throw new UnsupportedOperationException("ConfigurableEverythingUtils contains only static declarations.");
	}

	// CONFIG
	public static Path makePath(String name, boolean json5) {
		return Path.of("./config/" + MOD_ID + "/" + name + "." + (json5 ? "json5" : "json"));
	}

	// BIOME PARAMETERS
	public static List<Pair<Climate.ParameterPoint, Holder<Biome>>> biomeAdditions(HolderGetter<Biome> registryAccess, ResourceKey<DimensionType> dimension) {
		List<Pair<Climate.ParameterPoint, Holder<Biome>>> biomeAdditions = new ArrayList<>();
		var addedBiomes = BiomePlacementConfig.get().addedBiomes;
		if (addedBiomes != null && addedBiomes.value() != null) {
			var dimensionBiomes = addedBiomes.value().stream().filter(list -> list.dimension().equals(dimension)).toList();
			for (DimensionBiomeList list : dimensionBiomes) {
				for (BiomeParameters parameters : list.biomes()) {
					biomeAdditions.add(Pair.of(parameters.parameters(), registryAccess.getOrThrow(parameters.biome())));
				}
			}
		}
		return biomeAdditions;
	}

	public static List<ResourceKey<Biome>> biomeRemovals(ResourceKey<DimensionType> dimension) {
		List<ResourceKey<Biome>> biomeRemovals = new ArrayList<>();
		var removedBiomes = BiomePlacementConfig.get().removedBiomes;
		if (removedBiomes != null && removedBiomes.value() != null) {
			var dimensionBiomes = removedBiomes.value().stream().filter(list -> list.dimension().equals(dimension)).toList();
			for (DimensionBiomeKeyList list : dimensionBiomes) {
				biomeRemovals.addAll(list.biomes());
			}
		}
		return biomeRemovals;
	}

	// LOGGING
	public static void log(String string, boolean shouldLog) {
		if (shouldLog) {
			LOGGER.info(string);
		}
	}

	public static void log(Entity entity, String string, boolean shouldLog) {
		if (shouldLog) {
			LOGGER.info(entity.toString() + " : " + string + " : " + entity.position());
		}
	}

	public static void log(Block block, String string, boolean shouldLog) {
		if (shouldLog) {
			LOGGER.info(block.toString() + " : " + string + " : ");
		}
	}

	public static void log(Block block, BlockPos pos, String string, boolean shouldLog) {
		if (shouldLog) {
			LOGGER.info(block.toString() + " : " + string + " : " + pos);
		}
	}

	public static void logMod(String string, boolean shouldLog) {
		if (shouldLog) {
			LOGGER.info(string + " " + MOD_ID);
		}
	}

	// MEASURING
	public static final Map<Object, Long> INSTANT_MAP = new HashMap<>();

	public static void startMeasuring(Object object) {
		long started = System.nanoTime();
		String name = object.getClass().getName();
		LOGGER.info("Started measuring {}", name.substring(name.lastIndexOf(".") + 1));
		INSTANT_MAP.put(object, started);
	}

	public static void stopMeasuring(Object object) {
		if (INSTANT_MAP.containsKey(object)) {
			String name = object.getClass().getName();
			LOGGER.info("{} took {} nanoseconds", name.substring(name.lastIndexOf(".") + 1), System.nanoTime() - INSTANT_MAP.get(object));
			INSTANT_MAP.remove(object);
		}
	}

	public static ResourceLocation id(String path) {
		return new ResourceLocation(MOD_ID, path);
	}

	public static ResourceLocation vanillaId(String path) {
		return new ResourceLocation(ResourceLocation.DEFAULT_NAMESPACE, path);
	}

	public static String string(String path) {
		return id(path).toString();
	}
}
