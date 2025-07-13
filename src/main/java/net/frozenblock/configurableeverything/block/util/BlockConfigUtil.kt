package net.frozenblock.configurableeverything.block.util

import net.frozenblock.configurableeverything.config.BlockConfig
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.ENABLE_EXPERIMENTAL_FEATURES
import net.frozenblock.configurableeverything.util.ifExperimental
import net.frozenblock.configurableeverything.util.value
import net.frozenblock.lib.block.sound.api.BlockSoundTypeOverwrites
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries

object BlockConfigUtil {

    fun init() {
        val config = BlockConfig.get()
        if (!MainConfig.get().block) return

        ifExperimental {
            for (block in config.addedBlocks.value) {
                Registry.register(BuiltInRegistries.BLOCK, block.id, block.block)
            }
        }

        for (overwrite in config.soundGroupOverwrites.value) {
            BlockSoundTypeOverwrites.addBlock(
                overwrite.blockId,
                overwrite.soundOverwrite.immutable(),
                overwrite.condition
            )
        }
    }
}
