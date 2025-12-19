@file:Environment(EnvType.CLIENT)

package net.frozenblock.configurableeverything.config.gui

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment

// TODO: Re-enable when cloth config is unobfuscated
/*import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.clothconfig2.api.Requirement
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.ItemConfig
import net.frozenblock.configurableeverything.item.util.ItemReachOverride
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.text
import net.frozenblock.configurableeverything.util.tooltip
import net.frozenblock.configurableeverything.util.vanillaId
import net.frozenblock.lib.config.api.client.gui.EntryBuilder
import net.frozenblock.lib.config.api.client.gui.multiElementEntry
import net.frozenblock.lib.config.api.client.gui.typedEntryList
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.Items

private val configInstance = ItemConfig

private inline val mainToggleReq: Requirement
    get() = Requirement.isTrue(MainConfigGui.INSTANCE!!.item)

object ItemConfigGui {
    fun setupEntries(category: ConfigCategory, entryBuilder: ConfigEntryBuilder) {
        val config = configInstance.instance()
        val defaultConfig = configInstance.defaultInstance()

        category.addEntry(reachOverrides(entryBuilder, config, defaultConfig))
    }
}

private fun reachOverrides(
    entryBuilder: ConfigEntryBuilder,
    config: ItemConfig,
    defaultConfig: ItemConfig
): AbstractConfigListEntry<*> {
    return typedEntryList(
        entryBuilder,
        text("reach_overrides"),
        config::reachOverrides,
        { defaultConfig.reachOverrides },
        false,
        tooltip("reach_overrides"),
        { newValue -> config.reachOverrides = newValue },
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

                EntryBuilder(text("reach_overrides.item"), override.item.toString(),
                    vanillaId("").toString(),
                    { newValue -> override.item = Identifier.parse(newValue) },
                    tooltip("reach_overrides.item")
                ).build(entryBuilder),

                EntryBuilder(text("reach_overrides.reach"), override.reach,
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
*/
