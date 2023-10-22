package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.minecraft.resources.ResourceLocation

data class StructureConfig(
    var removedStructures: TypedEntry<List<ResourceLocation?>>? = TypedEntry(
        RESOURCE_LIST,
        listOf(
            ResourceLocation("ancient_city"),
            ResourceLocation("village_plains")
        )
    ),

    var removedStructureSets: TypedEntry<List<ResourceLocation?>>? = TypedEntry(
        RESOURCE_LIST,
        listOf(
            ResourceLocation("villages")
        )
    )
) {
	companion object {
        private val RESOURCE_LIST: TypedEntryType<List<ResourceLocation?>> = ConfigRegistry.register(
            TypedEntryType(
                MOD_ID,
                ResourceLocation.CODEC.listOf()
            )
        )

		@JvmField
		val INSTANCE: Config<StructureConfig> = ConfigRegistry.register(
			JsonConfig(
				MOD_ID,
				StructureConfig::class.java,
				makeConfigPath("structure"),
				CONFIG_JSONTYPE
			)
		)

		@JvmStatic
		fun get(real: Boolean = false): StructureConfig = if (real) INSTANCE.instance() else INSTANCE.config()
	}
}
