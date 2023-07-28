package TEMP_FILES_TO_REORGANIZE;

import net.frozenblock.lib.screenshake.api.ScreenShakeManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.warden.Warden;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Warden.class)
public class WardenMixin {

	@Inject(method = "doHurtTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/warden/Warden;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"))
	private void startShaking(Entity target, CallbackInfoReturnable<Boolean> cir) {
		Warden warden = Warden.class.cast(this);
		ScreenShakeManager.addScreenShake(warden.level(), 1F, 3, warden.getX(), warden.getY(), warden.getZ(), 2);
	}

}
