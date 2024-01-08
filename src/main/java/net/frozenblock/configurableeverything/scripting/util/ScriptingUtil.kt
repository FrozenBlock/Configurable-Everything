package net.frozenblock.configurableeverything.scripting.util

import kotlinx.coroutines.runBlocking
import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.config.ScriptingConfig
import net.frozenblock.configurableeverything.scripting.util.remap.Remapping
import net.frozenblock.configurableeverything.scripting.util.remap.Remapping.ORIGINAL_SCRIPTS
import net.frozenblock.configurableeverything.util.*
import java.io.File
import java.nio.file.Path
import kotlin.script.experimental.api.*
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.BasicJvmScriptEvaluator
import kotlin.script.experimental.jvm.impl.KJvmCompiledScript
import kotlin.script.experimental.jvmhost.BasicJvmScriptJarGenerator
import kotlin.script.experimental.jvmhost.JvmScriptCompiler
import kotlin.script.experimental.jvmhost.loadScriptFromJar

private object InvalidChecker {
    init {
        check(HAS_EXTENSIONS) { "Attempted to access scripting functionality without Fabric Kotlin Extensions mod!" }
    }
}

private fun ResultWithDiagnostics<*>.logReports() {
    this.reports.forEach {
        val message = " : ${it.message}" + if (it.exception == null) "" else ": ${it.exception}"
        when (it.severity) {
            ScriptDiagnostic.Severity.DEBUG -> logDebug(message)
            ScriptDiagnostic.Severity.INFO -> log(message)
            ScriptDiagnostic.Severity.WARNING -> logWarn(message)
            ScriptDiagnostic.Severity.ERROR -> logError(message, it.exception)
            ScriptDiagnostic.Severity.FATAL -> logError(message, it.exception)
            else -> logError(message)
        }
    }
}

internal sealed class ScriptType(val envType: EnvType?) {
    data object CLIENT : ScriptType(EnvType.CLIENT)
    data object COMMON : ScriptType(null)
}

internal object ScriptingUtil {

    private var COMPILED_SCRIPTS: MutableList<String>? = mutableListOf()
    private val SCRIPTS_TO_EVAL: MutableMap<CompiledScript, File> = mutableMapOf()

    private suspend fun compileScript(script: File, type: ScriptType, addToBuffer: Boolean = true): Pair<CompiledScript, File>? {
        if (COMPILED_SCRIPTS?.contains(script.path) == true) return null
        COMPILED_SCRIPTS?.add(script.path)

        val envType = type.envType
        if (envType != null && envType != FabricLoader.getInstance().environmentType) return null

        val compilationConfiguration = CEScriptCompilationConfig(type)
        val evaluationConfiguration = CEScriptEvaluationConfig
        val compiledScript: KJvmCompiledScript = JvmScriptCompiler()(
            script.toScriptSource(),
            compilationConfiguration
        ).apply { this.logReports() }.valueOrNull() as? KJvmCompiledScript ?: error("Compiled script is not java or is null")
        if (!DEV_ENV && ENABLE_EXPERIMENTAL_FEATURES && ScriptingConfig.get().remapping == true) {
            val file = ORIGINAL_SCRIPTS.resolve("${script.name}.jar").toFile()
            BasicJvmScriptJarGenerator(file)(compiledScript, evaluationConfiguration)
            val remappedFile: File = Remapping.remapScript(file)
            val remappedScript: CompiledScript = remappedFile.loadScriptFromJar() ?: error("Remapped script is null")
            if (addToBuffer) SCRIPTS_TO_EVAL[remappedScript] = remappedFile
            return Pair(remappedScript, remappedFile)
        }
        if (addToBuffer) SCRIPTS_TO_EVAL[compiledScript] = script
        return Pair(compiledScript, script)
    }

    fun runScripts() {
        if (MainConfig.get().scripting != true || ScriptingConfig.get().applyKotlinScripts != true)
            return
        log("Running scripts")
        compileScripts(KOTLIN_SCRIPT_PATH, ScriptType.COMMON)
        if (FabricLoader.getInstance().environmentType == EnvType.CLIENT)
            compileScripts(KOTLIN_CLIENT_SCRIPT_PATH, ScriptType.CLIENT)
        COMPILED_SCRIPTS?.clear(); COMPILED_SCRIPTS = null

        // verify the scripts don't use remapped sources
        REMAPPED_SOURCES_CACHE.toFile().recreateDir()
        runBlocking { evalScripts() }

        CEScript.POST_RUN_FUNS?.apply {
            this.toSortedMap().forEach { (_, value) ->
                value.invoke() // make sure to not use coroutines here
            }
        }
    }

    private fun compileScripts(path: Path, type: ScriptType) {
        val folder = path.toFile().listFiles() ?: return
        for (file in folder) {
            if (file.isDirectory) continue
            try {
                runBlocking { compileScript(file, type) }
            } catch (e: Exception) {
                logError("Error while compiling script $file", e)
            }
        }
    }

    @Throws(Exception::class)
    internal suspend fun forceRunScript(file: File, type: ScriptType) {
        val (compiledScript, compiledFile) = runBlocking { compileScript(file, type, addToBuffer = false) }
            ?: throw Exception("Unable to compile script $file")
        evalScript(compiledScript, compiledFile)
    }

    private suspend fun evalScript(script: CompiledScript, file: File) {
        try {
            val result = BasicJvmScriptEvaluator()(script, CEScriptEvaluationConfig)
            result.logReports()
        } catch (e: Exception) {
            logError("Error while running script file $file")
        }
    }

    private suspend fun evalScripts() {
        for ((script, file) in SCRIPTS_TO_EVAL) {
            evalScript(script, file)
        }
        SCRIPTS_TO_EVAL.clear()
    }
}
