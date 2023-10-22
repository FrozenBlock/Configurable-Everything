package net.frozenblock.configurableeverything.util

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.loader.api.FabricLoader
import net.frozenblock.lib.config.api.instance.json.JsonType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.pathString

const val MOD_ID = "configurable_everything"
const val MOD_NAME = "Configurable Everything"

/**
 * Enables update 1.1
 */
const val ENABLE_EXPERIMENTAL_FEATURES = false

@JvmField
val LOGGER: Logger? = LoggerFactory.getLogger(MOD_NAME)

@JvmField
var DEV_LOGGING = false

/**
 * Used for features that may be unstable and crash in public builds.
 *
 *
 * It's smart to use this for at least registries.
 */
@JvmField
var UNSTABLE_LOGGING: Boolean = FabricLoader.getInstance().isDevelopmentEnvironment

@JvmField
val HAS_EXTENSIONS: Boolean = FabricLoader.getInstance().isModLoaded("fabric_kotlin_extensions")

@JvmField
val DATAPACKS_PATH: Path = Path("./config/$MOD_ID/datapacks")

@JvmField
val KOTLIN_SCRIPT_PATH: Path = Path("./config/$MOD_ID/scripts/")

@JvmField
@Environment(EnvType.CLIENT)
val KOTLIN_CLIENT_SCRIPT_PATH: Path = Path("${KOTLIN_SCRIPT_PATH.pathString}/client/")

// the idea is configurableeverything.kts but shorter
const val KOTLIN_SCRIPT_EXTENSION: String = "cevt.kts"

@JvmField
val CONFIG_JSONTYPE: JsonType = JsonType.JSON5
