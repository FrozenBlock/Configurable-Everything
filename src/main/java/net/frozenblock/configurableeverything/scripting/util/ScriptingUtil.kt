package net.frozenblock.configurableeverything.scripting.util

import kotlin.script.experimental.*
import kotlin.script.experimental.api.*
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.*

object ScriptingUtil {

    private fun runScript(script: File): ResultWithDiagnostics<EvaluationResult> {
        val compilationConfiguration = CEScriptConfiguration()
        return BasicJvmScriptingHost().eval(script.toScriptSource(), compilationConfiguration, null)
    }

    fun runScripts() {
        if (MainConfig.get().kotlinScripting?.applyKotlinScripts == false)
            return
        File(KOTLIN_SCRIPT_PATH).walk().forEach { file ->
            val result = runScript(file)
            result.reports.forEach {
                if (it.severity > ScriptDiagnostic.Severity.DEBUG) {
                    log(" : {it.message}" + if (it.exception == null) "" else ": ${it.exception}")
                }
            }
        }
    }
}