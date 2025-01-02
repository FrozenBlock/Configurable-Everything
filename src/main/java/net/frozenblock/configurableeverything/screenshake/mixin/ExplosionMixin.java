package net.frozenblock.configurableeverything.screenshake.mixin;

import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.config.ScreenShakeConfig;
import net.frozenblock.lib.screenshake.api.ScreenShakeManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerExplosion;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerExplosion.class)
public class ExplosionMixin {

	@Shadow
	@Final
	private ServerLevel level;

	@Shadow
	@Final
	private Explosion.BlockInteraction blockInteraction;

	@Shadow
	@Final
	private float radius;

	@Shadow
	@Final
	private Vec3 center;

	@Inject(method = "explode", at = @At(value = "TAIL"))
	public void finalizeExplosion(CallbackInfo ci) {
		var config = ScreenShakeConfig.get();

		if (!MainConfig.get().screen_shake || !config.explosionScreenShake)
			return;

		double x = this.center.x;
		double y = this.center.y;
		double z = this.center.z;

		ScreenShakeManager.addScreenShake(this.level, (float) ((0.5F + (blockInteraction != Explosion.BlockInteraction.KEEP ? 0.2F : 0) + radius * 0.1) / 2F), (int) ((radius * 5) + 3), 1, x, y, z, radius * 2);
	}

}
