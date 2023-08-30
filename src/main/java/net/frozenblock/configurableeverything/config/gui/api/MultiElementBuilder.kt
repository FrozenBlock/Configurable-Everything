package net.frozenblock.configurableeverything.config.gui.api

import net.minecraft.network.chat.Component

data class MultiElementBuilder<T>(val title: Component, val value: T, val defaultValue: T)
