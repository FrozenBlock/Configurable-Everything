package net.frozenblock.configurableeverything.entity.util.zombie.ai;

import net.frozenblock.configurableeverything.config.EntityConfig;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;

public class ZombieFleeSunGoal extends FleeSunGoal {

	public ZombieFleeSunGoal(PathfinderMob mob, double speedModifier) {
		super(mob, speedModifier);
	}

	@Override
	public boolean canUse() {
		if (!(MainConfig.get().entity && EntityConfig.get().zombie.zombiesAvoidSun)) {
			return false;
		}
		return super.canUse();
	}
}

