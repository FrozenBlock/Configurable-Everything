package net.frozenblock.configurableeverything.util

import net.fabricmc.loader.api.FabricLoader
import net.frozenblock.configurableeverything.scripting.util.MCVersion
import net.frozenblock.lib.config.api.instance.json.JsonType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Path

const val MOD_ID = "configurable_everything"
const val MOD_NAME = "Configurable Everything"

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
val DATAPACKS_PATH: Path = Path.of("./config/$MOD_ID/datapacks")

@JvmField
val KOTLIN_SCRIPT_PATH: Path = Path.of("./config/$MOD_ID/scripts/")

@JvmField
val MAPPINGS_PATH: Path = Path.of("./.$MOD_ID/.mappings/")

@JvmField
val REMAPPED_SCRIPT_SOURCES_PATH: Path = Path.of("./.$MOD_ID/.remapped/")

// the idea is configurableeverything.kts but shorter
const val KOTLIN_SCRIPT_EXTENSION: String = "cevt.kts"

@JvmField
val CONFIG_JSONTYPE: JsonType = JsonType.JSON5

fun mappingsFile(version: MCVersion?, type: String): Path? {
    if (version != null) return MAPPINGS_PATH.resolve("${type}_${version.id}.gz")
    return null
}
