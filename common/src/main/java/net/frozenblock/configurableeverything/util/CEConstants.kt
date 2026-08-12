package net.frozenblock.configurableeverything.util

import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.config.ScriptingConfig
import net.frozenblock.lib.config.api.instance.json.JsonType
import net.frozenblock.lib.config.api.instance.xjs.XjsFormat
import net.frozenblock.lib.platform.ModLoader
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.Path

const val MOD_ID = "configurable_everything"
const val MOD_NAME = "Configurable Everything"

/**
 * Enables update 1.5
 */
const val ENABLE_EXPERIMENTAL_FEATURES = false

val DEV_ENV = ModLoader.isDevelopmentEnvironment()

@JvmField
val LOGGER: Logger? = LoggerFactory.getLogger(MOD_NAME)

@JvmField
var DEV_LOGGING = false

/**
 * Used for features that may be unstable and crash in public builds.
 *
 * It's smart to use this for at least registries.
 */
@JvmField
var UNSTABLE_LOGGING: Boolean = ModLoader.isDevelopmentEnvironment()

@JvmField
val ENVIRONMENT: String = ifClient { "client" } ?: "server"

inline val HAS_EXTENSIONS: Boolean
    get() = ModLoader.isModLoaded("ethans_kotlin_extensions")

inline val SCRIPTING_ENABLED
    get() = HAS_EXTENSIONS && MainConfig.scripting.get() && ScriptingConfig.applyKotlinScripts.get()

@JvmField
val DATAPACKS_PATH: Path = Path("config/$MOD_ID/datapacks")

@JvmField
val MAPPINGS_PATH: Path = Path(".$MOD_ID/mappings/")

@JvmField
val ORIGINAL_SOURCES_CACHE: Path = Path(".$MOD_ID/original/").apply {
    ifExtended { this.toFile().recreateDir() }
}

@JvmField
val REMAPPED_SOURCES_CACHE: Path = Path(".$MOD_ID/remapped/").apply {
    ifExtended { this.toFile().recreateDir() }
}

@JvmField
val KOTLIN_SCRIPT_PATH: Path = Path("config/$MOD_ID/scripts/")

@JvmField
//@Environment(EnvType.CLIENT) // env broken for some reason idk
val KOTLIN_CLIENT_SCRIPT_PATH: Path = KOTLIN_SCRIPT_PATH.resolve("client/")

// the idea is configurableeverything.kts but shorter
const val KOTLIN_SCRIPT_EXTENSION: String = "cevt.kts"

@JvmField
internal val LEGACY_CONFIG_JSONTYPE: JsonType = JsonType.JSON5

@JvmField
internal val CONFIG_FORMAT: XjsFormat = XjsFormat.DJS
