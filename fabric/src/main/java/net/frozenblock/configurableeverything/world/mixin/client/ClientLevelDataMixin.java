package net.frozenblock.configurableeverything.world.mixin.client;

import net.frozenblock.configurableeverything.world.impl.ClientLevelDataInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ClientLevel.ClientLevelData.class)
public class ClientLevelDataMixin implements ClientLevelDataInterface {

	@Unique
	private long configurableEverything$previousDayTime;

	@Unique
	@Override
	public void configurableEverything$setPreviousDayTime(long previousDayTime) {
		this.configurableEverything$previousDayTime = previousDayTime;
	}

	@Unique
	@Override
	public long configurableEverything$getPreviousDayTime() {
		return this.configurableEverything$previousDayTime;
	}
}
