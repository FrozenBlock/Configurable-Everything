package net.frozenblock.configurableeverything.mod_compat;

import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstants;
import net.frozenblock.lib.integration.api.ModIntegration;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtils;

public class FrozenLibIntegration extends ModIntegration {
	public FrozenLibIntegration() {
		super("frozenlib");
	}

	@Override
	public void init() {
		ConfigurableEverythingUtils.log("FrozenLib integration ran!", ConfigurableEverythingSharedConstants.UNSTABLE_LOGGING);
	}
}
