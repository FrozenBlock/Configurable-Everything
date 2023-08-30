package net.frozenblock.configurableeverything.config.gui.api

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.network.chat.Component

@Environment(EnvType.CLIENT)
data class MultiElementBuilder<T>(val title: Component, val value: T, val defaultValue: T)
