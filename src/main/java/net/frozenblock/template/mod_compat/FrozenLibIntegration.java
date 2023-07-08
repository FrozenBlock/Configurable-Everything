package net.frozenblock.template.mod_compat;

import net.frozenblock.lib.integration.api.ModIntegration;
import net.frozenblock.template.util.TemplateModSharedConstants;
import net.frozenblock.template.util.TemplateModUtils;

public class FrozenLibIntegration extends ModIntegration {
	public FrozenLibIntegration() {
		super("frozenlib");
	}

	@Override
	public void init() {
		TemplateModUtils.log("FrozenLib integration ran!", TemplateModSharedConstants.UNSTABLE_LOGGING);
	}
}
