package net.frozenblock.configurableeverything.block.util

import net.frozenblock.configurableeverything.config.BlockConfig
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.value
import net.frozenblock.lib.sound.api.block_sound_group.BlockSoundGroupOverwrites

object BlockConfigUtil {

    fun init() {
        val config = BlockConfig.get()
        if (!MainConfig.get().block) return
        for (overwrite in config.soundGroupOverwrites.value) {
            val immutable = overwrite.immutable()
            BlockSoundGroupOverwrites.addBlock(
                immutable.blockId,
                immutable.soundOverwrite,
                immutable.condition
            )
        }
    }
}
