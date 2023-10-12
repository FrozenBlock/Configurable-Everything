package net.frozenblock.configurableeverything.block.util

import net.frozenblock.configurableeverything.config.BlockConfig
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.lib.sound.api.block_sound_group.BlockSoundGroupOverwrites

object BlockConfigUtil {

    @JvmStatic
    fun init() {
        val config = BlockConfig.get()
        if (MainConfig.get().block == false) return
        config.soundGroupOverwrites?.value?.let { overwrites ->
            for (overwrite in overwrites) {
                val immutable = overwrite?.immutable() ?: continue
                BlockSoundGroupOverwrites.addBlock(
                    immutable.blockId,
                    immutable.soundOverwrite,
                    immutable.condition
                )
            }
        }
    }
}
