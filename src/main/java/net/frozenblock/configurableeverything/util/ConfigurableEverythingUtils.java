package net.frozenblock.configurableeverything.util;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import java.nio.file.Path;
import java.util.HashMap;
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
