package net.frozenblock.configurableeverything.config.gui.api

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.network.chat.Component

@Environment(EnvType.CLIENT)
data class MultiElementBuilder<T>(
    @JvmField val title: Component,
    @JvmField val value: T,
    @JvmField val defaultValue: T
)
