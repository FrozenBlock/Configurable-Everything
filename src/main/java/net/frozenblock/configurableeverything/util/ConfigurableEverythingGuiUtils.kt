import net.frozenblock.configurableeverything.util

import net.minecraft.network.chat.Component

fun text(key: String?): Component = Component.translatable("option.$MOD_ID.$key")

fun tooltip(key: String?): Component = Component.translatable("tooltip.$MOD_ID.$key")