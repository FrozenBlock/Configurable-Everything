package net.frozenblock.configurableeverything.scripting.util.api

import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour

fun <T : Block> registerBlock(id: String, block: (BlockBehaviour.Properties) -> T, properties: BlockBehaviour.Properties): T {
    val id = Identifier.parse(id)
    return Registry.register(
        BuiltInRegistries.BLOCK,
        id,
        block(properties.setId(ResourceKey.create(Registries.BLOCK, id)))
    )
}
