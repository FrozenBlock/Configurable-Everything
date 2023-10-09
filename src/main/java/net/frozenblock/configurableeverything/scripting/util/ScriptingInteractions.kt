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

data class ResourceLocation(val value: String) : FakeObject<VanillaResourceLocation> {
    constructor(namespace: String, path: String) : this("$namespace:$path")

    override fun value(): VanillaResourceLocation = VanillaResourceLocation(value)
}

data class Block(val block: VanillaBlock) : FakeObject<VanillaBlock> {
    constructor(properties: BlockProperties) : this(VanillaBlock(properties.value()))

    override fun value(): VanillaBlock = block
}

fun blankBlockProperties(): BlockProperties = BlockProperties(VanillaProperties.of())

data class BlockProperties(val properties: VanillaProperties) : FakeObject<VanillaProperties> {
    companion object {
        fun of(): BlockProperties = BlockProperties(VanillaProperties.of())
    }

    override fun value(): VanillaProperties = properties
}

fun interface FakeObject<T> {
    fun value(): T
}
