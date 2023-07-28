package TEMP_FILES_TO_REORGANIZE;

import net.frozenblock.lib.screenshake.api.ScreenShakeManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.warden.SonicBoom;
import net.minecraft.world.entity.monster.warden.Warden;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SonicBoom.class)
public class SonicBoomMixin {

	@Inject(method = {"m_ehrxwrfs","method_43265","lambda$tick$2"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/warden/Warden;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V", ordinal = 0), require = 1)
	private static void startShaking(Warden warden, ServerLevel world, LivingEntity livingEntity, CallbackInfo ci) {
		ScreenShakeManager.addScreenShake(world, 2F, 25, warden.getX(), warden.getY(), warden.getZ(), 18);
	}

}
