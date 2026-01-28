@file:Environment(EnvType.CLIENT)

package net.frozenblock.configurableeverything.config

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.util.CESimpleConfig
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.SyncBehavior
import net.minecraft.resources.Identifier

// UNSYNCABLE
data class RecipeConfig(

    @JvmField
    // UNSYNCABLE
    var removedRecipes: TypedEntry<MutableList<Identifier>> = TypedEntry.create(
        RESOURCE_LIST,
        mutableListOf(
            Identifier.parse("acacia_boat")
        ),
    )
) {
    companion object : CESimpleConfig<RecipeConfig>(
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
