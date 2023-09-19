package net.frozenblock.configurableeverything.splash_text.mixin;

import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.config.SplashTextConfig;
import net.minecraft.client.gui.components.SplashRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(SplashRenderer.class)
public class SplashRendererMixin {

    @ModifyConstant(method = "render", constant = @Constant(intValue = 16776960))
    private int changeColor(int original) {
      var modified = SplashTextConfig.get().splashColor
		  return MainConfig.get().splash_text == true && modified != null
        ? modified
        : original;
    }
}
