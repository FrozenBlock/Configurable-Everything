package net.frozenblock.configurableeverything.scripting.util

import kotlinx.coroutines.runBlocking
import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.scripting.util.remap.remapScript
import net.frozenblock.configurableeverything.util.*
import java.io.File
import java.nio.file.Path
import kotlin.script.experimental.api.*
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.BasicJvmScriptEvaluator
import kotlin.script.experimental.jvm.impl.KJvmCompiledScript
import kotlin.script.experimental.jvmhost.BasicJvmScriptJarGenerator
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
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

internal object ScriptingUtil {

    private suspend fun runScript(script: File): ResultWithDiagnostics<EvaluationResult> {
        val compilationConfiguration = CEScriptCompilationConfig
        val evaluationConfiguration = CEScriptEvaluationConfig
        if (ENABLE_EXPERIMENTAL_FEATURES) {
            val compiledScript: KJvmCompiledScript = JvmScriptCompiler()(
                script.toScriptSource(),
                compilationConfiguration
            ).apply { this.logReports() }.valueOrNull() as? KJvmCompiledScript ?: error("Compiled script is not java or is null")
            val file = File(".$MOD_ID/original_scripts/${script.name}.jar")
            BasicJvmScriptJarGenerator(file)(compiledScript, evaluationConfiguration)
            val remappedFile: File = remapScript(file)
            val remappedScript: CompiledScript = remappedFile.loadScriptFromJar() ?: error("Remapped script is null")
            return BasicJvmScriptEvaluator()(remappedScript, evaluationConfiguration)
        }
        return BasicJvmScriptingHost().eval(script.toScriptSource(), compilationConfiguration, evaluationConfiguration)
    }

    fun runScripts() {
        log("Running scripts")
        if (MainConfig.get().kotlinScripting?.applyKotlinScripts != true)
            return
        runScripts(KOTLIN_SCRIPT_PATH)
        if (FabricLoader.getInstance().environmentType == EnvType.CLIENT)
            runScripts(KOTLIN_CLIENT_SCRIPT_PATH)

        CEScript.POST_RUN_FUNS?.apply {
            this.toSortedMap().forEach { (_, value) ->
                value.invoke() // make sure to not use coroutines here
            }
        }
    }

    private fun runScripts(path: Path) {
        val folder = path.toFile().listFiles() ?: return
        for (file in folder) {
            if (file.isDirectory) continue
            try {
                val result = runBlocking { runScript(file) }
                result.logReports()
            } catch (e: Exception) {
                logError("Error while running script $file", e)
            }
        }
    }
}
