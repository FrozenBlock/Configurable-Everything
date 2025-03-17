@file:Environment(EnvType.CLIENT)

package net.frozenblock.configurableeverything.config

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.util.CEConfig
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.mutListOf
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.SyncBehavior
import net.frozenblock.lib.config.api.sync.annotation.EntrySyncData
import net.frozenblock.lib.config.api.sync.annotation.UnsyncableConfig
import net.minecraft.resources.ResourceLocation

private val RESOURCE_LIST: TypedEntryType<MutableList<ResourceLocation>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        ResourceLocation.CODEC.mutListOf()
    )
)

@UnsyncableConfig
data class RecipeConfig(

    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    var removedRecipes: TypedEntry<MutableList<ResourceLocation>> = TypedEntry.create(
        RESOURCE_LIST,
        mutableListOf(
            ResourceLocation.parse("acacia_boat")
        ),
    )
) {
    companion object : CEConfig<RecipeConfig>(
        RecipeConfig::class,
        "recipe",
    ) {

        init {
            ConfigRegistry.register(this)
        }

        @JvmStatic
        @JvmOverloads
        fun get(real: Boolean = false): RecipeConfig = if (real) this.instance() else this.config()
    }
}
