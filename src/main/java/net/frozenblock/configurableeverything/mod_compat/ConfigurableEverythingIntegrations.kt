package net.frozenblock.configurableeverything.mod_compat

import net.frozenblock.configurableeverything.mod_compat.FrozenLibIntegration
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.lib.integration.api.ModIntegration
import net.frozenblock.lib.integration.api.ModIntegrationSupplier
import net.frozenblock.lib.integration.api.ModIntegrations
import java.util.function.Supplier

internal object ConfigurableEverythingIntegrations {

    @JvmField
    internal val FROZENLIB_INTEGRATION: ModIntegration = registerAndGet({ FrozenLibIntegration }, "frozenlib")

    inline fun register(
        crossinline integration: () -> ModIntegration?,
        modID: String?
    ): ModIntegrationSupplier<out ModIntegration> {
        return ModIntegrations.register(Supplier { integration() }, MOD_ID, modID)
    }

    inline fun <T : ModIntegration?> register(
        crossinline integration: () -> T?,
        crossinline unloadedIntegration: () -> T?,
        modID: String?
    ): ModIntegrationSupplier<T> {
        return ModIntegrations.register(Supplier { integration() }, Supplier { unloadedIntegration() }, MOD_ID, modID)
    }

    fun <T : ModIntegration?> registerAndGet(integration: () -> T?, modID: String?): ModIntegration {
        return ModIntegrations.register(Supplier { integration() }, MOD_ID, modID).integration
    }
}
