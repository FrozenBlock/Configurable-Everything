package net.frozenblock.configurableeverything.scripting.util

import net.fabricmc.api.EnvType
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.loader.api.FabricLoader
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.registry.util.DynamicRegistryAddition
import net.frozenblock.configurableeverything.util.KOTLIN_SCRIPT_EXTENSION
import net.frozenblock.configurableeverything.util.logError
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import org.quiltmc.qsl.frozenblock.core.registry.api.event.RegistryEvents
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
    @Suppress("unchecked")
    fun <T> register(registry: ResourceKey<Registry<T>>, path: ResourceLocation, value: T & Any): T? {
        val realRegistry: Optional<Registry<T>>? = BuiltInRegistries.REGISTRY.getOptional(registry.location()) as? Optional<Registry<T>>
        if (realRegistry != null && realRegistry.isPresent) {
            return Registry.register(realRegistry.get(), path, value)
        }
        logError("Registry ${registry.location()} does not exist in built in registries.")
        return null
    }

    fun <T : Any> registerDynamic(registry: ResourceKey<Registry<T>>, path: ResourceLocation, value: T) {
        RegistryEvents.DYNAMIC_REGISTRY_SETUP.register { setupContext ->
            DynamicRegistryAddition(registry, path, value).register(setupContext)
        }
    }

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
