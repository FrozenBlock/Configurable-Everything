package net.frozenblock.configurableeverything.entity.util.zombie.ai;

import net.frozenblock.configurableeverything.config.EntityConfig;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;

public class ZombieRestrictSunGoal extends RestrictSunGoal {

	public ZombieRestrictSunGoal(PathfinderMob mob) {
		super(mob);
	}

	@Override
	public boolean canUse() {
		if (!(MainConfig.get().entity && EntityConfig.get().zombie.zombiesAvoidSun)) {
			return false;
		}
		return super.canUse();
	}
}
