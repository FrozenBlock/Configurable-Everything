package net.frozenblock.configurableeverything;

import net.frozenblock.configurableeverything.util.CEConstantsKt;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@SuppressWarnings("KotlinInternalInJava")
@Mod(CEConstantsKt.MOD_ID)
public class ConfigurableEverythingNeoForge {

	public ConfigurableEverythingNeoForge(IEventBus modBus) {
		ConfigurableEverything.INSTANCE.init();
	}
}
