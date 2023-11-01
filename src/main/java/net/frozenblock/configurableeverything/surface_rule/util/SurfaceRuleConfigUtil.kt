package net.frozenblock.configurableeverything.surface_rule.util

import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.config.SurfaceRuleConfig
import net.frozenblock.lib.worldgen.surface.api.SurfaceRuleEvents

object SurfaceRuleConfigUtil {

    @JvmStatic
    fun init() {
        val config = SurfaceRuleConfig.get();
        if (MainConfig.get().surface_rule == true) {
            SurfaceRuleEvents.MODIFY_GENERIC.register { context ->
                run {
                    config.addedSurfaceRules?.value?.apply {
                        context.addAll(this)
                    }
                }
            }
        }
    }
}
