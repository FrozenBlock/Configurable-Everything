package net.frozenblock.configurableeverything.config

import blue.endless.jankson.Comment
import net.frozenblock.configurableeverything.registry.util.BiomeAddition
import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.instance.json.JsonType
import net.frozenblock.lib.config.api.registry.ConfigRegistry

class RegistryConfig {
    companion object {
        private val BIOME_ADDITIONS: TypedEntryType<List<BiomeAddition>> = ConfigRegistry.register(
            TypedEntryType(
                MOD_ID,
                BiomeAddition.CODEC.listOf()
            )
        )

        private val INSTANCE: Config<RegistryConfig> = ConfigRegistry.register(
            JsonConfig(
                MOD_ID,
                RegistryConfig::class.java,
                makeConfigPath("registry"),
                CONFIG_JSONTYPE
            )
        )

        @JvmStatic
        fun get(): RegistryConfig = INSTANCE.config()

        @JvmStatic
        fun getConfigInstance(): Config<RegistryConfig> = INSTANCE
    }

    var osf: Boolean = true
}
