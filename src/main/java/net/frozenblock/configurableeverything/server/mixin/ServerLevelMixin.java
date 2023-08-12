package net.frozenblock.configurableeverything.server.mixin;

import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.config.WorldConfig;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {

	@ModifyArg(method = "tickTime", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;setDayTime(J)V"), index = 0)
	private long tickTime(long time) {
		if (MainConfig.get().server) {
			return (time - 1L) + WorldConfig.get().dayTimeSpeedAmplifier;
		}
		return time;
	}
}
