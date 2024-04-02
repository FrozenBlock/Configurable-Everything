package net.frozenblock.configurableeverything.splash_text.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
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

    @ModifyExpressionValue(
		method = "render",
		at = @At(
			value = "CONSTANT",
			args = {
				"intValue=16776960"
			}
		))
    private int changeColor(int original) {
      var modified = SplashTextConfig.get(false).splashColor;
		  return MainConfig.get().splash_text && modified != null
        ? modified
        : original;
    }
}
