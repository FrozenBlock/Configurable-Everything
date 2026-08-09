package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.util.*
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.xjs.XjsConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
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
