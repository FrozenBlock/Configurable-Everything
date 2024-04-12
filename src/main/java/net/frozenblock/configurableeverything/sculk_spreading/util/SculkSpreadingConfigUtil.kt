package net.frozenblock.configurableeverything.sculk_spreading.util

import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.config.SculkSpreadingConfig
import net.frozenblock.configurableeverything.util.value
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.state.BlockState

object SculkSpreadingConfigUtil {

    fun growthState(original: BlockState, random: RandomSource, isWorldGeneration: Boolean): BlockState {
        val config = SculkSpreadingConfig.get()
        if (!MainConfig.get().sculk_spreading) return original

        val list = config.growths.value
        for (growth in list) {
            if (growth.restrictedToWorldgen == true && !isWorldGeneration) continue
            val rarity = growth.rarity ?: continue
            val newState = growth.blockState ?: continue

            if (rarity == 0 || random.nextInt(rarity) == 0) {
                return newState
            }
        }

        return original
    }
}
