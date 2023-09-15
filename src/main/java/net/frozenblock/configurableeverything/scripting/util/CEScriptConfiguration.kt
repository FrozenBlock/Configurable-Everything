package net.frozenblock.configureableeverything.scripting.util

import kotlinx.coroutines.runBlocking
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.StartTick
import kotlin.script.experimental.api.*
import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.jvm.*
import net.frozenblock.configurableeverything.util.*
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import java.util.Optional
import kotlin.script.experimental.dependencies.*

private val DEFAULT_IMPORTS: List<String> = listOf(
    "java.util.*",
    "kotlinx.coroutines.*",
    "net.frozenblock.configurableeverything.util.*",
    "net.minecraft.core.registries.*",
    "net.minecraft.core.registries.Registries.*",
    "net.minecraft.resources.*",
    "net.minecraft.server.*",
    "net.minecraft.world.level.block.*",
    "net.minecraft.world.level.block.state.*",
    "net.minecraft.world.level.block.state.BlockBehaviour.Properties"
)

@KotlinScript(
    fileExtension = KOTLIN_SCRIPT_EXTENSION,
    compilationConfiguration = CEScriptConfiguration::class,
    evaluationConfiguration = CEScriptEvaluationConfig::class,
)
abstract class CEScript {
    fun <T> register(registry: ResourceKey<Registry<T>>, path: ResourceLocation, value: T): T? {
        val realRegistry: Optional<Registry<T>>? = BuiltInRegistries.REGISTRY.getOptional(registry.location()) as? Optional<Registry<T>>
        if (realRegistry != null && realRegistry.isPresent) {
            return Registry.register(realRegistry.get(), path, value)
        } else {
            error("Registry ${registry.location()} does not exist")
        }
        return null
    }

    fun runEachTick(tickFun: (MinecraftServer) -> Unit) {
        ServerTickEvents.START_SERVER_TICK.register(tickFun::invoke)
    }

    @Environment(EnvType.CLIENT)
    fun runEachClientTick(tickFun: (MinecraftServer) -> Unit) {
        ClientTickEvents.START_CLIENT_TICK.register { tickFun.run {  } }
    }
}

object CEScriptConfiguration : ScriptCompilationConfiguration({
    defaultImports(DEFAULT_IMPORTS)
    baseClass(CEScript::class)
    ide {
        acceptedLocations(ScriptAcceptedLocation.Everywhere)
    }
    jvm {
        dependenciesFromCurrentContext(wholeClasspath = true)
        dependenciesFromClassContext(CEScriptConfiguration::class)
    }
    compilerOptions(listOf("-jvm-target", "17"))
    refineConfiguration {
        //onAnnotations(DependsOn::class, Repository::class, handler = ::configureMavenDepsOnAnnotations)
    }
}) {
    private fun readResolve(): Any = CEScriptConfiguration
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
}
*/
