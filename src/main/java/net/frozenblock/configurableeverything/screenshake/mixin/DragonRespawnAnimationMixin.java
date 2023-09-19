package net.frozenblock.configurableeverything.screenshake.mixin;

import java.util.List;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.config.ScreenShakeConfig;
import net.frozenblock.lib.screenshake.api.ScreenShakeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.level.dimension.end.DragonRespawnAnimation;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DragonRespawnAnimation.class)
public class DragonRespawnAnimationMixin {

	@Mixin(targets = "net/minecraft/world/level/dimension/end/DragonRespawnAnimation$2")
	private static class PreparingPillarsMixin {

		@Inject(method = "tick", at = @At("HEAD"))
		private void startShaking(ServerLevel world, EndDragonFight fight, List<EndCrystal> crystals, int i, BlockPos pos, CallbackInfo ci) {
			var config = ScreenShakeConfig.get();
			if (MainConfig.get().screen_shake == true) {
				if (i == 0 && config.dragonRespawnScreenShake == true) {
					ScreenShakeManager.addScreenShake(world, 0.9F, 60, 0, 130, 0, 180);
				}
			}
		}
	}

	@Mixin(targets = "net/minecraft/world/level/dimension/end/DragonRespawnAnimation$4")
	private static class SpawningDragonMixin {

		@Inject(method = "tick", at = @At("TAIL"))
		private void startShaking(ServerLevel world, EndDragonFight fight, List<EndCrystal> crystals, int i, BlockPos pos, CallbackInfo ci) {
			var config = ScreenShakeConfig.get();
			if (MainConfig.get().screen_shake) {
				if (i == 0 && config.dragonRespawnScreenShake) {
					ScreenShakeManager.addScreenShake(world, 1.9F, 140, 0, 130, 0, 180);
				}
			}
		}
	}
}
