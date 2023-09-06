package net.frozenblock.configurableeverything.util

import java.nio.file.Path

/**
 * @return The path of the config file.
 */
fun makeConfigPath(name: String?, json5: Boolean): Path
    = Path.of("./config/$MOD_ID/$name.${if (json5) "json5" else "json"}")

// could use default parameters (json5: Boolean = true) but this solution is java-compatible
fun makeConfigPath(name: String?): Path = makeConfigPath(name, true)