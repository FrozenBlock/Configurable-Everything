@file:ClientOnly

package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.util.CEConfig
import net.frozenblock.lib.config.v2.entry.ConfigEntry
import net.frozenblock.lib.config.v2.entry.EntryType
import net.mehvahdjukaar.candlelight.api.ClientOnly
import net.minecraft.resources.Identifier

// UNSYNCABLE
object RecipeConfig : CEConfig("recipe") {

    @JvmField
    // UNSYNCABLE
    var removedRecipes: ConfigEntry<MutableList<Identifier>> = this.unsyncableEntry("removedRecipes",
        EntryType.IDENTIFIER.asList(),
        mutableListOf(
            Identifier.parse("acacia_boat")
        ),
    )
}
