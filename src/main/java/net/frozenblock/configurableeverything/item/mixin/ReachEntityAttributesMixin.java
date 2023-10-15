package net.frozenblock.configurableeverything.item.mixin;

import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.frozenblock.configurableeverything.item.util.ItemConfigUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ReachEntityAttributes.class)
public class ReachEntityAttributesMixin {

	@Inject(method = "getReachDistance", at = @At("HEAD"), cancellable = true)
	private static void getReachDistance(LivingEntity entity, double baseAttackRange, CallbackInfoReturnable<Double> cir){
		if (entity instanceof Player player) {
			Double override = ItemConfigUtil.getReach(player);
			if (override != null) cir.setReturnValue(override);
		}
	}

	@Inject(method = "getAttackRange", at = @At("HEAD"), cancellable = true)
	private static void getAttackRange(LivingEntity entity, double baseAttackRange, CallbackInfoReturnable<Double> cir){
		if (entity instanceof Player player) {
			Double override = ItemConfigUtil.getReach(player);
			if (override != null) cir.setReturnValue(override);
		}
	}
}
