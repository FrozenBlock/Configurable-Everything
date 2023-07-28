package net.frozenblock.configurableeverything.entity.zombie.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.frozenblock.configurableeverything.config.EntityConfig;
import net.frozenblock.configurableeverything.entity.zombie.ai.NewZombieBreakDoorGoal;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.monster.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Zombie.class)
public class AllZombiesBreakDoorsMixin {

	@Shadow private boolean canBreakDoors;

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Zombie;isUnderWaterConverting()Z", shift = At.Shift.BEFORE))
	public void tick(CallbackInfo callbackInfo) {
		Zombie zombie = Zombie.class.cast(this);
		if (!EntityConfig.get().zombie.allZombiesBreakDoors) {
			if (GoalUtils.hasGroundPathNavigation(zombie)) {
				((GroundPathNavigation)zombie.getNavigation()).setCanOpenDoors(this.canBreakDoors);
			}
		} else if (GoalUtils.hasGroundPathNavigation(zombie)) {
			((GroundPathNavigation)zombie.getNavigation()).setCanOpenDoors(this.supportsBreakDoorGoal());
		}
	}

	@Inject(method = "canBreakDoors", at = @At("HEAD"), cancellable = true)
	public void mcFixes$canBreakDoors(CallbackInfoReturnable<Boolean> info) {
		if (EntityConfig.get().zombie.allZombiesBreakDoors) {
			info.setReturnValue(true);
		}
	}

	@Inject(method = "addBehaviourGoals", at = @At("TAIL"))
	public void mcFixes$addBehaviourGoals(CallbackInfo info) {
		Mob.class.cast(this).goalSelector.addGoal(1, new NewZombieBreakDoorGoal(Mob.class.cast(this), difficulty -> EntityConfig.get().zombie.allZombiesBreakDoors || difficulty == Difficulty.HARD));
	}

	@ModifyExpressionValue(method = "addAdditionalSaveData", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Zombie;canBreakDoors()Z"))
	public boolean mcFixes$addAdditionalSaveData(boolean original) {
		return this.canBreakDoors;
	}

	@Shadow
	protected boolean supportsBreakDoorGoal() {
		throw new AssertionError("Mixin injection failed - AllZombiesBreakDoorsMixin.");
	}

}
