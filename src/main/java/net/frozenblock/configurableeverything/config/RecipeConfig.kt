@file:Environment(EnvType.CLIENT)

package net.frozenblock.configurableeverything.config

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.util.CEConfig
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.SyncBehavior
import net.frozenblock.lib.config.api.sync.annotation.EntrySyncData
import net.frozenblock.lib.config.api.sync.annotation.UnsyncableConfig
import net.minecraft.resources.Identifier

@UnsyncableConfig
data class RecipeConfig(

    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    var removedRecipes: TypedEntry<MutableList<Identifier>> = TypedEntry.create(
        RESOURCE_LIST,
        mutableListOf(
            Identifier.parse("acacia_boat")
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
