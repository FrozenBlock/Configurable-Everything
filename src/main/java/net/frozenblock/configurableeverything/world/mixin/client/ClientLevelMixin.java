package net.frozenblock.configurableeverything.world.mixin.client;

import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.config.WorldConfig;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {

	@ModifyArgs(method = "tickTime", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;setDayTime(J)V"))
	private void tickTime(Args args) {
		if (MainConfig.get().world) {
			args.set(0, ((long)args.get(0) - 1L) + WorldConfig.get().dayTimeSpeedAmplifier);
		} else {
			args.get(0);
		}
	}
}
