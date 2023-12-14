package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.SyncBehavior
import net.frozenblock.lib.config.api.sync.annotation.EntrySyncData
import net.frozenblock.lib.config.api.sync.annotation.UnsyncableConfig
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment
import net.minecraft.network.chat.Component

// source: https://github.com/QuiltMC/quilt-standard-libraries/blob/1.20.2/library/core/registry/src/main/java/org/quiltmc/qsl/registry/impl/RegistryConfig.java
@UnsyncableConfig
data class ModProtocolConfig(
    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    @Comment(
"""
Mod protocol is a feature allowing you to prevent clients with mismatched settings to join.
Client with mismatched values won't be able to connect to servers having this enabled.
It should be used only for non-vanilla compatible modpacks!
Protocol version. Needs to be the same on client and server. If it has value of -1, it won't be required by servers.
"""
    )
    var modProtocolVersion: Int = -1,

    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    @Comment("Protocol id. It should be different for every modpack, to prevent joining with mismatched mods.")
    var modProtocolId: String = "my_configurable_everything_modpack",

    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    @Comment("A mod protocol name. Used for easier identification. Doesn't affect functionality")
    var modProtocolName: String = "My Configurable Everything Modpack",

    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    @Comment("Message displayed for players joining with clients incompatible with Registry Sync. Supports strings and Minecraft's JSON text format.")
    var missingRegistrySyncMessage: String = Component.Serializer.toJson(
        Component.translatableWithFallback(
            "frozenlib.registry_sync.unsupported_client",
"""
Unsupported (vanilla?) client!
This server requires modded client to join!
"""
        )
    ),

    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    @Comment("Top part of the message displayed for players joining with incompatible clients. Supports strings and Minecraft's JSON text format.")
    var mismatchedEntriesTopMessage: String = Component.Serializer.toJson(
        Component.translatableWithFallback(
            "frozenlib.registry_sync.failed_sync",
"""
Failed to synchronize client with the server!
This can happen when client's and server's mods don't match.
"""
        )
    ),

    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    @Comment("Bottom part of the message displayed for players joining with incompatible clients. Supports strings and Minecraft's JSON text format.")
    var mismatchedEntriesBottomMessage: String = "",

    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    @Comment("Shows some details about why client couldn't connect.")
    var mismatchedEntriesShowDetails: Boolean = true,

    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    @Comment("Disables the Mod Protocol sync on server list/initial query.")
    var disableModProtocolPing: Boolean = false,

    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    @Comment("Disables the Mod Protocol requirement. USE AT YOUR OWN RISK!")
    var disableModProtocol: Boolean = false,
) {

    companion object : JsonConfig<ModProtocolConfig>(
        MOD_ID,
        ModProtocolConfig::class.java,
        makeConfigPath("mod_protocol"),
        CONFIG_JSONTYPE,
        false,
        null,
        null
    ) {
        init {
            ConfigRegistry.register(this)
        }

        @JvmStatic
        fun get(): ModProtocolConfig = this.config()
    }
}
