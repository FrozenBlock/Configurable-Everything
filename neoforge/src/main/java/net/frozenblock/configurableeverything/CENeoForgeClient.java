package net.frozenblock.configurableeverything;

import net.frozenblock.configurableeverything.config.gui.main.ConfigurableEverythingConfigGui;
import net.frozenblock.configurableeverything.util.CEConstantsKt;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = CEConstantsKt.MOD_ID, dist = Dist.CLIENT)
public class CENeoForgeClient {

	public CENeoForgeClient(IEventBus modBus) {
		ModLoadingContext.get().registerExtensionPoint(
			IConfigScreenFactory.class,
			() -> (container, parent) ->
				ConfigurableEverythingConfigGui.buildScreen(parent)
		);
	}
}
