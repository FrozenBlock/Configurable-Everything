package net.frozenblock.configurableeverything.world.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
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

	@ModifyExpressionValue(method = "tickTime", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
	private boolean configurableEverything$tickTime(boolean original) {
		if (!original && MainConfig.get(false).world == true && WorldConfig.get(false).fixSunMoonRotating == true) {
			this.configurableEverything$setPreviousDayTime(this.clientLevelData.getDayTime());
		}
		return original;
	}

	@ModifyArgs(method = "tickTime", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;setDayTime(J)V"))
	private void configurableEverything$tickTime(Args args) {
		if (MainConfig.get(false).world == true) {
			long prevTime = ((long)args.get(0) - 1L);
			if (WorldConfig.get(false).fixSunMoonRotating == true) {
				this.configurableEverything$setPreviousDayTime(prevTime);
			}
			var dayTimeSpeedAmplifier = WorldConfig.get(false).dayTimeSpeedAmplifier;
			if (dayTimeSpeedAmplifier != null) {
				args.set(0, prevTime + dayTimeSpeedAmplifier);
			}
		} else {
			args.get(0);
		}
	}

	@Unique
	public void configurableEverything$setPreviousDayTime(long time) {
		((ClientLevelDataInterface)this.clientLevelData).configurableEverything$setPreviousDayTime(time);
	}
}
