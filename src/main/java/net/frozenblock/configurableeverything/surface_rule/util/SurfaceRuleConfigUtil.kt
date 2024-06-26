package net.frozenblock.configurableeverything.surface_rule.util

import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.config.SurfaceRuleConfig
import net.frozenblock.configurableeverything.util.value
import net.frozenblock.lib.worldgen.surface.api.SurfaceRuleEvents

internal object SurfaceRuleConfigUtil {

    internal fun init() {
        val config = SurfaceRuleConfig.get();
        if (MainConfig.get().surface_rule) {
            SurfaceRuleEvents.MODIFY_GENERIC.register { context ->
                context.addAll(config.addedSurfaceRules.value)
            }
        }
    }
}
