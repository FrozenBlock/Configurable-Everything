package net.frozenblock.configurableeverything.sculk_spreading.util

import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.config.SculkSpreadingConfig
import net.frozenblock.configurableeverything.util.value
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.state.BlockState

object SculkSpreadingConfigUtil {

    fun growthState(original: BlockState, random: RandomSource, isWorldGeneration: Boolean): BlockState {
        if (!MainConfig.sculk_spreading.get()) return original

        val list = SculkSpreadingConfig.growths.get()
        for ((restrictedToWorldgen, rarity, newState) in list) {
            if (restrictedToWorldgen && !isWorldGeneration) continue

            if (rarity == 0 || random.nextInt(rarity) == 0) {
                return newState
            }
        }

        return original
    }
}
