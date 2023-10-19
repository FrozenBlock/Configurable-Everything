package net.frozenblock.configurableeverything.registry.util

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import org.quiltmc.qsl.frozenblock.core.registry.api.event.DynamicRegistryManagerSetupContext

open class DynamicRegistryAddition<T>(
    @JvmField val registry: ResourceKey<out Registry<T>>,
    open val key: ResourceLocation,
    open val value: T & Any
) {

    fun register(setupContext: DynamicRegistryManagerSetupContext) {
        setupContext.register(registry, key) { value }
    }

    override fun toString(): String = "DynamicRegistryAddition[registry=$registry, key=$key, value=$value]"
}
