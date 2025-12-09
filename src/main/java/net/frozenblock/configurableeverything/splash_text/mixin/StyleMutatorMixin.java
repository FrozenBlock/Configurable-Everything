package net.frozenblock.configurableeverything.splash_text.mixin;

import net.frozenblock.configurableeverything.splash_text.util.StyleMutator;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Style.class)
public class StyleMutatorMixin implements StyleMutator {
	@Mutable
	@Shadow
	@Final
	@Nullable TextColor color;

	@Override
	public void configurableEverything$setTextColor(int color) {
		this.color = TextColor.fromRgb(color);
	}

	@Override
	public void configurableEverything$setTextColor(TextColor color) {
		this.color = color;
	}
}
