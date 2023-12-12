package net.frozenblock.configurableeverything.scripting.util

import kotlinx.coroutines.runBlocking
import net.fabricmc.api.EnvType
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.ObjectShare
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.config.ScriptingConfig
import net.frozenblock.configurableeverything.util.*
import net.frozenblock.configurableeverything.util.ENABLE_EXPERIMENTAL_FEATURES
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URLClassLoader
import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.api.*
import kotlin.script.experimental.dependencies.*
import kotlin.script.experimental.dependencies.maven.MavenDependenciesResolver
import kotlin.script.experimental.jvm.*

@KotlinScript(
    fileExtension = KOTLIN_SCRIPT_EXTENSION,
    compilationConfiguration = CEScriptCompilationConfig::class,
    evaluationConfiguration = CEScriptEvaluationConfig::class,
)
// dont use environment annotations anywhere
abstract class CEScript {
    companion object {
        internal var POST_RUN_FUNS: MutableMap<Int, () -> Unit>? = mutableMapOf()
    }

    /**
     * The name of the script file.
     */
    val scriptName: String = this::class.java.simpleName.let { name -> name.substring(0, name.length - 5) }
    val logger: Logger = LoggerFactory.getLogger("CE Script: $scriptName")
    val objectShare: ObjectShare = FabricLoader.getInstance().objectShare

    fun clientOnly(`fun`: () -> Unit) {
        if (FabricLoader.getInstance().environmentType == EnvType.CLIENT)
            `fun`()
    }

    /**
     * @since 1.1
     */
    fun runLate(priority: Int, `fun`: () -> Unit) {
        experimental { POST_RUN_FUNS!![priority] = `fun` }
    }

    fun runEachTick(tickFun: () -> Unit) {
        // TODO: add to client tick events if a client script
        ServerTickEvents.START_SERVER_TICK.register { tickFun() }
    }

    fun println(message: Any?) {
        log(message)
    }

    fun log(message: Any?) {
        logger.info(message.toString())
    }

    fun logWarning(message: Any?) {
        logger.warn(message.toString())
    }

    fun logError(message: Any?, e: Throwable? = null) {
        logger.error(message.toString(), e)
    }
}

object CEScriptCompilationConfig : ScriptCompilationConfiguration({
    val defaultImports = ScriptingConfig.get().defaultImports ?: ScriptingConfig.defaultInstance().defaultImports!!
    defaultImports(defaultImports)
    if (ENABLE_EXPERIMENTAL_FEATURES) defaultImports(DependsOn::class, Repository::class)
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
            onAnnotations(DependsOn::class, Repository::class, handler = ::configureMavenDepsOnAnnotations)
    }
})

object CEScriptEvaluationConfig : ScriptEvaluationConfiguration({
    jvm {
        loadDependencies(true)
        scriptsInstancesSharing(true)
    }
})

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
