package net.frozenblock.configurableeverything.scripting.util

import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.*
import java.io.File
import java.nio.file.Path
import kotlin.script.experimental.api.EvaluationResult
import kotlin.script.experimental.api.ResultWithDiagnostics
import kotlin.script.experimental.api.ScriptDiagnostic
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.*
import kotlin.script.experimental.jvm.impl.*
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
        if (ENABLE_EXPERIMENTAL_FEATURES) {
            val compiledScript: KJvmCompiledScript = JvmScriptCompiler()(
                script.toScriptSource(),
                compilationConfiguration
            ) as? KJvmCompiledScript ?: error("Compiled script is not java??")
            val file = File(".$MOD_ID/original_scripts/${script.getName()}.jar")
            compiledScript.saveToJar(file)
            val remappedFile: File = remapScript(file)
            val remappedScript: CompiledScript = remappedFile.loadScriptFromJar() ?: error("Remapped script is null")
            return BasicJvmScriptEvaluator()(remappedScript, evaluationConfiguration)
        }
        return BasicJvmScriptingHost().eval(script.toScriptSource(), compilationConfiguration, evaluationConfiguration)
    }

    fun runScripts() {
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
                val result = runScript(file)
                result.reports.forEach {
                    val message = " : ${it.message}" + if (it.exception == null) "" else ": ${it.exception}"
                    when (it.severity) {
                        ScriptDiagnostic.Severity.DEBUG -> logDebug(message)
                        ScriptDiagnostic.Severity.INFO -> log(message)
                        ScriptDiagnostic.Severity.WARNING -> logWarn(message)
                        ScriptDiagnostic.Severity.ERROR -> logError(message)
                        ScriptDiagnostic.Severity.FATAL -> logError(message)
                        else -> logError(message)
                    }
                    it.exception?.printStackTrace()
                }
            } catch (e: Exception) {
                logError("Error while running script $file", e)
            }
        }
    }
}
