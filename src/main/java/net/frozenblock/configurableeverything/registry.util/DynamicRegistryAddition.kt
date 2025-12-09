package net.frozenblock.configurableeverything.registry.util

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.Identifier
import org.quiltmc.qsl.frozenblock.core.registry.api.event.DynamicRegistryManagerSetupContext

open class DynamicRegistryAddition<T : Any>(
    @JvmField val registry: ResourceKey<out Registry<T>>,
    open val key: Identifier,
    open val value: T
) {

    fun register(setupContext: DynamicRegistryManagerSetupContext) {
        setupContext.register(registry, key) { value }
    }
}
