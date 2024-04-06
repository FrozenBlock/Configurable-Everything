package net.frozenblock.configurableeverything.entity.util.zombie.ai

import java.util.function.Predicate
import net.minecraft.world.Difficulty
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.goal.BreakDoorGoal
import net.minecraft.world.entity.monster.Zombie

open class NewZombieBreakDoorGoal : BreakDoorGoal {

    constructor(mob: Mob, i: Int, predicate: Predicate<Difficulty>) : super(mob, i, predicate)

	constructor(mob: Mob, predicate: Predicate<Difficulty>) : super(mob, predicate)

	override fun canUse(): Boolean {
		val mob = this.mob
		if (mob is Zombie) {
			if (!mob.canBreakDoors() || !mob.supportsBreakDoorGoal()) {
				return false
			}
		}
		return super.canUse()
	}

}
