package net.frozenblock.configurableeverything.scripting.util

import java.nio.file.Path
import kotlin.io.path.Path

internal object ScriptTrust {
    @JvmField
    internal val TRUST_FOLDER: Path = Path("${System.getProperty("user.home")}/.configurable-everything/").apply {
        FileUtil.createDirectoriesSafe(this)
    }

    @JvmField
    internal val TRUST_CACHE: File = File(TRUST_FOLDER.resolve("trust.json"))

    /**
     * User's trust preferences. List of trusted scripts by absolute file path
     */
    private val trust: List<String>

    internal fun loadTrust() {
        val jankson = Jankson.builder().build()

        // TODO: load

        this.saveTrust()
    }

    private fun saveTrust() {
        Files.newBufferedWriter(
            TRUST_CACHE.path,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING
        ).use { writer ->
            // TODO: write
            //writer.write(jankson.toJson(trust).toJson())
        }
    }
}