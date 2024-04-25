package net.frozenblock.configurableeverything.tag.util

import net.minecraft.core.Registry

internal interface TagLoaderExtension<T> {

    fun `configurableEverything$setRegistry`(registry: Registry<T>)
}
