package net.frozenblock.template.util;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

public final class TemplateModSharedConstants {
	private TemplateModSharedConstants() {
		throw new UnsupportedOperationException("TemplateModSharedConstants contains only static declarations.");
	}

	public static final String MOD_ID = "frozenblock_template_mod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static boolean DEV_LOGGING = false;
	/**
	 * Used for features that may be unstable and crash in public builds.
	 * <p>
	 * It's smart to use this for at least registries.
	 */
	public static boolean UNSTABLE_LOGGING = FabricLoader.getInstance().isDevelopmentEnvironment();
	public static final ModContainer MOD_CONTAINER = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow();
	public static boolean areConfigsInit = false;

	/**
	 * Used for datafixers.
	 * <p>
	 * Is not necessary for a normal mod, but can be useful in some cases.
	 */
	public static final int DATA_VERSION = 0;
}
