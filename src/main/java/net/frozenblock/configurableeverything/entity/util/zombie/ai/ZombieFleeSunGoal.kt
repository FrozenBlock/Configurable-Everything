package net.frozenblock.configurableeverything.entity.util.zombie.ai;

import net.frozenblock.configurableeverything.config.EntityConfig;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;

class ZombieFleeSunGoal(mob: PathfinderMob, speedModifier: Double) : FleeSunGoal(mob, speedModifier) {

	override fun canUse(): Boolean {
		return if (!(MainConfig.get().entity && EntityConfig.get().zombie.zombiesAvoidSun))
			false
		else super.canUse()
	}
}

