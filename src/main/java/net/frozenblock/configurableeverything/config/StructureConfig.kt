package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.util.CONFIG_FORMAT
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.xjs.XjsConfig
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
    var removedStructures: TypedEntry<List<ResourceLocation>> = TypedEntry.create(
        RESOURCE_LIST,
        listOf(
            ResourceLocation("ancient_city"),
            ResourceLocation("village_plains")
        )
    ),

    @JvmField
    @EntrySyncData("removedStructureSets")
    var removedStructureSets: TypedEntry<List<ResourceLocation>> = TypedEntry.create(
        RESOURCE_LIST,
        listOf(
            ResourceLocation("villages")
        )
    )
) {
	companion object : XjsConfig<StructureConfig>(
        MOD_ID,
        StructureConfig::class.java,
        makeConfigPath("structure"),
        CONFIG_FORMAT
    ) {

		init {
            ConfigRegistry.register(this)
        }

		@JvmStatic
        @JvmOverloads
		fun get(real: Boolean = false): StructureConfig = if (real) this.instance() else this.config()
	}
}
