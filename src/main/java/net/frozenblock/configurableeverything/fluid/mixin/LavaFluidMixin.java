package net.frozenblock.configurableeverything.fluid.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.frozenblock.configurableeverything.fluid.util.FluidConfigUtils;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.LavaFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LavaFluid.class)
public class LavaFluidMixin {

	@ModifyReturnValue(method = "getTickDelay", at = @At("RETURN"))
	private int getTickDelay(int original, LevelReader level) {
		return FluidConfigUtils.getTickDelay(original, Fluid.class.cast(this), level);
	}
}
