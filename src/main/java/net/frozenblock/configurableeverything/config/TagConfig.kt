package net.frozenblock.configurableeverything.config

import blue.endless.jankson.Comment
import com.mojang.serialization.Codec
import net.frozenblock.configurableeverything.tag.util.RegistryTagModification
import net.frozenblock.configurableeverything.tag.util.TagModification
import net.frozenblock.configurableeverything.util.*
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.xjs.XjsConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.annotation.EntrySyncData
import net.frozenblock.lib.config.api.sync.annotation.UnsyncableConfig
import net.minecraft.core.registries.BuiltInRegistries

private val TAG_MODIFICATIONS: TypedEntryType<MutableList<RegistryTagModification>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        RegistryTagModification.CODEC.mutListOf()
    )
)

data class TagConfig(
    @JvmField
    @EntrySyncData("ignoreInvalidEntries")
    @Comment("Passes over invalid entries instead of failing to load the tag and any dependent tags")
    var ignoreInvalidEntries: Boolean = true,

    @JvmField
    @EntrySyncData("tagModifications")
    var tagModifications: TypedEntry<MutableList<RegistryTagModification>> = TypedEntry.create(
        TAG_MODIFICATIONS,
        mutableListOf(
            RegistryTagModification(
                BuiltInRegistries.ITEM.key().identifier().toString(),
                mutableListOf(
                    TagModification(
                        "minecraft:piglin_loved",
                        mutableListOf(
                            "diamond_block"
                        ),
                        mutableListOf(
                            "gold_ingot"
                        )
                    )
                )
            )
        )
    )
) {
    companion object : CEConfig<TagConfig>(
        TagConfig::class,
        "tag"
    ) {

        init {
            ConfigRegistry.register(this)
        }

        @JvmStatic
        @JvmOverloads
        fun get(real: Boolean = false): TagConfig = if (real) this.instance() else this.config()
    }
}
