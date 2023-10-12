package net.frozenblock.configurableeverything.scripting.util

import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.*
import java.io.File
import kotlin.script.experimental.api.EvaluationResult
import kotlin.script.experimental.api.ResultWithDiagnostics
import kotlin.script.experimental.api.ScriptDiagnostic
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost

private object InvalidChecker {
    init {
        if (!HAS_EXTENSIONS) throw IllegalStateException("Attempted to access scripting functionality without Fabric Kotlin Extensions mod!")
    }
}

internal object ScriptingUtil {

    private fun runScript(script: File): ResultWithDiagnostics<EvaluationResult> {
        val compilationConfiguration = CEScriptCompilationConfig
        val evaluationConfiguration = CEScriptEvaluationConfig
        return BasicJvmScriptingHost().eval(script.toScriptSource(), compilationConfiguration, evaluationConfiguration)
    }

    fun runScripts() {
        if (MainConfig.get().kotlinScripting?.applyKotlinScripts == false)
            return
        val folder = File(KOTLIN_SCRIPT_PATH.toString()).listFiles() ?: return
        for (file in folder) {
            val result = runScript(file)
            result.reports.forEach {
                val message = " : ${it.message}" + if (it.exception == null) "" else ": ${it.exception}"
                when (it.severity) {
                    ScriptDiagnostic.Severity.DEBUG -> logDebug(message)
                    ScriptDiagnostic.Severity.INFO -> log(message)
                    ScriptDiagnostic.Severity.WARNING -> logWarn(message)
                    ScriptDiagnostic.Severity.ERROR -> logError(message)
                    else -> logError(message)
                }
            }
        }
    }
}
