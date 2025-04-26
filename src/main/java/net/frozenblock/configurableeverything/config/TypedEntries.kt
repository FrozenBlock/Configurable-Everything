package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.mutListOf
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.minecraft.resources.ResourceLocation

internal val RESOURCE_LIST: TypedEntryType<MutableList<ResourceLocation>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        ResourceLocation.CODEC.mutListOf()
    )
)
