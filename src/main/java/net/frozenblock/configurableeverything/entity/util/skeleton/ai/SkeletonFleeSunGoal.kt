package net.frozenblock.configurableeverything.entity.util.skeleton.ai

import net.frozenblock.configurableeverything.config.EntityConfig
import net.frozenblock.configurableeverything.config.MainConfig
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.goal.FleeSunGoal

class SkeletonFleeSunGoal(mob: PathfinderMob, speedModifier: Double) : FleeSunGoal(mob, speedModifier) {
    override fun canUse(): Boolean {
        val config = EntityConfig.get()

        if (!MainConfig.get().entity)
            return super.canUse()

        return config.skeleton.skeletonsAvoidSun && super.canUse()
    }
}
