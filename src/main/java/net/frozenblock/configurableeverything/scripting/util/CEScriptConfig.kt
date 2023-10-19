package net.frozenblock.configurableeverything.scripting.util

import net.fabricmc.api.EnvType
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.loader.api.FabricLoader
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.KOTLIN_SCRIPT_EXTENSION
import net.frozenblock.configurableeverything.util.logError
import java.util.*
import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.api.*
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvm.loadDependencies

@KotlinScript(
    fileExtension = KOTLIN_SCRIPT_EXTENSION,
    compilationConfiguration = CEScriptCompilationConfig::class,
    evaluationConfiguration = CEScriptEvaluationConfig::class,
)
// dont use environment annotations anywhere
abstract class CEScript {

    fun clientOnly(`fun`: () -> Unit) {
        if (FabricLoader.getInstance().environmentType == EnvType.CLIENT)
            `fun`.invoke()
    }

    fun runEachTick(tickFun: () -> Unit) {
        ServerTickEvents.START_SERVER_TICK.register { tickFun.invoke() }
    }
}

object CEScriptCompilationConfig : ScriptCompilationConfiguration({
    val defaultImports = MainConfig.get().kotlinScripting?.defaultImports ?: MainConfig.INSTANCE.defaultInstance().kotlinScripting!!.defaultImports!!
    defaultImports(defaultImports)
    baseClass(CEScript::class)
    ide {
        acceptedLocations(ScriptAcceptedLocation.Everywhere)
    }
    jvm {
        dependenciesFromCurrentContext(wholeClasspath = true)
    }

    compilerOptions(listOf("-jvm-target", "17"))
    compilerOptions.append("-Xadd-modules=ALL-MODULE-PATH")
    refineConfiguration {
        //onAnnotations(DependsOn::class, Repository::class, handler = ::configureMavenDepsOnAnnotations)
    }
}) {
    private fun readResolve(): Any = CEScriptCompilationConfig
}

object CEScriptEvaluationConfig : ScriptEvaluationConfiguration({
    jvm {
        loadDependencies(true)
        scriptsInstancesSharing(true)
    }
}) {
    private fun readResolve(): Any = CEScriptEvaluationConfig
}

//private val resolver = CompoundDependenciesResolver(FileSystemDependenciesResolver(), MavenDependenciesResolver())

/*fun configureMavenDepsOnAnnotations(context: ScriptConfigurationRefinementContext): ResultWithDiagnostics<ScriptCompilationConfiguration> {
    val annotations = context.collectedData?.get(ScriptCollectedData.collectedAnnotations)?.takeIf { it.isNotEmpty() }
        ?: return context.compilationConfiguration.asSuccess()
    return runBlocking {
        resolver.resolveFromScriptSourceAnnotations(annotations)
    }.onSuccess {
        context.compilationConfiguration.with {
            dependencies.append(JvmDependency(it))
        }.asSuccess()
    }
}*/
