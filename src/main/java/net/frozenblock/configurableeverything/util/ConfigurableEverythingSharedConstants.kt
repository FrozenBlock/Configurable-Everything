package net.frozenblock.configurableeverything.util

import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.ModContainer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Path

const val MOD_ID = "configurable_everything"
const val MOD_NAME = "Configurable Everything"

@JvmField
val LOGGER: Logger? = LoggerFactory.getLogger(MOD_ID)

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
val MOD_CONTAINER: ModContainer = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow()

@JvmField
val DATAPACKS_PATH: Path = Path.of("./config/$MOD_ID/datapacks")
