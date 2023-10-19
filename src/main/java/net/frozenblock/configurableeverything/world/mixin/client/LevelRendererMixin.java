package net.frozenblock.configurableeverything.world.mixin.client;

import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.config.WorldConfig;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

	@ModifyConstant(method = "renderSky", constant = @Constant(floatValue = 30.0F))
	private float changeSunSize(float original) {
		if (MainConfig.get(false).world == true) {
			var modified = WorldConfig.getSunSize();
			if (modified != null) return modified;
		}
		return original;
	}

	@ModifyConstant(method = "renderSky", constant = @Constant(floatValue = 20.0F))
	private float changeMoonSize(float original) {
		if (MainConfig.get(false).world == true) {
			var modified = WorldConfig.getMoonSize();
			if (modified != null) return modified;
		}
		return original;
	}
}
