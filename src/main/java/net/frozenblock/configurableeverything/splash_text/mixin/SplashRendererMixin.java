package net.frozenblock.configurableeverything.splash_text.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.config.SplashTextConfig;
import net.minecraft.client.gui.components.SplashRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SplashRenderer.class)
@Environment(EnvType.CLIENT)
public class SplashRendererMixin {

    @WrapOperation(
		method = "render",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/util/ARGB;color(FI)I"
		))
    private int changeColor(float f, int i, Operation<Integer> original) {
		var modified = SplashTextConfig.get(false).splashColor;
		return MainConfig.get().splash_text ? modified : original.call(f, i);
    }
}
