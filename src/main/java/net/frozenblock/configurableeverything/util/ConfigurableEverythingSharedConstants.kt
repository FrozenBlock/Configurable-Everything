package net.frozenblock.configurableeverything.util

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.loader.api.FabricLoader
import net.frozenblock.lib.FrozenBools
import net.frozenblock.lib.config.api.instance.json.JsonType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.pathString

internal const val MOD_ID = "configurable_everything"
internal const val MOD_NAME = "Configurable Everything"

/**
 * Enables update 1.1
 */
internal const val ENABLE_EXPERIMENTAL_FEATURES = true

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

@JvmField
val DATAPACKS_PATH: Path = Path("./config/$MOD_ID/datapacks")

@JvmField
val INTERMEDIARY_ROOT_CACHE_PATH: Path = if (FrozenBools.IS_QUILT) Path(".quilt/") else Path(".fabric/")

@JvmField
val INTERMEDIARY_GAME_CACHE_PATH: Path = Path("${INTERMEDIARY_ROOT_CACHE_PATH.pathString}/remappedJars/minecraft-${modContainer("minecraft").version}-${modContainer("fabricloader").version}/")

@JvmField
val INTERMEDIARY_MOD_CACHE_PATH: Path = Path("${INTERMEDIARY_ROOT_CACHE_PATH.pathString}/processedMods/")

@JvmField
val RAW_MAPPINGS_PATH: Path = Path(".$MOD_ID/mappings/raw/")

@JvmField
val TINY_MAPPINGS_PATH: Path = Path(".$MOD_ID/mappings/tiny/")

@JvmField
val OFFICIAL_SOURCES_CACHE: Path = Path(".$MOD_ID/official/")

@JvmField
val REMAPPED_SOURCES_CACHE: Path = Path("./.$MOD_ID/remapped/")

@JvmField
val KOTLIN_SCRIPT_PATH: Path = Path("./config/$MOD_ID/scripts/")

@JvmField
@Environment(EnvType.CLIENT)
val KOTLIN_CLIENT_SCRIPT_PATH: Path = Path("${KOTLIN_SCRIPT_PATH.pathString}/client/")

// the idea is configurableeverything.kts but shorter
const val KOTLIN_SCRIPT_EXTENSION: String = "cevt.kts"

@JvmField
internal val CONFIG_JSONTYPE: JsonType = JsonType.JSON5
