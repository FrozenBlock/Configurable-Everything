package net.frozenblock.configurableeverything.config

import net.frozenblock.lib.shadow.blue.endless.jankson.Comment
import com.mojang.serialization.Codec
import net.frozenblock.configurableeverything.tag.util.RegistryTagModification
import net.frozenblock.configurableeverything.tag.util.TagModification
import net.frozenblock.configurableeverything.util.*
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.xjs.XjsConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.v2.entry.ConfigEntry
import net.frozenblock.lib.config.v2.entry.EntryType
import net.minecraft.core.registries.BuiltInRegistries

private val TAG_MODIFICATIONS: EntryType<RegistryTagModification> = EntryType.create(
    RegistryTagModification.CODEC,
    RegistryTagModification.STREAM_CODEC,
)

object TagConfig : CEConfig("tag") {
    @JvmField
    var ignoreInvalidEntries: ConfigEntry<Boolean> = this.entry("ignoreInvalidEntries",
        EntryType.BOOL,
        true,
        "Passes over invalid entries instead of failing to load the tag and any dependent tags"
    )

    @JvmField
    var tagModifications: ConfigEntry<MutableList<RegistryTagModification>> = this.entry("tagModifications",
        TAG_MODIFICATIONS.asList(),
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
}
