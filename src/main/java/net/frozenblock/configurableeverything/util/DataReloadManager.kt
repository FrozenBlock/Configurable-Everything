package net.frozenblock.configurableeverything.util

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.JsonOps
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener
import net.frozenblock.configurableeverything.util.DataReloadManager.DataReloader
import net.frozenblock.lib.config.api.instance.ConfigSerialization
import net.frozenblock.lib.config.api.instance.json.JanksonOps
import net.frozenblock.lib.config.api.instance.json.JsonType
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.Resource
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.util.GsonHelper
import net.minecraft.util.profiling.ProfilerFiller
import org.slf4j.LoggerFactory
import java.io.IOException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

abstract class DataReloadManager<T : Any>(
    private val id: String,
    name: String,
    private val directory: String,
    private val profilerMessage: String,
    private val fileTypeName: String,
    private val codec: Codec<T>
) : SimpleResourceReloadListener<DataReloader<T>> {

    private val logger = LoggerFactory.getLogger("Configurable Everything $name")

    protected abstract val shouldApply: Boolean

    protected abstract fun apply(values: Collection<T>)

    fun getPath(dataId: ResourceLocation, jsonType: JsonType): ResourceLocation =
        ResourceLocation.fromNamespaceAndPath(dataId.namespace, "$directory/${dataId.path}.${jsonType.serializedName}")

    protected var data: MutableMap<ResourceLocation, T>? = null
    private val queuedData: MutableMap<ResourceLocation, T> = Object2ObjectOpenHashMap()

    inline val values get() = `access$data`?.values?.toMutableList()

    @Suppress("NOTHING_TO_INLINE")
    internal inline fun getValue(id: ResourceLocation?): T? = data?.get(id)

    /**
     * Adds a value with the specified [ResourceLocation]
     */
    internal fun add(key: ResourceLocation, value: T) {
        queuedData[key] = value
    }

    override fun load(
        manager: ResourceManager?,
        profiler: ProfilerFiller?,
        executor: Executor?
    ): CompletableFuture<DataReloader<T>> {
        return CompletableFuture.supplyAsync({ DataReloader(this, manager, profiler) }, executor)
    }

    override fun apply(
        prepared: DataReloader<T>?,
        manager: ResourceManager?,
        profiler: ProfilerFiller?,
        executor: Executor?
    ): CompletableFuture<Void> {
        data = prepared?.data
        data?.putAll(queuedData)
        return CompletableFuture.runAsync {
            data?.values?.also {
                apply(it)
            }
        }
    }

    override fun getFabricId(): ResourceLocation
        = id(this.id)

    class DataReloader<T : Any>(private val reloadManager: DataReloadManager<T>, private val manager: ResourceManager?, private val profiler: ProfilerFiller?) {
        val data: MutableMap<ResourceLocation, T> = Object2ObjectOpenHashMap()

        init {
            if (reloadManager.shouldApply) {
                loadData()
            }
        }

        private fun loadData() = runBlocking {
            profiler?.push(reloadManager.profilerMessage)

            // basically, this is a coroutine that loads the json and json5 files in parallel
            // should speed it up a bit
            val loadJson = launch {
                loadData(false)
            }
            val loadJson5 = launch {
                loadData(true)
            }

            // make sure they finish
            loadJson.join()
            loadJson5.join()

            profiler?.pop()
        }

        private suspend fun loadData(json5: Boolean) = coroutineScope {
            val resources = manager?.listResources(reloadManager.directory) { id: ResourceLocation -> id.path.endsWith(if (json5) ".json5" else ".json") }
            val entrySet: Set<Map.Entry<ResourceLocation?, Resource?>>? = resources?.entries
            entrySet?.forEach { (key, value) ->
                launch {
                    if (key != null && value != null) {
                        addValue(key, value, json5)
                    }
                }
            }
        }

        private fun addValue(id: ResourceLocation, resource: Resource, isJson5: Boolean) {
            try {
                resource.openAsReader()
            } catch (e: IOException) {
                reloadManager.logger.error("Unable to open BufferedReader for id $id", e)
                return
            }.use { reader ->
                val result: DataResult<out Pair<T, *>> = if (isJson5) {
                    val json5 = reader.let { ConfigSerialization.createJankson("").load(it.readText()) }
                    reloadManager.codec.decode(JanksonOps.INSTANCE, json5)
                } else {
                    val json = reader.let { GsonHelper.parse(it) }
                    reloadManager.codec.decode(JsonOps.INSTANCE, json)
                }

                if (result.error().isPresent) {
                    reloadManager.logger.error("Unable to parse ${reloadManager.fileTypeName} file $id. \nReason: ${result.error().get().message()}")
                    return
                }
                val dataId = ResourceLocation.fromNamespaceAndPath(id.namespace, id.path.substring(("${reloadManager.directory}/").length))
                data[dataId] = result.result().orElseThrow().first
            }
        }
    }

    @PublishedApi
    internal val `access$data`: MutableMap<ResourceLocation, T>? get() = data
}
