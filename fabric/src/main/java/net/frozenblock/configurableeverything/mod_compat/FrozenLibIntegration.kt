package net.frozenblock.configurableeverything.mod_compat

import net.frozenblock.configurableeverything.util.UNSTABLE_LOGGING
import net.frozenblock.configurableeverything.util.log
import net.frozenblock.lib.integration.api.ModIntegration

internal object FrozenLibIntegration : ModIntegration("frozenlib") {
    override fun init() {
        log("FrozenLib integration ran!", UNSTABLE_LOGGING)
    }
}
