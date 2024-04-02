package net.frozenblock.configurableeverything.entity.util.skeleton.ai

import net.frozenblock.configurableeverything.config.EntityConfig
import net.frozenblock.configurableeverything.config.MainConfig
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.goal.RestrictSunGoal

class SkeletonRestrictSunGoal(mob: PathfinderMob) : RestrictSunGoal(mob) {
    override fun canUse(): Boolean {
        val config = EntityConfig.get()

        if (!MainConfig.get().entity)
            return super.canUse()

        return config.skeleton?.skeletonsAvoidSun == true && super.canUse()
    }
}
