package net.frozenblock.configurableeverything.entity.util.skeleton.ai

import net.frozenblock.configurableeverything.config.EntityConfig
import net.frozenblock.configurableeverything.config.MainConfig
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.goal.RestrictSunGoal

class SkeletonRestrictSunGoal(mob: PathfinderMob) : RestrictSunGoal(mob) {
    override fun canUse(): Boolean {
        if (!MainConfig.entity.get())
            return super.canUse()

        return EntityConfig.skeletonsAvoidSun.get() && super.canUse()
    }
}
