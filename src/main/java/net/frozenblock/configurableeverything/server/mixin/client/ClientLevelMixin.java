package net.frozenblock.configurableeverything.server.mixin.client;

import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.config.WorldConfig;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {

	@ModifyArg(method = "tickTime", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;setDayTime(J)V"), index = 0)
	private long tickTime(long time) {
		if (MainConfig.get().server) {
			return (time - 1L) + WorldConfig.get().dayTimeSpeedAmplifier;
		}
		return time;
	}
}
