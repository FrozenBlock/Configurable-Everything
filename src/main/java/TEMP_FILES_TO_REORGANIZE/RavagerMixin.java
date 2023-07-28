package TEMP_FILES_TO_REORGANIZE;

import net.frozenblock.lib.screenshake.api.ScreenShakeManager;
import net.minecraft.world.entity.monster.Ravager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Ravager.class)
public class RavagerMixin {

	@Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Ravager;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"))
	private void startShaking(CallbackInfo ci) {
		ScreenShakeManager.addEntityScreenShake(Ravager.class.cast(this), 1F, 17, 23);
	}

}
