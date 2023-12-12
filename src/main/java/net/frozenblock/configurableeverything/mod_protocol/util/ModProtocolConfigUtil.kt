package net.frozenblock.configurableeverything.mod_protocol.util

import it.unimi.dsi.fastutil.ints.IntList
import net.frozenblock.configurableeverything.config.ModProtocolConfig
import net.minecraft.network.chat.Component
import org.quiltmc.qsl.frozenblock.core.registry.api.sync.ModProtocol
import org.quiltmc.qsl.frozenblock.core.registry.impl.sync.mod_protocol.ModProtocolDef
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

        ServerRegistrySync.noRegistrySyncMessage = text(config.missingRegistrySyncMessage)
        ServerRegistrySync.errorStyleHeader = text(config.mismatchedEntriesTopMessage)
        ServerRegistrySync.errorStyleFooter = text(config.mismatchedEntriesBottomMessage)

        ServerRegistrySync.forceDisable = config.disableModProtocol
        ServerRegistrySync.showErrorDetails = config.mismatchedEntriesShowDetails

        if (ServerRegistrySync.forceDisable) {
            ServerRegistrySync.SERVER_SUPPORTED_PROTOCOL.clear()
        }
    }

    @Deprecated("Replace with frozenlib 1.5.1 at some point")
    private fun text(string: String?): Component {
        if (string.isNullOrEmpty()) {
            return Component.empty()
        }

        var text: Component? = null
        try {
            text = Component.Serializer.fromJson(string)
        } catch (ignored: Exception) {
        }

        return text ?: Component.literal(string)
    }
}
