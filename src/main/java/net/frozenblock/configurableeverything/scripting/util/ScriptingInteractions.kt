package net.frozenblock.configurableeverything.scripting.util

import net.minecraft.core.Registry as VanillaRegistry
import net.minecraft.core.registries.BuiltInRegistries as VanillaBuiltInRegistries
import net.minecraft.core.registries.Registries as VanillaRegistries
import net.minecraft.resources.ResourceKey as VanillaResourceKey
import net.minecraft.resources.ResourceLocation as VanillaResourceLocation

protected object Registries {
    val BLOCK = VanillaRegistries.BLOCK
    val ENTITY_TYPE = VanillaRegistries.ENTITY_TYPE
    val ITEM = VanillaRegistries.ITEM
}

protected object BuiltInRegistries {
    val BLOCK = Registry(VanillaBuiltInRegistries.BLOCK)
    val ENTITY_TYPE = Registry(VanillaBuiltInRegistries.ENTITY_TYPE)
    val ITEM = Registry(VanillaBuiltInRegistries.ITEM)
}

protected data class Registry<T>(
    val registry = VanillaRegistry<T>
) : FakeObject<VanillaRegistry<T>> {

    fun register(id: ResourceLocation, value: FakeObject<T>): FakeObject<T> {
        VanillaRegistry.register(registry, id.value(), value.value())
        return value
    }

    fun register(id: ResourceKey<T>, value: FakeObject<T>): FakeObject<T> {
        VanillaRegistry.register(registry, id.value(), value.value())
        return value
    }

    override fun value(): VanillaRegistry<T> {
        return registry
    }
}

protected data class ResourceKey<T>(val registry: ResourceKey<Registry<T>>?, id: ResourceLocation) : FakeObject<VanillaResourceKey<T>> {
    override fun value(): VanillaResourceKey<T> {
        return VanillaResourceKey.create(registry?.value(), id.value())
    }
}

protected data class ResourceLocation(val namespace: String = "minecraft", val path: String) : FakeObject<VanillaResourceLocation> {
    override fun value(): VanillaResourceLocation {
        return VanillaResourceLocation(namespace, path)
    }
}

protected abstract class FakeObject<T> {
    abstract fun value(): T
}