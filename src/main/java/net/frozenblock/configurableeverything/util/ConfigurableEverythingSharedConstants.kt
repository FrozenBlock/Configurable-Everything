package net.frozenblock.configurableeverything.util

import net.fabricmc.loader.api.FabricLoader
import org.slf4j.LoggerFactory

object ConfigurableEverythingSharedConstants {

    const val MOD_ID = "configurable_everything"
    @JvmField
    val LOGGER = LoggerFactory.getLogger(MOD_ID)
    var DEV_LOGGING = false

    /**
     * Used for features that may be unstable and crash in public builds.
     *
     *
     * It's smart to use this for at least registries.
     */
    @JvmField
    var UNSTABLE_LOGGING = FabricLoader.getInstance().isDevelopmentEnvironment
    @JvmField
    val MOD_CONTAINER = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow()
    var areConfigsInit = false

    /**
     * Used for datafixers.
     *
     *
     * Is not necessary for a normal mod, but can be useful in some cases.
     */
    const val DATA_VERSION = 0
}
