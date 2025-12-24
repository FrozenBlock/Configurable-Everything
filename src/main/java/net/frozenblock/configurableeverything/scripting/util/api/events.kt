package net.frozenblock.configurableeverything.scripting.util.api

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.server.MinecraftServer

fun serverStarted(callback: (MinecraftServer) -> Unit) {
    ServerLifecycleEvents.SERVER_STARTED.register { callback(it) }
}
