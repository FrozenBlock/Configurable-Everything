package net.frozenblock.configurableeverything.item.util

import net.frozenblock.configurableeverything.config.ItemConfig
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.value
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import kotlin.jvm.optionals.getOrNull

object ItemConfigUtil {

    @JvmStatic
    fun getReach(player: LivingEntity): Double? {
        val overrides = ItemConfig.get().reachOverrides?.value
        if (MainConfig.get().item && overrides != null) {
            val item = player.getItemInHand(InteractionHand.MAIN_HAND)
            for (reachOverride in overrides) {
                val itemOverrideLoc = reachOverride?.item ?: continue
                val reach = reachOverride.reach ?: continue

                val itemOverrideKey: ResourceKey<Item> = ResourceKey.create(Registries.ITEM, itemOverrideLoc)
                val itemOverride: Holder.Reference<Item> =
                    BuiltInRegistries.ITEM.getHolder(itemOverrideKey).getOrNull() ?: continue
                if (item.`is`(itemOverride)) return reach
            }
        }
        return null
    }
}
