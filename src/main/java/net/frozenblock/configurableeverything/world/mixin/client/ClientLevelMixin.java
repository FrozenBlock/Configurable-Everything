package net.frozenblock.configurableeverything.world.mixin.client;

import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.config.WorldConfig;
import net.frozenblock.configurableeverything.world.impl.ClientLevelDataInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {

	@Shadow @Final
	private ClientLevel.ClientLevelData clientLevelData;

	@ModifyArgs(method = "tickTime", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;setDayTime(J)V"))
	private void tickTime(Args args) {
		if (MainConfig.get().world) {
			long prevTime = ((long)args.get(0) - 1L);
			if (WorldConfig.get().fixSunMoonRotating) {
				this.configurableEverything$setPreviousDayTime(prevTime);
			}
			args.set(0, prevTime + WorldConfig.get().dayTimeSpeedAmplifier);
		} else {
			args.get(0);
		}
	}

	@Unique
	public void configurableEverything$setPreviousDayTime(long time) {
		((ClientLevelDataInterface)this.clientLevelData).configurableEverything$setPreviousDayTime(time);
	}
}
