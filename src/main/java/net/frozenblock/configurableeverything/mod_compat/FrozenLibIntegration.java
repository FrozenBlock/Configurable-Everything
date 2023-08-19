package net.frozenblock.configurableeverything.mod_compat;

import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstants;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtilsKt;
import net.frozenblock.lib.integration.api.ModIntegration;

public class FrozenLibIntegration extends ModIntegration {
	public FrozenLibIntegration() {
		super("frozenlib");
	}

	@Override
	public void init() {
		ConfigurableEverythingUtilsKt.log("FrozenLib integration ran!", ConfigurableEverythingSharedConstants.UNSTABLE_LOGGING);
	}
}
