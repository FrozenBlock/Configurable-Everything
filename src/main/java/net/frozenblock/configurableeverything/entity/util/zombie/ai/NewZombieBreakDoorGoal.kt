package net.frozenblock.configurableeverything.entity.util.zombie.ai

import java.util.function.Predicate
import net.minecraft.world.Difficulty
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.goal.BreakDoorGoal
import net.minecraft.world.entity.monster.Zombie

open class NewZombieBreakDoorGoal(mob: Mob, i: Int, predicate: Predicate<Difficulty>) : BreakDoorGoal(mob, i, predicate) {

	constructor NewZombieBreakDoorGoal(Mob mob, Predicate<Difficulty> predicate) : super(mob, predicate)

	override fun canUse(): Boolean {
		val mob = this.mob
		if (this.mob is Zombie) {
			if (!mob.canBreakDoors() || !mob.supportsBreakDoorGoal()) {
				return false
			}
		}
		return super.canUse()
	}

}
