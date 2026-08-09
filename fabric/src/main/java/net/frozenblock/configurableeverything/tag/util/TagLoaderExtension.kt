package net.frozenblock.configurableeverything.tag.util

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey

internal interface TagLoaderExtension<T : Any> {

    fun `configurableEverything$setRegistryKey`(registry: ResourceKey<out Registry<T>>)
}
