package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.experimental
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.minecraft.resources.ResourceLocation

private val RESOURCE_LIST: TypedEntryType<List<ResourceLocation?>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        ResourceLocation.CODEC.listOf()
    )
)

data class StructureConfig(

    @JvmField
    var removedStructures: TypedEntry<List<ResourceLocation?>>? = TypedEntry(
        RESOURCE_LIST,
        listOf(
            ResourceLocation("ancient_city"),
            ResourceLocation("village_plains")
        )
    ),

    @JvmField
    var removedStructureSets: TypedEntry<List<ResourceLocation?>>? = TypedEntry(
        RESOURCE_LIST,
        listOf(
            ResourceLocation("villages")
        )
    )
) {
	companion object : JsonConfig<StructureConfig>(
        MOD_ID,
        StructureConfig::class.java,
        makeConfigPath("structure"),
        CONFIG_JSONTYPE
    ) {

		init {
            experimental {
                ConfigRegistry.register(this)
            }
        }

		@JvmStatic
        @JvmOverloads
		fun get(real: Boolean = false): StructureConfig = if (real) this.instance() else this.config()
	}
}
