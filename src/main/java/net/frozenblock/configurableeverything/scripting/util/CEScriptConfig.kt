@file:Suppress("NOTHING_TO_INLINE")

package net.frozenblock.configurableeverything.scripting.util

import kotlinx.coroutines.runBlocking
import net.fabricmc.api.EnvType
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.ObjectShare
import net.frozenblock.configurableeverything.config.ScriptingConfig
import net.frozenblock.configurableeverything.util.*
import net.frozenblock.configurableeverything.util.ENABLE_EXPERIMENTAL_FEATURES
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.io.path.absolutePathString
import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.api.*
import kotlin.script.experimental.dependencies.*
import kotlin.script.experimental.dependencies.maven.MavenDependenciesResolver
import kotlin.script.experimental.host.FileBasedScriptSource
import kotlin.script.experimental.host.FileScriptSource
import kotlin.script.experimental.impl.internalScriptingRunSuspend
import kotlin.script.experimental.jvm.*
import kotlin.script.experimental.util.filterByAnnotationType

@KotlinScript(
    fileExtension = KOTLIN_SCRIPT_EXTENSION,
    compilationConfiguration = CEScriptCompilationConfig::class,
    evaluationConfiguration = CEScriptEvaluationConfig::class,
)
// dont use environment annotations anywhere
abstract class CEScript {
    companion object {
        @PublishedApi
        internal var POST_RUN_FUNS: MutableMap<Int, () -> Unit>? = mutableMapOf()
    }

    /**
     * The name of the script file.
     */
    @JvmField
    val scriptName: String = this::class.java.simpleName.let { name -> name.substring(0, name.length - 5) }
    @JvmField
    val logger: Logger = LoggerFactory.getLogger("CE Script: $scriptName")
    @JvmField
    val objectShare: ObjectShare = FabricLoader.getInstance().objectShare

    inline fun clientOnly(crossinline `fun`: () -> Unit) {
        if (FabricLoader.getInstance().environmentType == EnvType.CLIENT)
            `fun`()
    }

    /**
     * @since 1.1
     */
    inline fun runLate(priority: Int, noinline `fun`: () -> Unit)
        = experimental { POST_RUN_FUNS!![priority] = `fun` }

    inline fun runEachTick(crossinline tickFun: () -> Unit) {
        // TODO: add to client tick events if a client script
        ServerTickEvents.START_SERVER_TICK.register { tickFun() }
    }

    inline fun log(message: Any?) = logger.info(message.toString())

    inline fun logWarning(message: Any?) = logger.warn(message.toString())

    inline fun logError(message: Any?, e: Throwable?) = logger.error(message.toString(), e)
}

object CEScriptCompilationConfig : ScriptCompilationConfiguration({
    val defaultImports = ScriptingConfig.get().defaultImports ?: ScriptingConfig.defaultInstance().defaultImports!!
    defaultImports(defaultImports)
    if (ENABLE_EXPERIMENTAL_FEATURES)
        defaultImports(
            DependsOn::class,
            Repository::class,
            Import::class,
            CompilerOptions::class,
        )
    baseClass(CEScript::class)
    ide {
        acceptedLocations(ScriptAcceptedLocation.Everywhere)
    }
    jvm {
        // the dependenciesFromCurrentContext helper function extracts the classpath from current thread classloader
        // and take jars with mentioned names to the compilation classpath via `dependencies` key.
        ifExperimental {
            // Adds the remapped Minecraft and mod jars to the classpath
            if (ScriptingConfig.get().remapping == true)
                updateClasspath(REMAPPED_SOURCES_CACHE.asFileList!!)
        }
        dependenciesFromCurrentContext(wholeClasspath = true)
    }

    compilerOptions(listOf(
        "-jvm-target", "17",
        //"-language-version", "2.0",
    ))
    compilerOptions.append("-Xadd-modules=ALL-MODULE-PATH")

    refineConfiguration {
        // the callback called when any of the listed file-level annotations are encountered in the compiled script
        // the processing is defined by the `handler`, that may return refined configuration depending on the annotations
        if (ENABLE_EXPERIMENTAL_FEATURES)
            onAnnotations(
                DependsOn::class,
                Repository::class,
                Import::class,
                CompilerOptions::class,
                handler = ::configureDepsOnAnnotations
            )
    }
}) {
    // used for serialization for some reason
    private fun readResolve(): Any = CEScriptCompilationConfig
}

object CEScriptEvaluationConfig : ScriptEvaluationConfiguration({
    jvm {
        loadDependencies(true)
        scriptsInstancesSharing(true)
    }
}) {
    // used for serialization for some reason
    private fun readResolve(): Any = CEScriptEvaluationConfig
}

private val resolver = CompoundDependenciesResolver(FileSystemDependenciesResolver(), MavenDependenciesResolver())

fun configureDepsOnAnnotations(context: ScriptConfigurationRefinementContext): ResultWithDiagnostics<ScriptCompilationConfiguration> {
    val diagnostics = arrayListOf<ScriptDiagnostic>()

    val annotations = context.collectedData?.get(ScriptCollectedData.collectedAnnotations)?.takeIf { it.isNotEmpty() }
        ?: return context.compilationConfiguration.asSuccess()

    val scriptBaseDir = (context.script as? FileBasedScriptSource)?.file?.parentFile
    val importedSources = linkedMapOf<String, Pair<File, String>>()
    var hasImportErrors = false
    annotations.filterByAnnotationType<Import>().forEach { scriptAnnotation ->
        scriptAnnotation.annotation.paths.forEach { sourceName ->
            val file = (scriptBaseDir?.resolve(sourceName) ?: File(sourceName)).normalize()
            val keyPath = file.absolutePath
            val prevImport = importedSources.put(keyPath, file to sourceName)
            if (prevImport != null) {
                diagnostics.add(
                    ScriptDiagnostic(
                        ScriptDiagnostic.unspecifiedError, "Duplicate imports: \"${prevImport.second}\" and \"$sourceName\"",
                        sourcePath = context.script.locationId, location = scriptAnnotation.location?.locationInText
                    )
                )
                hasImportErrors = true
            }
        }
    }
    if (hasImportErrors) return ResultWithDiagnostics.Failure(diagnostics)

    val compileOptions = annotations.filterByAnnotationType<CompilerOptions>().flatMap {
        it.annotation.options.toList()
    }

    val resolveResult = try {
        @Suppress("DEPRECATION_ERROR")
        internalScriptingRunSuspend {
            resolver.resolveFromScriptSourceAnnotations(annotations.filter { it.annotation is DependsOn || it.annotation is Repository })
        }
    } catch (e: Throwable) {
        diagnostics.add(e.asDiagnostics(path = context.script.locationId))
        ResultWithDiagnostics.Failure(diagnostics)
    }

    return resolveResult.onSuccess { resolvedClasspath ->
        ScriptCompilationConfiguration(context.compilationConfiguration) {
            updateClasspath(resolvedClasspath)
            if (importedSources.isNotEmpty()) {
                importScripts.append(importedSources.values.map { FileScriptSource(it.first) })
            }
            if (compileOptions.isNotEmpty()) compilerOptions.append(compileOptions)
        }.asSuccess()
    }
}
