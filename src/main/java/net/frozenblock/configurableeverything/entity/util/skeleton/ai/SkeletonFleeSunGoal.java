package net.frozenblock.configurableeverything.entity.util.skeleton.ai;

import net.frozenblock.configurableeverything.config.EntityConfig;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;

public class SkeletonFleeSunGoal extends FleeSunGoal {

	public SkeletonFleeSunGoal(PathfinderMob mob, double speedModifier) {
		super(mob, speedModifier);
	}

	@Override
	public boolean canUse() {
		if (!EntityConfig.get().skeleton.skeletonsAvoidSun && MainConfig.get().entity) {
			return false;
		}
		return super.canUse();
	}
}

