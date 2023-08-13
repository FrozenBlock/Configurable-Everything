package net.frozenblock.configurableeverything;

import net.fabricmc.api.ModInitializer;
import net.frozenblock.configurableeverything.biome.util.BiomeConfigUtil;
import net.frozenblock.configurableeverything.biome_placement.util.BiomePlacementUtils;
import net.frozenblock.configurableeverything.config.*;
import net.frozenblock.configurableeverything.datafixer.util.DataFixerUtils;
import net.frozenblock.configurableeverything.entity.util.EntityConfigUtil;
import net.frozenblock.configurableeverything.mod_compat.ConfigurableEverythingIntegrations;
import net.frozenblock.configurableeverything.splash_text.util.SplashTextConfigUtil;
import net.frozenblock.configurableeverything.surface_rule.util.SurfaceRuleConfigUtil;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstants;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtils;
import net.minecraft.FileUtil;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigurableEverything implements ModInitializer {

	@Override
	public void onInitialize() {
		ConfigurableEverythingUtils.startMeasuring(this);
		DataFixerUtils.applyDataFixes(ConfigurableEverythingSharedConstants.MOD_CONTAINER);

		ConfigurableEverythingIntegrations.init();
		// init configs
		MainConfig.get();
		BiomeConfigUtil.init();
		BiomePlacementUtils.init();
		DataFixerConfig.get();
		EntityConfigUtil.init();
		FluidConfig.get();
		ScreenShakeConfig.get();
		WorldConfig.get();
		SplashTextConfigUtil.init();
		SurfaceRuleConfigUtil.init();

		try {
			FileUtil.createDirectoriesSafe(ConfigurableEverythingSharedConstants.DATAPACKS_PATH);
		} catch (IOException e) {
			throw new RuntimeException("Unable to create Configurable Everything datapacks folder", e);
		}

		ConfigurableEverythingUtils.stopMeasuring(this);
	}
}
