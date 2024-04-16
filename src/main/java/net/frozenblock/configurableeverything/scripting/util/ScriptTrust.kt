package net.frozenblock.configurableeverything.scripting.util

import net.frozenblock.lib.shadow.blue.endless.jankson.Jankson
import net.minecraft.FileUtil
import java.io.BufferedWriter
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.io.path.Path

private data class ScriptTrustInfo(val trustedScripts: List<String>)

internal object ScriptTrust {
    @JvmField
    private val TRUST_FOLDER: Path = Path("${System.getProperty("user.home")}/.configurable-everything/").apply {
        FileUtil.createDirectoriesSafe(this)
    }

    @JvmField
    private val TRUST_CACHE: File = File(TRUST_FOLDER.resolve("trust.json"))

    /**
     * User's trust preferences. List of trusted scripts by absolute file path
     */
    private lateinit var trustInfo: ScriptTrustInfo

    /**
     * Called by the mixin config to prevent mixins from being applied to this file.
     * <p>
     * Under no circumstances should this be able to be injected into.
     */
    internal fun preventMixins() {}

    internal fun loadTrust() {
        // ensure this is called from Configurable Everything
        val caller = Thread.currentThread().getStackTrace()[2]
        val callerClass: String = caller.getClassName()
        if (callerClass != "net.frozenblock.configurableeverything.ConfigurableEverything")
            throw RuntimeException("Prevented script trust tampering from $callerClass")


        val jankson = Jankson.builder().build()

        try {
            this.trustInfo = jankson.fromJson(jankson.load(TRUST_CACHE), ScriptTrustInfo::class.java)
        } catch (e: Exception) {
            this.trustInfo = ScriptTrustInfo(listOf())
        }

        this.saveTrust()
    }

    private fun saveTrust() {
        Files.newBufferedWriter(
            TRUST_CACHE.path,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING
        ).use { writer ->
            writer.write(jankson.toJson(this.trustInfo).toJson(JsonGrammar.COMPACT))
        }
    }
}