package net.frozenblock.configurableeverything.util

import net.fabricmc.loader.api.FabricLoader
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.config.ScriptingConfig
import net.frozenblock.lib.config.api.instance.json.JsonType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.Path

@PublishedApi
internal const val MOD_ID = "configurable_everything"
@PublishedApi
internal const val MOD_NAME = "Configurable Everything"

/**
 * Enables update 1.1
 */
@PublishedApi
internal const val ENABLE_EXPERIMENTAL_FEATURES = false

internal val DEV_ENV = FabricLoader.getInstance().isDevelopmentEnvironment

@JvmField
internal val LOGGER: Logger? = LoggerFactory.getLogger(MOD_NAME)

@JvmField
internal var DEV_LOGGING = false

/**
 * Used for features that may be unstable and crash in public builds.
 *
 * It's smart to use this for at least registries.
 */
@JvmField
internal var UNSTABLE_LOGGING: Boolean = FabricLoader.getInstance().isDevelopmentEnvironment

@JvmField
internal val ENVIRONMENT: String = ifClient { "client" } ?: "server"

@JvmField
val HAS_EXTENSIONS: Boolean = FabricLoader.getInstance().isModLoaded("fabric_kotlin_extensions")

inline val SCRIPTING_ENABLED get() = HAS_EXTENSIONS && MainConfig.get().scripting == true && ScriptingConfig.get().applyKotlinScripts == true

@JvmField
val DATAPACKS_PATH: Path = Path("config/$MOD_ID/datapacks")

inline val INPUT_GAME_JARS: List<Path>
    @Suppress("UNCHECKED_CAST")
    get() = FabricLoader.getInstance().objectShare["fabric-loader:inputGameJars"] as List<Path>

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
internal val CONFIG_JSONTYPE: JsonType = JsonType.JSON5
