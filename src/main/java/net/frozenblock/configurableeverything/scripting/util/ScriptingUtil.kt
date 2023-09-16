package net.frozenblock.configurableeverything.scripting.util

/*import kotlin.script.experimental.api.*
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.*
import java.io.File
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost

object ScriptingUtil {

    private fun runScript(script: File): ResultWithDiagnostics<EvaluationResult> {
        val compilationConfiguration = CEScriptConfiguration
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
                if (it.severity > ScriptDiagnostic.Severity.DEBUG) {
                    error(" : ${it.message}" + if (it.exception == null) "" else ": ${it.exception}")
                }
            }
        }
    }
}
*/
