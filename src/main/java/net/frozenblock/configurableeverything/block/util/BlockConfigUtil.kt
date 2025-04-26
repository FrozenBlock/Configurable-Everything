package net.frozenblock.configurableeverything.block.util

import net.frozenblock.configurableeverything.config.BlockConfig
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.value
import net.frozenblock.lib.block.sound.api.BlockSoundTypeOverwrites

object BlockConfigUtil {

    fun init() {
        val config = BlockConfig.get()
        if (!MainConfig.get().block) return
        for (overwrite in config.soundGroupOverwrites.value) {
            BlockSoundTypeOverwrites.addBlock(
                overwrite.blockId,
                overwrite.soundOverwrite.immutable(),
                overwrite.condition
            )
        }
    }
}
