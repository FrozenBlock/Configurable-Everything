package net.frozenblock.configurableeverything.fluid.mixin;

import net.frozenblock.configurableeverything.config.FluidConfig;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.LavaFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LavaFluid.class)
public class LavaFluidMixin {

	@Inject(method = "getTickDelay", at = @At("RETURN"), cancellable = true)
	private void getTickDelay(LevelReader level, CallbackInfoReturnable<Integer> cir) {
		if (MainConfig.get().fluid) {
			var config = FluidConfig.get();
			var flowSpeeds = config.flowSpeeds;
			if (flowSpeeds != null && flowSpeeds.value() != null) {
				for (var flowSpeed : flowSpeeds.value()) {
					if (flowSpeed.fluid().equals(BuiltInRegistries.FLUID.getResourceKey(Fluid.class.cast(this)).orElseThrow())) {
						cir.setReturnValue(level.dimensionType().ultraWarm() ? flowSpeed.ultraWarmFlowTickDelay() : flowSpeed.flowTickDelay());
					}
				}
			}
		}
	}
}
