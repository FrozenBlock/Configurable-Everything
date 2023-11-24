package net.frozenblock.configurableeverything.mod_compat

import net.frozenblock.configurableeverything.mod_compat.FrozenLibIntegration
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.lib.integration.api.ModIntegration
import net.frozenblock.lib.integration.api.ModIntegrationSupplier
import net.frozenblock.lib.integration.api.ModIntegrations
import java.util.function.Supplier

object ConfigurableEverythingIntegrations {

    val FROZENLIB_INTEGRATION: ModIntegration = registerAndGet({ FrozenLibIntegration }, "frozenlib")

    fun register(
        integration: Supplier<out ModIntegration?>?,
        modID: String?
    ): ModIntegrationSupplier<out ModIntegration> {
        return ModIntegrations.register(integration, MOD_ID, modID)
    }

    fun <T : ModIntegration?> register(
        integration: Supplier<T>?,
        unloadedIntegration: Supplier<T>?,
        modID: String?
    ): ModIntegrationSupplier<T> {
        return ModIntegrations.register(integration, unloadedIntegration, MOD_ID, modID)
    }

    fun <T : ModIntegration?> registerAndGet(integration: Supplier<T>?, modID: String?): ModIntegration {
        return ModIntegrations.register(integration, MOD_ID, modID).integration
    }
}
