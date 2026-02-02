package net.frozenblock.configurableeverything.screenshake.mixin;

import java.util.List;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.config.ScreenShakeConfig;
import net.frozenblock.lib.screenshake.api.ScreenShakeManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.level.dimension.end.EnderDragonFight;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class DragonRespawnAnimationMixin {

	@Mixin(targets = "net/minecraft/world/level/dimension/end/DragonRespawnStage$2")
	private static class PreparingPillarsMixin {

		@Inject(method = "tick", at = @At("HEAD"))
		private void startShaking(ServerLevel level, EnderDragonFight fight, List<EndCrystal> crystals, int time, CallbackInfo ci) {
			if (MainConfig.screen_shake.get()) {
				if (time == 0 && ScreenShakeConfig.dragonRespawnScreenShake.get()) {
					ScreenShakeManager.addScreenShake(level, 0.9F, 60, 0, 130, 0, 180);
				}
			}
		}
	}

	@Mixin(targets = "net/minecraft/world/level/dimension/end/DragonRespawnStage$4")
	private static class SpawningDragonMixin {

		@Inject(method = "tick", at = @At("TAIL"))
		private void startShaking(ServerLevel world, EnderDragonFight fight, List<EndCrystal> crystals, int time, CallbackInfo ci) {
			if (MainConfig.screen_shake.get()) {
				if (time == 0 && ScreenShakeConfig.dragonRespawnScreenShake.get()) {
					ScreenShakeManager.addScreenShake(world, 1.9F, 140, 0, 130, 0, 180);
				}
			}
		}
	}
}
