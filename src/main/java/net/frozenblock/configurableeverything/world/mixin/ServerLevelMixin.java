package net.frozenblock.configurableeverything.world.mixin;

import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.config.WorldConfig;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {

	@ModifyArgs(method = "tickTime", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;setDayTime(J)V"))
	private void tickTime(Args args) {
		long dayTimeSpeedAmplifier = WorldConfig.get().dayTimeSpeedAmplifier;
		if (MainConfig.get(false).world) {
			args.set(0, ((long)args.get(0) - 1L) + dayTimeSpeedAmplifier);
		} else {
			args.get(0);
		}
	}
}
