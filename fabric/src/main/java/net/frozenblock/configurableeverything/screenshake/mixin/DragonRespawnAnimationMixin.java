package net.frozenblock.configurableeverything.screenshake.mixin;

import java.util.List;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.config.ScreenShakeConfig;
import net.frozenblock.configurableeverything.screenshake.util.ScreenShakeConfigUtil;
import net.frozenblock.lib.screenshake.api.ScreenShake;
import net.frozenblock.lib.screenshake.api.ScreenShakes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.level.dimension.end.EnderDragonFight;
import net.minecraft.world.phys.Vec3;
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
					ScreenShakes.add(
						level,
						ScreenShake.builder(level, new Vec3(130, 0, 180))
							.intensity(0.9F)
							.duration(60)
							.falloffStartDuration(0)
							.build()
					);
				}
			}
		}
	}

	@Mixin(targets = "net/minecraft/world/level/dimension/end/DragonRespawnStage$4")
	private static class SpawningDragonMixin {

		@Inject(method = "tick", at = @At("TAIL"))
		private void startShaking(ServerLevel level, EnderDragonFight fight, List<EndCrystal> crystals, int time, CallbackInfo ci) {
			if (MainConfig.screen_shake.get()) {
				if (time == 0 && ScreenShakeConfig.dragonRespawnScreenShake.get()) {
					ScreenShakes.add(
						level,
						ScreenShake.builder(level, new Vec3(130, 0, 180))
							.intensity(1.9F)
							.duration(140)
							.falloffStartDuration(0)
							.build()
					);
				}
			}
		}
	}
}
