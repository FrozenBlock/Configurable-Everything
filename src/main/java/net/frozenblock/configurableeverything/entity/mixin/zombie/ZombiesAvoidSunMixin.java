package net.frozenblock.configurableeverything.entity.mixin.zombie;

import net.frozenblock.configurableeverything.entity.util.zombie.ai.ZombieFleeSunGoal;
import net.frozenblock.configurableeverything.entity.util.zombie.ai.ZombieRestrictSunGoal;
import net.minecraft.world.entity.monster.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Zombie.class)
public class ZombiesAvoidSunMixin {

	@Inject(method = "addBehaviourGoals", at = @At("TAIL"))
	public void mcFixes$addBehaviourGoalsToAvoidSun(CallbackInfo info) {
		Zombie zombie = Zombie.class.cast(this);
		zombie.goalSelector.addGoal(2, new ZombieRestrictSunGoal(zombie));
		zombie.goalSelector.addGoal(3, new ZombieFleeSunGoal(zombie, 1.0));
	}

}
