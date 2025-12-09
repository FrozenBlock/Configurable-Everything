package net.frozenblock.configurableeverything.splash_text.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.config.SplashTextConfig;
import net.frozenblock.configurableeverything.splash_text.util.StyleMutator;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SplashRenderer.class)
@Environment(EnvType.CLIENT)
public class SplashRendererMixin {

	@Shadow
	@Final
	private Component splash;

	@Unique
	private TextColor originalColor;

	@Unique
	private boolean wasLastModified = false;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void changeColor(Component component, CallbackInfo ci) {
		this.originalColor = this.splash.getStyle().getColor();
		this.changeColor();
	}

    @Inject(
		method = "render",
		at = @At("HEAD")
	)
    private void changeColor(GuiGraphics guiGraphics, int i, Font font, float f, CallbackInfo ci) {
		this.changeColor();
    }

	@Unique
	private void changeColor() {
		var modified = SplashTextConfig.get(false).splashColor;
		if (MainConfig.get().splash_text) {
			StyleMutator.class.cast(this.splash.getStyle()).configurableEverything$setTextColor(modified);
			this.wasLastModified = true;
		} else if (this.wasLastModified) {
			StyleMutator.class.cast(this.splash.getStyle()).configurableEverything$setTextColor(this.originalColor);
			this.wasLastModified = false;
		}
	}
}
