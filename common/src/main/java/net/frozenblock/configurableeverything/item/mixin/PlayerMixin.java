package net.frozenblock.configurableeverything.item.mixin;

import net.frozenblock.configurableeverything.item.util.ItemConfigUtil;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Player.class, priority = 500)
public class PlayerMixin {

	@Inject(method = "blockInteractionRange", at = @At("HEAD"), cancellable = true)
	private void getReachDistance(CallbackInfoReturnable<Double> cir) {
		Double override = ItemConfigUtil.getReach(Player.class.cast(this));
		if (override != null) cir.setReturnValue(override);
	}

	@Inject(method = "entityInteractionRange", at = @At("HEAD"), cancellable = true)
	private void getAttackRange(CallbackInfoReturnable<Double> cir){
		Double override = ItemConfigUtil.getReach(Player.class.cast(this));
		if (override != null) cir.setReturnValue(override);
	}
}
