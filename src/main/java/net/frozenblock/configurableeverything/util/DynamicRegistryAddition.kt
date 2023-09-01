package net.frozenblock.configurableverything.util

import net.fabricmc.fabric.api.event.registry.DynamicRegistries
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey

data class DynamicRegistryAddition<T>(registry: ResourceKey<out Registry<T>>, key: ResourceKey<T>, value: T) {
    fun register() {
        TODO()
    }
}