package net.frozenblock.configurableeverything.mod_protocol.util

import it.unimi.dsi.fastutil.ints.IntList
import net.frozenblock.configurableeverything.config.ModProtocolConfig
import org.quiltmc.qsl.frozenblock.core.registry.api.sync.ModProtocol
import org.quiltmc.qsl.frozenblock.core.registry.api.sync.ModProtocolDef
import org.quiltmc.qsl.frozenblock.core.registry.impl.sync.server.ServerRegistrySync

object ModProtocolConfigUtil : ModProtocol.LoadModProtocol {

    override fun load() {
        val config = ModProtocolConfig.get()
        ModProtocol.disableQuery = config.disableModProtocolPing

        if (config.modProtocolVersion >= 0) {
            ModProtocol.prioritizedEntry = ModProtocolDef("global:${config.modProtocolId}", config.modProtocolName, IntList.of(config.modProtocolVersion), false)
            ModProtocol.prioritizedId = ModProtocol.prioritizedEntry.id
            ModProtocol.add(ModProtocol.prioritizedEntry)
        }

        ServerRegistrySync.noRegistrySyncMessage = ServerRegistrySync.text(config.missingRegistrySyncMessage)
        ServerRegistrySync.errorStyleHeader = ServerRegistrySync.text(config.mismatchedEntriesTopMessage)
        ServerRegistrySync.errorStyleFooter = ServerRegistrySync.text(config.mismatchedEntriesBottomMessage)

        ServerRegistrySync.forceDisable = config.disableModProtocol
        ServerRegistrySync.showErrorDetails = config.mismatchedEntriesShowDetails

        if (ServerRegistrySync.forceDisable) {
            ServerRegistrySync.SERVER_SUPPORTED_PROTOCOL.clear()
        }
    }
}
