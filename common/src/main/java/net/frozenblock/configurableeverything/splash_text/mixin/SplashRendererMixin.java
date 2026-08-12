package net.frozenblock.configurableeverything.splash_text.mixin;

import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.config.SplashTextConfig;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SplashRenderer.class)
@ClientOnly
public class SplashRendererMixin {

	@Mutable
	@Shadow
	@Final
	private Component splash;

	@Unique
	private TextColor configurableEverything$originalColor;

	@Unique
	private boolean configurableEverything$wasLastModified = false;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void changeColor(Component splash, CallbackInfo ci) {
		this.configurableEverything$originalColor = this.splash.getStyle().getColor();
		this.configurableEverything$changeColor();
	}

    @Inject(
		method = "extractRenderState",
		at = @At("HEAD")
	)
    private void changeColor(GuiGraphicsExtractor graphics, int screenWidth, Font font, float alpha, CallbackInfo ci) {
		this.configurableEverything$changeColor();
    }

	@Unique
	private void configurableEverything$changeColor() {
		var modified = SplashTextConfig.splashColor.get();
		if (MainConfig.splash_text.get()) {
			this.splash = this.splash.copy().withColor(modified);
			this.configurableEverything$wasLastModified = true;
		} else if (this.configurableEverything$wasLastModified) {
			this.splash = this.splash.copy().withColor(this.configurableEverything$originalColor.getValue());
			this.configurableEverything$wasLastModified = false;
		}
	}
}
