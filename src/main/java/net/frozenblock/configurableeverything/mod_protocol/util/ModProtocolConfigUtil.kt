package net.frozenblock.configurableeverything.mod_protocol.util

import it.unimi.dsi.fastutil.ints.IntList
import net.frozenblock.configurableeverything.config.ModProtocolConfig
import org.quiltmc.qsl.frozenblock.core.registry.api.sync.ModProtocol
import org.quiltmc.qsl.frozenblock.core.registry.api.sync.ModProtocolDef
import org.quiltmc.qsl.frozenblock.core.registry.impl.sync.server.ServerRegistrySync

object ModProtocolConfigUtil : ModProtocol.LoadModProtocol {

    override fun load() {
        var disableModProtocolPing = ModProtocolConfig.disableModProtocolPing.get()
        val disableModProtocol = ModProtocolConfig.disableModProtocol.get()
        val modProtocolId = ModProtocolConfig.modProtocolId.get()
        val modProtocolName = ModProtocolConfig.modProtocolName.get()
        val modProtocolVersion = ModProtocolConfig.modProtocolVersion.get()
        val missingRegistrySyncMessage = ModProtocolConfig.missingRegistrySyncMessage.get()
        val mismatchedEntriesTopMessage = ModProtocolConfig.mismatchedEntriesTopMessage.get()
        val mismatchedEntriesBottomMessage = ModProtocolConfig.mismatchedEntriesBottomMessage.get()
        val mismatchedEntriesShowDetails = ModProtocolConfig.mismatchedEntriesShowDetails.get()

        ModProtocol.disableQuery = disableModProtocolPing

        if (modProtocolVersion >= 0) {
            ModProtocol.prioritizedEntry = ModProtocolDef("global:${modProtocolId}", modProtocolName, IntList.of(modProtocolVersion), false)
            ModProtocol.prioritizedId = ModProtocol.prioritizedEntry.id
            ModProtocol.add(ModProtocol.prioritizedEntry)
        }

        ServerRegistrySync.noRegistrySyncMessage = ServerRegistrySync.text(missingRegistrySyncMessage)
        ServerRegistrySync.errorStyleHeader = ServerRegistrySync.text(mismatchedEntriesTopMessage)
        ServerRegistrySync.errorStyleFooter = ServerRegistrySync.text(mismatchedEntriesBottomMessage)

        ServerRegistrySync.forceDisable = disableModProtocol
        ServerRegistrySync.showErrorDetails = mismatchedEntriesShowDetails

        if (ServerRegistrySync.forceDisable) {
            ServerRegistrySync.SERVER_SUPPORTED_PROTOCOL.clear()
        }
    }
}
