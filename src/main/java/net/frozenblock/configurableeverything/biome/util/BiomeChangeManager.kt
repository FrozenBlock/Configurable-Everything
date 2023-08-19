package net.frozenblock.configurableeverything.biome.util

import com.google.gson.JsonObject
import com.mojang.serialization.JsonOps
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener
import net.frozenblock.configurableeverything.biome.util.BiomeChangeManager.BiomeChangeLoader
import net.frozenblock.configurableeverything.biome.util.BiomeConfigUtil.applyModifications
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.id
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.Resource
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.util.GsonHelper
import net.minecraft.util.profiling.ProfilerFiller
import org.jetbrains.annotations.ApiStatus
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.IOException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

@ApiStatus.Internal
internal class BiomeChangeManager : SimpleResourceReloadListener<BiomeChangeLoader> {
    private var changes: MutableMap<ResourceLocation?, BiomeChange?>? = null
    private val queuedChanges: MutableMap<ResourceLocation?, BiomeChange?> = Object2ObjectOpenHashMap()
    fun getChanges(): MutableList<BiomeChange?>? {
        return changes?.values?.stream()?.toList()
    }

    fun getChange(id: ResourceLocation?): BiomeChange? {
        return changes?.get(id)
    }

    /**
     * Adds a biome change with the specified [ResourceLocation]
     */
    fun addChange(
        key: ResourceLocation?,
        addedFeatures: List<BiomePlacedFeatureList?>?,
        removedFeatures: List<BiomePlacedFeatureList?>?,
        replacedFeatures: List<BiomePlacedFeatureReplacementList?>?,
        musicReplacements: List<BiomeMusic?>?
    ) {
        addChange(key, BiomeChange(addedFeatures, removedFeatures, replacedFeatures, musicReplacements))
    }

    /**
     * Adds a biome change with the specified [ResourceLocation]
     */
    fun addChange(key: ResourceLocation?, change: BiomeChange?) {
        if (key != null && change != null
            && change.addedFeatures != null && change.removedFeatures != null && change.replacedFeatures != null && change.musicReplacements != null) {
                queuedChanges[key] = change
        }
    }

    override fun load(
        manager: ResourceManager?,
        profiler: ProfilerFiller?,
        executor: Executor?
    ): CompletableFuture<BiomeChangeLoader> {
        return CompletableFuture.supplyAsync({ BiomeChangeLoader(manager, profiler) }, executor)
    }

    override fun apply(
        prepared: BiomeChangeLoader?,
        manager: ResourceManager?,
        profiler: ProfilerFiller?,
        executor: Executor?
    ): CompletableFuture<Void> {
        changes = prepared?.changes
        changes!!.putAll(queuedChanges)
        return CompletableFuture.runAsync {
            applyModifications(
                changes?.values
            )
        }
    }

    override fun getFabricId(): ResourceLocation {
        return id("biome_change_reloader")
    }

    class BiomeChangeLoader(private val manager: ResourceManager?, private val profiler: ProfilerFiller?) {
        val changes: MutableMap<ResourceLocation?, BiomeChange?> = Object2ObjectOpenHashMap()

        init {
            if (MainConfig.get().datapack.biome) {
                loadChanges()
            }
        }

        private fun loadChanges() {
            profiler?.push("Load Biome Changes")
            val resources = manager?.listResources(DIRECTORY) { id: ResourceLocation -> id.path.endsWith(".json") }
            val entrySet: Set<Map.Entry<ResourceLocation?, Resource?>>? = resources?.entries
            entrySet?.forEach { (key, value) ->
                if (key != null && value != null) {
                    addBiomeChange(key, value)
                }
            }
            profiler?.pop()
        }

        private fun addBiomeChange(id: ResourceLocation, resource: Resource) {
            val reader: BufferedReader? = try {
                resource.openAsReader()
            } catch (e: IOException) {
                LOGGER.error(String.format("Unable to open BufferedReader for id %s", id), e)
                return
            }
            val json: JsonObject? = reader?.let { GsonHelper.parse(it) }
            val result = BiomeChange.CODEC.decode(JsonOps.INSTANCE, json)
            if (result.error().isPresent) {
                LOGGER.error(
                    String.format(
                        "Unable to parse biome change file %s. \nReason: %s",
                        id,
                        result.error().get().message()
                    )
                )
                return
            }
            val changeId = ResourceLocation(id.namespace, id.path.substring(("$DIRECTORY/").length))
            changes[changeId] = result.result().orElseThrow().first
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger("Configurable Everything Biome Change Manager")
        private const val DIRECTORY = "biome_modifications"
        val INSTANCE = BiomeChangeManager()
        fun getPath(changeId: ResourceLocation?): ResourceLocation? {
            return changeId?.namespace?.let { ResourceLocation(it, DIRECTORY + "/" + changeId.path + ".json") }
        }
    }
}
