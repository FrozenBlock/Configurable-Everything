package TEMP_FILES_TO_REORGANIZE;

import net.frozenblock.lib.screenshake.api.ScreenShakeManager;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Explosion.class)
public class ExplosionMixin {

	@Shadow @Final private Level level;
	@Shadow @Final private double x;
	@Shadow @Final private double y;
	@Shadow @Final private double z;
	@Shadow @Final private Explosion.BlockInteraction blockInteraction;
	@Shadow @Final private float radius;

	@Inject(method = "finalizeExplosion", at = @At(value = "TAIL"))
	public void finalizeExplosion(boolean spawnParticles, CallbackInfo info) {
		ScreenShakeManager.addScreenShake(this.level, (float) ((0.5F + (blockInteraction != Explosion.BlockInteraction.KEEP ? 0.2F : 0) + radius * 0.1) / 2F), (int) ((radius * 5) + 3), 1, this.x, this.y, this.z, radius * 2);
	}

}
