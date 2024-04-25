package net.frozenblock.configurableeverything.config

import com.mojang.serialization.Codec
import net.frozenblock.configurableeverything.tag.util.RegistryTagModification
import net.frozenblock.configurableeverything.tag.util.TagModification
import net.frozenblock.configurableeverything.util.CONFIG_FORMAT
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.xjs.XjsConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.annotation.EntrySyncData
import net.frozenblock.lib.config.api.sync.annotation.UnsyncableConfig
import net.minecraft.core.registries.BuiltInRegistries

private val TAG_MODIFICATIONS: TypedEntryType<List<RegistryTagModification>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        Codec.list(RegistryTagModification.CODEC)
    )
)

@UnsyncableConfig
data class TagConfig(
    @JvmField
    @EntrySyncData("lootModifications")
    var tagModifications: TypedEntry<List<RegistryTagModification>> = TypedEntry(
        TAG_MODIFICATIONS,
        listOf(
            RegistryTagModification(
                BuiltInRegistries.BLOCK.key().location().toString(),
                listOf(
                    TagModification(
                        "minecraft:piglin_loved",
                        listOf(
                            "diamond_block"
                        ),
                        listOf(
                            "gold_ingot"
                        )
                    )
                )
            )
        )
    )
) {
    companion object : XjsConfig<TagConfig>(
        MOD_ID,
        TagConfig::class.java,
        makeLegacyConfigPath("tag"),
        CONFIG_FORMAT
    ) {

        init {
            ConfigRegistry.register(this)
        }

        @JvmStatic
        @JvmOverloads
        fun get(real: Boolean = false): TagConfig = if (real) this.instance() else this.config()
    }
}
