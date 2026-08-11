package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.util.*
import net.frozenblock.lib.config.v2.entry.ConfigEntry
import net.frozenblock.lib.config.v2.entry.EntryType
import net.minecraft.resources.Identifier

object StructureConfig : CEConfig("structure") {

    @JvmField
    var removedStructures: ConfigEntry<MutableList<Identifier>> = this.entry("removedStructures",
        EntryType.IDENTIFIER.asList(),
        mutableListOf(
            Identifier.withDefaultNamespace("ancient_city"),
            Identifier.withDefaultNamespace("village_plains")
        )
    )

    @JvmField
    var removedStructureSets: ConfigEntry<MutableList<Identifier>> = this.entry("removedStructureSets",
        EntryType.IDENTIFIER.asList(),
        mutableListOf(
            Identifier.withDefaultNamespace("villages")
        )
    )
}
