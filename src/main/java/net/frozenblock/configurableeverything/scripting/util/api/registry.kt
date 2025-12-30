package net.frozenblock.configurableeverything.scripting.util.api

import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour

fun <T : Block> registerBlock(id: String, block: (BlockBehaviour.Properties) -> T, properties: BlockBehaviour.Properties): T {
    val id = ResourceLocation.parse(id)
    return Registry.register(
        BuiltInRegistries.BLOCK,
        id,
        block(properties.setId(ResourceKey.create(Registries.BLOCK, id)))
    )
}

val MinecraftServer.registryAccess: RegistryAccess
    get() = this.registryAccess()
