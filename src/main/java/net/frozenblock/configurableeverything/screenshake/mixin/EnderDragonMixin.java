package net.frozenblock.configurableeverything.screenshake.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.lib.screenshake.api.client.ScreenShaker;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(EnderDragon.class)
public class EnderDragonMixin {

	@Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playLocalSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V", shift = At.Shift.AFTER, ordinal = 0))
	public void mcFixes$growlScreenshake(CallbackInfo info) {
		ScreenShaker.SCREEN_SHAKES.add(
			new ScreenShaker.ClientEntityScreenShake(
				EnderDragon.class.cast(this),
				2.5F,
				140,
				90,
				48,
				0
			)
		);
	}

	@Inject(method = "onFlap", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playLocalSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V", shift = At.Shift.AFTER, ordinal = 0))
	public void mcFixes$flapScreenshake(CallbackInfo info) {
		ScreenShaker.SCREEN_SHAKES.add(
			new ScreenShaker.ClientEntityScreenShake(
				EnderDragon.class.cast(this),
				0.5F,
				8,
				1,
				26,
				0
			)
		);
	}

}
