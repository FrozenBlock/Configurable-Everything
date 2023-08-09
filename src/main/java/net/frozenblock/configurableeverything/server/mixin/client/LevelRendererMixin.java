package net.frozenblock.configurableeverything.server.mixin.client;

import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.config.ServerConfig;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

	@ModifyConstant(method = "renderSky", constant = @Constant(floatValue = 30.0F))
	private float changeSunSize(float original) {
		var config = ServerConfig.get();
		if (MainConfig.get().server) {
			return config.sunSize;
		}
		return original;
	}

	@ModifyConstant(method = "renderSky", constant = @Constant(floatValue = 20.0F))
	private float changeMoonSize(float original) {
		var config = ServerConfig.get();
		if (MainConfig.get().server) {
			return config.moonSize;
		}
		return original;
	}
}
