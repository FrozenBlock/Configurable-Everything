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
		var entity = MainConfig.get().entity
		var skeleton = EntityConfig.get().skeleton
		// null safety in java moment
		if (entity != true || skeleton == null || skeleton.skeletonsAvoidSun != true) {
			return false;
		}
		return super.canUse();
	}
}
