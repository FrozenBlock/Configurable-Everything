package net.frozenblock.configurableeverything.entity.zombie.mixin;

import net.frozenblock.configurableeverything.entity.zombie.ai.NewZombieBreakDoorGoal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.BreakDoorGoal;
import net.minecraft.world.entity.ai.goal.DoorInteractGoal;
import net.minecraft.world.entity.monster.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BreakDoorGoal.class)
public abstract class DoorBreakConfigGoalMixin extends DoorInteractGoal {

	public DoorBreakConfigGoalMixin(Mob mob) {
		super(mob);
	}

	@Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
	public void canUse(CallbackInfoReturnable<Boolean> info) {
		if (this.mob instanceof Zombie && !(BreakDoorGoal.class.cast(this) instanceof NewZombieBreakDoorGoal)) {
			info.setReturnValue(false);
		}
	}

}
