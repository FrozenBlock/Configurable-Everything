package net.frozenblock.configurableeverything.util

import net.frozenblock.lib.config.api.instance.ConfigSerialization
import net.frozenblock.lib.config.api.instance.xjs.XjsConfig
import net.frozenblock.lib.config.v2.config.ConfigData
import net.frozenblock.lib.config.v2.config.ConfigSettings
import net.frozenblock.lib.config.v2.registry.ConfigV2Registry
import net.frozenblock.lib.shadow.xjs.data.JsonValue
import java.nio.file.Files
import kotlin.io.path.deleteIfExists
import kotlin.reflect.KClass

open class CEConfig(name: String) : ConfigData<JsonValue>(config(name), ConfigSettings.DJS) {
    init {
        ConfigV2Registry.register(this, this.id())
    }
}

open class CESimpleConfig<T : Any>(
    config: KClass<T>,
    private val configName: String,
    supportsModification: Boolean = true,
    private val thirdParty: Boolean = false
) : XjsConfig<T>(
    MOD_ID,
    config.java,
    if (thirdParty) makeThirdPartyConfigPath(configName) else makeConfigPath(configName),
    CONFIG_FORMAT,
    supportsModification
) {
    init {
        if (this.load()) {
            this.save()
        }
    }

    @Suppress("SENSELESS_COMPARISON")
    @Throws(Exception::class)
    override fun onLoad(): Boolean {
        if (this.configName == null)
            return false // cancels if run from the super constructor because the variable isn't set yet

        if (Files.exists(this.path()))
            return super.onLoad()

        val legacy = if (thirdParty) makeLegacyThirdPartyConfigPath(configName) else makeLegacyConfigPath(configName)
        if (Files.exists(legacy)) {
            val jankson = ConfigSerialization.createJankson(MOD_ID)
            this.setConfig(jankson.fromJson(jankson.load(legacy.toFile()), this.configClass()))
            legacy.deleteIfExists()
        }

        return true
    }
}
