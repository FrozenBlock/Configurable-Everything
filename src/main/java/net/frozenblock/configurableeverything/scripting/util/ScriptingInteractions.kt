package net.frozenblock.configurableeverything.scripting.util

import net.minecraft.core.Registry as VanillaRegistry
import net.minecraft.core.registries.BuiltInRegistries as VanillaBuiltInRegistries
import net.minecraft.core.registries.Registries as VanillaRegistries
import net.minecraft.resources.ResourceLocation as VanillaResourceLocation
import net.minecraft.world.level.block.Block as VanillaBlock
import net.minecraft.world.level.block.state.BlockBehaviour.Properties as VanillaProperties

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

    override fun value(): VanillaRegistry<T> {
        return registry
    }
}

data class ResourceLocation(val namespace: String = "minecraft", val path: String) : FakeObject<VanillaResourceLocation> {
    override fun value(): VanillaResourceLocation = VanillaResourceLocation(namespace, path)
}

data class Block(val block: VanillaBlock) : FakeObject<VanillaBlock> {
    constructor(properties: Properties) {
        this(VanillaBlock(properties))
    }

    override fun value(): VanillaBlock = block
}

data class Properties(val properties: VanillaProperties) : FakeObject<VanillaProperties> {
    companion object {
        fun of(): Properties = Properties(VanillaProperties.of())
    }

    override fun value(): VanillaProperties = properties
}

fun interface FakeObject<T> {
    fun value(): T
}