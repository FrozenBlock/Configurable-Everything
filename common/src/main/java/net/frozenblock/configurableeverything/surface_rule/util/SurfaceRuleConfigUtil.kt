package net.frozenblock.configurableeverything.surface_rule.util

import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.config.SurfaceRuleConfig
import net.frozenblock.configurableeverything.util.value
import net.frozenblock.lib.levelgen.surface.api.SurfaceRuleEvents

internal object SurfaceRuleConfigUtil {

    internal fun init() {
        if (MainConfig.surface_rule.get()) {
            SurfaceRuleEvents.MODIFY_GENERIC.register { biomes, sourceHolders ->
                sourceHolders.addAll(SurfaceRuleConfig.addedSurfaceRules.get())
            }
        }
    }
}
