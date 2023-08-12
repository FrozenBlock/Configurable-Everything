package net.frozenblock.configurableeverything;

import net.fabricmc.api.ModInitializer;
import net.frozenblock.configurableeverything.biome.util.BiomeConfigUtil;
import net.frozenblock.configurableeverything.config.BiomePlacementConfig;
import net.frozenblock.configurableeverything.config.DataFixerConfig;
import net.frozenblock.configurableeverything.config.FluidConfig;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.config.ScreenShakeConfig;
import net.frozenblock.configurableeverything.config.WorldConfig;
import net.frozenblock.configurableeverything.datafixer.util.DataFixerUtils;
import net.frozenblock.configurableeverything.entity.util.EntityConfigUtil;
import net.frozenblock.configurableeverything.mod_compat.ConfigurableEverythingIntegrations;
import net.frozenblock.configurableeverything.splash_text.util.SplashTextConfigUtil;
import net.frozenblock.configurableeverything.surface_rule.util.SurfaceRuleConfigUtil;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstants;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtils;

public class ConfigurableEverything implements ModInitializer {

	@Override
	public void onInitialize() {
		ConfigurableEverythingUtils.startMeasuring(this);
		DataFixerUtils.applyDataFixes(ConfigurableEverythingSharedConstants.MOD_CONTAINER);

		ConfigurableEverythingIntegrations.init();
		// init configs
		MainConfig.get();
		BiomeConfigUtil.init();
		BiomePlacementConfig.get();
		DataFixerConfig.get();
		EntityConfigUtil.init();
		FluidConfig.get();
		ScreenShakeConfig.get();
		WorldConfig.get();
		SplashTextConfigUtil.init();
		SurfaceRuleConfigUtil.init();

		ConfigurableEverythingUtils.stopMeasuring(this);
	}
}
