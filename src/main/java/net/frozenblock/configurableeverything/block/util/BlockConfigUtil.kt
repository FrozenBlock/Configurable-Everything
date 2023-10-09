package net.frozenblock.configurableeverything.block.util

import net.frozenblock.configurableeverything.config.BlockConfig
import net.frozenblock.lib.sound.api.block_sound_group.BlockSoundGroupOverwrites

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
