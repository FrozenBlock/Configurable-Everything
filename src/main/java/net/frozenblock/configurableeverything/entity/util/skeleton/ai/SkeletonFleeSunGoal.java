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
		var entity = MainConfig.get(false).entity;
		var skeleton = EntityConfig.get(false).skeleton;
		// null safety in java moment
		if (entity != true || skeleton == null || skeleton.skeletonsAvoidSun != true) {
			return false;
		}
		return super.canUse();
	}
}

