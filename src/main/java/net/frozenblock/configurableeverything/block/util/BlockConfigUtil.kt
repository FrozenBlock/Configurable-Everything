package net.frozenblock.configurableeverything.block.util

import net.frozenblock.lib.sound.api.block_sound_group.*
import net.frozenblock.configurableeverything.config.BlockConfig

object BlockConfigUtil {

    @JvmStatic
    fun init() {
        val config = BlockConfig.get()
        config.soundGroupOverwrites?.value?.let { overwrites ->
            overwrites.forEach { overwrite ->
                if (overwrite != null) {
                    BlockSoundGroupOverwrites.addBlock(overwrite.blockId, overwrite.soundOverwrite, overwrite.condition)
                }
            }
        }
    }
}