package net.frozenblock.configurableeverything.entity.mixin.skeleton;

import net.frozenblock.configurableeverything.entity.util.skeleton.ai.SkeletonFleeSunGoal;
import net.frozenblock.configurableeverything.entity.util.skeleton.ai.SkeletonRestrictSunGoal;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(AbstractSkeleton.class)
public class SkeletonsAvoidSunMixin {

	@ModifyArgs(method = "registerGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V", ordinal = 0))
	public void configurableEverything$newRestrictSunGoal(Args args) {
		args.set(1, new SkeletonRestrictSunGoal(AbstractSkeleton.class.cast(this)));
	}

	@ModifyArgs(method = "registerGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V", ordinal = 1))
	public void configurableEverything$newFleeSunGoal(Args args) {
		args.set(1, new SkeletonFleeSunGoal(AbstractSkeleton.class.cast(this), 1.0));
	}

}
