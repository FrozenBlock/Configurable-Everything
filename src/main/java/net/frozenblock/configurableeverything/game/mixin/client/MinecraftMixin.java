package net.frozenblock.configurableeverything.game.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.frozenblock.configurableeverything.config.GameConfig;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Minecraft.class)
public class MinecraftMixin {

	@ModifyReturnValue(method = "createTitle", at = @At("RETURN"))
	private String createTitle(String original) {
		var config = GameConfig.get();
		if (MainConfig.get().game) {
			var title = config.windowTitle;
			if (!title.isEmpty()) {
				return title;
			}
		}
		return original;
	}
}
