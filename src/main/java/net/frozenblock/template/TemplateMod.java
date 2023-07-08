package net.frozenblock.template;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.ModContainer;
import net.frozenblock.template.mod_compat.TemplateModIntegrations;
import net.frozenblock.template.util.TemplateModSharedConstants;
import net.frozenblock.template.util.TemplateModUtils;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.qsl.frozenblock.misc.datafixerupper.api.QuiltDataFixerBuilder;
import org.quiltmc.qsl.frozenblock.misc.datafixerupper.api.QuiltDataFixes;

public class TemplateMod implements ModInitializer {

	@Override
	public void onInitialize() {
		TemplateModUtils.startMeasuring(this);
		applyDataFixes(TemplateModSharedConstants.MOD_CONTAINER);

		TemplateModIntegrations.init();

		TemplateModUtils.stopMeasuring(this);
	}

	private static void applyDataFixes(final @NotNull ModContainer mod) {
		TemplateModUtils.log("Applying DataFixes for FrozenBlock Template Mod with Data Version " + TemplateModSharedConstants.DATA_VERSION, true);

		var builder = new QuiltDataFixerBuilder(TemplateModSharedConstants.DATA_VERSION);
		builder.addSchema(0, QuiltDataFixes.BASE_SCHEMA);

		QuiltDataFixes.buildAndRegisterFixer(mod, builder);
		TemplateModUtils.log("DataFixes for FrozenBlock Template Mod have been applied", true);
	}
}
