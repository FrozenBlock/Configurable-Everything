package net.frozenblock.configureableeverything.scripting.util

import kotlinx.coroutines.runBlocking
import kotlin.script.experimental.*
import kotlin.script.experimental.api.*
import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.host.FileBasedScriptSource
import kotlin.script.experimental.host.FileScriptSource
import kotlin.script.experimental.jvm.*
import net.frozenblock.configurableeverything.util.*

abstract class CEScript

private val DEFAULT_IMPORTS: List<String> = listOf(
    "java.util.*",
    "kotlinx.coroutines.*"
    "net.frozenblock.configurableeverything.util.*",
)

object CEScriptConfiguration : ScriptCompilationConfiguration({
    defaultImports(DEFAULT_IMPORTS, DependsOn::class, Repository::class)
    ide {
        acceptedLocations(ScriptAcceptedLocation.Everywhere)
    }
    compilerOptions(listOf("-jvm-target", "17"))
})

internal class CEScriptEvaluationConfig : ScriptEvaluationConfiguration({
    jvm {
        baseClassLoader(javaClass.classLoader)
        loadDependencies(true)
        scriptsInstancesSharing(true)
    },
    refineConfiguration {
        onAnnotations(DependsOn::class, Repository::class, handler = ::configureMavenDepsOnAnnotations)
    }
})

@KotlinScript(
    fileExtension = KOTLIN_SCRIPT_EXTENSION,
    compilationConfiguration = CEScriptConfiguration::class,
    evaluationConfiguration = CEScriptEvaluationConfig::class,
)
abstract class CEScript()

private val resolver = CompoundDependenciesResolver(FileSystemDependenciesResolver(), MavenDependenciesResolver())

fun configureMavenDepsOnAnnotations(context: ScriptConfigurationRefinementContext): ResultWithDiagnostics<ScriptCompilationConfiguration> {
    val annotations = context.collectedData?.get(ScriptCollectedData.collectedAnnotations)?.takeIf { it.isNotEmpty() }
        ?: return context.compilationConfiguration.asSuccess()
    return runBlocking {
        resolver.resolveFromScriptSourceAnnotations(annotations)
    }.onSuccess {
        context.compilationConfiguration.with {
            dependencies.append(JvmDependency(it))
        }.asSuccess()
    }
}