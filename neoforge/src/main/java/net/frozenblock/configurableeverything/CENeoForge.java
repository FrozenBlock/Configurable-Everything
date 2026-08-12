package net.frozenblock.configurableeverything;

import net.frozenblock.configurableeverything.util.CEConstantsKt;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(CEConstantsKt.MOD_ID)
public class CENeoForge {

	public CENeoForge(IEventBus modBus) {
		ConfigurableEverything.INSTANCE.init();
	}
}
