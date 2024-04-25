package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeLegacyConfigPath
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.annotation.EntrySyncData
import net.frozenblock.lib.config.api.sync.annotation.UnsyncableConfig
import net.minecraft.resources.ResourceLocation

private val RESOURCE_LIST: TypedEntryType<List<ResourceLocation>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        ResourceLocation.CODEC.listOf()
    )
)

@UnsyncableConfig
data class StructureConfig(

    @JvmField
    @EntrySyncData("removedStructures")
    var removedStructures: TypedEntry<List<ResourceLocation>> = TypedEntry(
        RESOURCE_LIST,
        listOf(
            ResourceLocation("ancient_city"),
            ResourceLocation("village_plains")
        )
    ),

    @JvmField
    @EntrySyncData("removedStructureSets")
    var removedStructureSets: TypedEntry<List<ResourceLocation>> = TypedEntry(
        RESOURCE_LIST,
        listOf(
            ResourceLocation("villages")
        )
    )
) {
	companion object : JsonConfig<StructureConfig>(
        MOD_ID,
        StructureConfig::class.java,
        makeLegacyConfigPath("structure"),
        CONFIG_JSONTYPE,
        null,
        null
    ) {

		init {
            ConfigRegistry.register(this)
        }

		@JvmStatic
        @JvmOverloads
		fun get(real: Boolean = false): StructureConfig = if (real) this.instance() else this.config()
	}
}
