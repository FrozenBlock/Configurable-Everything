package net.frozenblock.configurableeverything.entity.util.skeleton.ai;

import net.frozenblock.configurableeverything.config.EntityConfig;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;

public class SkeletonRestrictSunGoal extends RestrictSunGoal {

	public SkeletonRestrictSunGoal(PathfinderMob mob) {
		super(mob);
	}

	@Override
	public boolean canUse() {
		if (!EntityConfig.get().skeleton.skeletonsAvoidSun && MainConfig.get().entity) {
			return false;
		}
		return super.canUse();
	}
}
