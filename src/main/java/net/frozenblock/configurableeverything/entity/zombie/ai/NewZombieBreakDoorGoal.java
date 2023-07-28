package net.frozenblock.configurableeverything.entity.zombie.ai;

import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.BreakDoorGoal;
import net.minecraft.world.entity.monster.Zombie;
import java.util.function.Predicate;

public class NewZombieBreakDoorGoal extends BreakDoorGoal {

	public NewZombieBreakDoorGoal(Mob mob, Predicate<Difficulty> predicate) {
		super(mob, predicate);
	}

	public NewZombieBreakDoorGoal(Mob mob, int i, Predicate<Difficulty> predicate) {
		super(mob, i, predicate);
	}

	@Override
	public boolean canUse() {
		if (this.mob instanceof Zombie zombie) {
			if (!zombie.canBreakDoors() || !zombie.supportsBreakDoorGoal()) {
				return false;
			}
		}
		return super.canUse();
	}

}
