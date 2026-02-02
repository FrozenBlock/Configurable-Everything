package net.frozenblock.configurableeverything.block.util

import net.frozenblock.configurableeverything.config.BlockConfig
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.value
import net.frozenblock.lib.block.sound.api.BlockSoundTypeOverwrites

object BlockConfigUtil {

    fun init() {
        if (!MainConfig.block.get()) return
        for (overwrite in BlockConfig.soundGroupOverwrites.get()) {
            BlockSoundTypeOverwrites.addBlock(
                overwrite.blockId,
                overwrite.soundOverwrite.immutable(),
                overwrite.condition
            )
        }
    }
}
