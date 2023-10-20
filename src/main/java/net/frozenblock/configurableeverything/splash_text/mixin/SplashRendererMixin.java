package net.frozenblock.configurableeverything.splash_text.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.config.SplashTextConfig;
import net.minecraft.client.gui.components.SplashRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(SplashRenderer.class)
@Environment(EnvType.CLIENT)
public class SplashRendererMixin {

    @ModifyConstant(method = "render", constant = @Constant(intValue = 16776960))
    private int changeColor(int original) {
      var modified = SplashTextConfig.get(false).splashColor;
		  return MainConfig.get(false).splash_text == true && modified != null
        ? modified
        : original;
    }
}
