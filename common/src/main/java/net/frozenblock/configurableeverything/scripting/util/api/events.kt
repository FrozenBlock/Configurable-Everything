package net.frozenblock.configurableeverything.scripting.util.api

import net.frozenblock.lib.event.api.events.LifecycleEvents
import net.minecraft.server.MinecraftServer

fun serverStarted(callback: (MinecraftServer) -> Unit) {
    LifecycleEvents.SERVER_STARTED.register { callback(it) }
}
