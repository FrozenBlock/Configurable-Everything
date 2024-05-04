package net.frozenblock.configurableeverything.world.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.config.WorldConfig;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = LevelRenderer.class, priority = 600)
public class LevelRendererMixin {

	@ModifyExpressionValue(
		method = "renderSky",
		at = @At(
			value = "CONSTANT",
			args = {
				"floatValue=30.0F"
			}
		)
	)
	private float changeSunSize(float original) {
		if (!MainConfig.get().world) return original;
		return WorldConfig.getSunSize();
	}

	@ModifyExpressionValue(
		method = "renderSky",
		at = @At(
			value = "CONSTANT",
			args = {
				"floatValue=20.0F"
			}
		)
	)
	private float changeMoonSize(float original) {
		if (!MainConfig.get().world) return original;
		return WorldConfig.getMoonSize();
	}
}
