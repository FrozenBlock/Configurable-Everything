package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.util.*
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.xjs.XjsConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.minecraft.resources.Identifier

data class StructureConfig(

    @JvmField
    var removedStructures: TypedEntry<MutableList<Identifier>> = TypedEntry.create(
        RESOURCE_LIST,
        mutableListOf(
            Identifier.withDefaultNamespace("ancient_city"),
            Identifier.withDefaultNamespace("village_plains")
        )
    ),

    @JvmField
    var removedStructureSets: TypedEntry<MutableList<Identifier>> = TypedEntry.create(
        RESOURCE_LIST,
        mutableListOf(
            Identifier.withDefaultNamespace("villages")
        )
    )
) {
	companion object : CESimpleConfig<StructureConfig>(
        StructureConfig::class,
        "structure"
    ) {

		init {
            ConfigRegistry.register(this)
        }

		@JvmStatic
        @JvmOverloads
		fun get(real: Boolean = false): StructureConfig = if (real) this.instance() else this.config()
	}
}
