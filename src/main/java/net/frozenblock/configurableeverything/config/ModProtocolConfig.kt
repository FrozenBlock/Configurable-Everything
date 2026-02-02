package net.frozenblock.configurableeverything.config

import com.mojang.serialization.JsonOps
import net.frozenblock.configurableeverything.util.CEConfig
import net.frozenblock.configurableeverything.util.CESimpleConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.SyncBehavior
import net.frozenblock.lib.config.v2.entry.ConfigEntry
import net.frozenblock.lib.config.v2.entry.EntryType
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization

private fun toJson(component: Component): String {
    return ComponentSerialization.CODEC.encodeStart(JsonOps.INSTANCE, component).orThrow.toString()
}

// source: https://github.com/QuiltMC/quilt-standard-libraries/blob/1.20.2/library/core/registry/src/main/java/org/quiltmc/qsl/registry/impl/RegistryConfig.java
// UNSYNCABLE
object ModProtocolConfig : CEConfig("mod_protocol") {
    @JvmField
    var modProtocolVersion: ConfigEntry<Int> = this.unsyncableEntry("modProtocolVersion", EntryType.INT, -1,
        """
        Mod protocol is a feature allowing you to prevent clients with mismatched settings to join.
        Client with mismatched values won't be able to connect to servers having this enabled.
        It should be used only for non-vanilla compatible modpacks!
        Protocol version. Needs to be the same on client and server. If it has value of -1, it won't be required by servers.
        """.trimIndent()
    )

    @JvmField
    var modProtocolId: ConfigEntry<String> = this.unsyncableEntry("modProtocolId", EntryType.STRING, "my_configurable_everything_modpack",
        "Protocol id. It should be different for every modpack, to prevent joining with mismatched mods."
    )

    @JvmField
    var modProtocolName: ConfigEntry<String> = this.unsyncableEntry("modProtocolName", EntryType.STRING, "My Configurable Everything Modpack",
        "A mod protocol name. Used for easier identification. Doesn't affect functionality"
    )

    @JvmField
    var missingRegistrySyncMessage: ConfigEntry<String> = this.unsyncableEntry("missingRegistrySyncMessage", EntryType.STRING,
        toJson(
            Component.translatableWithFallback(
                "frozenlib.registry_sync.unsupported_client",
                """
                Unsupported (vanilla?) client!
                This server requires modded client to join!
                """.trimIndent()
            )
        ),
        "Message displayed for players joining with clients incompatible with Registry Sync. Supports strings and Minecraft's JSON text format."
    )

    @JvmField
    var mismatchedEntriesTopMessage: ConfigEntry<String> = this.unsyncableEntry("mismatchedEntriesTopMessage", EntryType.STRING,
        toJson(
            Component.translatableWithFallback(
                "frozenlib.registry_sync.failed_sync",
                """
                Failed to synchronize client with the server!
                This can happen when client's and server's mods don't match.
                """.trimIndent(),
            )
        ),
        "Top part of the message displayed for players joining with incompatible clients. Supports strings and Minecraft's JSON text format."
    )

    @JvmField
    var mismatchedEntriesBottomMessage: ConfigEntry<String> = this.unsyncableEntry("mismatchedEntriesBottomMessage", EntryType.STRING, "",
        "Bottom part of the message displayed for players joining with incompatible clients. Supports strings and Minecraft's JSON text format."
    )

    @JvmField
    var mismatchedEntriesShowDetails: ConfigEntry<Boolean> = this.unsyncableEntry("mismatchedEntriesShowDetails", EntryType.BOOL, true,
        "Shows some details about why client couldn't connect."
    )

    @JvmField
    var disableModProtocolPing: ConfigEntry<Boolean> = this.unsyncableEntry("disableModProtocolPing", EntryType.BOOL, false,
        "Disables the Mod Protocol sync on server list/initial query."
    )

    @JvmField
    var disableModProtocol: ConfigEntry<Boolean> = this.unsyncableEntry("disableModProtocol", EntryType.BOOL, false,
        "Disables the Mod Protocol requirement. USE AT YOUR OWN RISK!"
    )
}
