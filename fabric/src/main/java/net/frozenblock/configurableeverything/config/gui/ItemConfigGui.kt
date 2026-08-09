@file:Environment(EnvType.CLIENT)

package net.frozenblock.configurableeverything.config.gui

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.clothconfig2.api.Requirement
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.ItemConfig
import net.frozenblock.configurableeverything.item.util.ItemReachOverride
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.configurableeverything.util.tooltip
import net.frozenblock.configurableeverything.util.vanillaId
import net.frozenblock.lib.config.api.client.gui.EntryBuilder
import net.frozenblock.lib.config.api.client.gui.SimpleEntryBuilder
import net.frozenblock.lib.config.api.client.gui.configEntryList
import net.frozenblock.lib.config.api.client.gui.multiElementEntry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Items

private inline val mainToggleReq: Requirement
    get() = Requirement.isTrue(MainConfigGui.INSTANCE!!.item)

object ItemConfigGui {
    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        category.addEntry(reachOverrides(entryBuilder))
    }
}

private fun reachOverrides(
    entryBuilder: ConfigEntryBuilder,
): AbstractConfigListEntry<*> {
    return configEntryList(
        entryBuilder,
        text("reach_overrides"),
        ItemConfig.reachOverrides,
        false,
        tooltip("reach_overrides"),
        { element: ItemReachOverride?, _ ->
            val defaultOverride = ItemReachOverride(
                BuiltInRegistries.ITEM.getKey(Items.TRIDENT),
                100.0,
            )
            val override: ItemReachOverride = element ?: defaultOverride
            multiElementEntry(
                text("reach_overrides.reach_override"),
                override,
                true,

                SimpleEntryBuilder(text("reach_overrides.item"), override.item.toString(),
                    vanillaId("").toString(),
                    { newValue -> override.item = Identifier.parse(newValue) },
                    tooltip("reach_overrides.item")
                ).build(entryBuilder),

                SimpleEntryBuilder(text("reach_overrides.reach"), override.reach,
                    3.0,
                    { newValue -> override.reach = newValue },
                    tooltip("reach_overrides.reach")
                ).build(entryBuilder)
            )
        }
    ).apply {
        this.requirement = mainToggleReq
    }
}
