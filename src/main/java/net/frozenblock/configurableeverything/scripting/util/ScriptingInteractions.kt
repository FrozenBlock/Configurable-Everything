package net.frozenblock.configurableeverything.scripting.util

import net.minecraft.core.Registry as VanillaRegistry
import net.minecraft.core.registries.BuiltInRegistries as VanillaBuiltInRegistries
import net.minecraft.core.registries.Registries as VanillaRegistries
import net.minecraft.resources.ResourceKey as VanillaResourceKey
import net.minecraft.resources.ResourceLocation as VanillaResourceLocation

object Registries {
    val BLOCK = VanillaRegistries.BLOCK
    val ENTITY_TYPE = VanillaRegistries.ENTITY_TYPE
    val ITEM = VanillaRegistries.ITEM
}

object BuiltInRegistries {
    val BLOCK = Registry(VanillaBuiltInRegistries.BLOCK)
    val ENTITY_TYPE = Registry(VanillaBuiltInRegistries.ENTITY_TYPE)
    val ITEM = Registry(VanillaBuiltInRegistries.ITEM)
}

data class Registry<T : Any>(
    val registry: VanillaRegistry<T>
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

data class ResourceKey<T : Any>(val registryKey: VanillaResourceKey<out Registry<T>>, val location: ResourceLocation) : FakeObject<VanillaResourceKey<T>> {
    override fun value(): VanillaResourceKey<T> {
        return VanillaResourceKey.create(registryKey, location.value())
    }
}

data class ResourceLocation(val namespace: String = "minecraft", val path: String) : FakeObject<VanillaResourceLocation> {
    override fun value(): VanillaResourceLocation {
        return VanillaResourceLocation(namespace, path)
    }
}

fun interface FakeObject<T> {
    fun value(): T
}