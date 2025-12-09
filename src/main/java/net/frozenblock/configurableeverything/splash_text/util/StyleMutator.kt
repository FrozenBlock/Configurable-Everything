package net.frozenblock.configurableeverything.splash_text.util

import net.minecraft.network.chat.TextColor

interface StyleMutator {
    fun `configurableEverything$setTextColor`(color: Int)

    fun `configurableEverything$setTextColor`(color: TextColor?)
}
