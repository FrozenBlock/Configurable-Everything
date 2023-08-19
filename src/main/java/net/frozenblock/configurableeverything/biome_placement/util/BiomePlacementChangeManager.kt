package net.frozenblock.configurableeverything.biome_placement.util

import com.mojang.serialization.JsonOps
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener
import net.frozenblock.configurableeverything.biome_placement.util.BiomePlacementChangeManager.BiomePlacementChangeLoader
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
internal class BiomePlacementChangeManager : SimpleResourceReloadListener<BiomePlacementChangeLoader> {
    companion object {
        private val LOGGER = LoggerFactory.getLogger("Configurable Everything Biome Placement Change Manager")
        private const val DIRECTORY = "biome_placement"

        @JvmField
        val INSTANCE = BiomePlacementChangeManager()

        @JvmStatic
        fun getPath(changeId: ResourceLocation): ResourceLocation {
            return ResourceLocation(changeId.namespace, DIRECTORY + "/" + changeId.path + ".json")
        }
    }

    private var changes: MutableMap<ResourceLocation?, BiomePlacementChange?>? = null
    private val queuedChanges: MutableMap<ResourceLocation?, BiomePlacementChange?> = Object2ObjectOpenHashMap()

    fun getChanges(): MutableList<BiomePlacementChange?>? {
        return changes?.values?.stream()?.toList()
    }

    fun getChange(id: ResourceLocation?): BiomePlacementChange? {
        return changes?.get(id)
    }

    /**
     * Adds a biome placement change with the specified [ResourceLocation]
     */
    fun addChange(
        key: ResourceLocation?,
        addedBiomes: List<DimensionBiomeList?>?,
        removedBiomes: List<DimensionBiomeKeyList?>?
    ) {
        addChange(key, BiomePlacementChange(addedBiomes, removedBiomes))
    }

    /**
     * Adds a biome placement change with the specified [ResourceLocation]
     */
    fun addChange(key: ResourceLocation?, change: BiomePlacementChange?) {
        if (key != null && change != null && change.addedBiomes != null && change.removedBiomes != null) {
            queuedChanges[key] = change
        }
    }

    override fun load(
        manager: ResourceManager?,
        profiler: ProfilerFiller?,
        executor: Executor?
    ): CompletableFuture<BiomePlacementChangeLoader> {
        return CompletableFuture.supplyAsync({ BiomePlacementChangeLoader(manager, profiler) }, executor)
    }

    override fun apply(
        prepared: BiomePlacementChangeLoader?,
        manager: ResourceManager?,
        profiler: ProfilerFiller?,
        executor: Executor?
    ): CompletableFuture<Void>? {
        changes = prepared?.changes
        changes!!.putAll(queuedChanges)
        return CompletableFuture.runAsync {}
    }

    override fun getFabricId(): ResourceLocation {
        return id("biome_placement_change_reloader")
    }

    class BiomePlacementChangeLoader(private val manager: ResourceManager?, private val profiler: ProfilerFiller?) {
        val changes: MutableMap<ResourceLocation?, BiomePlacementChange?> = Object2ObjectOpenHashMap()

        init {
            if (MainConfig.get().datapack.biome_placement) {
                loadPlacementChanges()
            }
        }

        private fun loadPlacementChanges() {
            profiler?.push("Load Biome Placement Changes")
            val resources = manager?.listResources(DIRECTORY) { id: ResourceLocation -> id.path.endsWith(".json") }
            val entrySet: Set<Map.Entry<ResourceLocation, Resource>>? = resources?.entries
            entrySet?.forEach { (key, value) ->
                addPlacementChange(key, value)
            }
            profiler?.pop()
        }

        private fun addPlacementChange(id: ResourceLocation, resource: Resource) {
            val reader: BufferedReader? = try {
                resource.openAsReader()
            } catch (e: IOException) {
                LOGGER.error(String.format("Unable to open BufferedReader for id %s", id), e)
                return
            }
            val json = reader?.let { GsonHelper.parse(it) }
            val result = BiomePlacementChange.CODEC.decode(JsonOps.INSTANCE, json)
            if (result.error().isPresent) {
                LOGGER.error(
                    String.format(
                        "Unable to parse biome placement change file %s. \nReason: %s",
                        id,
                        result.error().get().message()
                    )
                )
                return
            }
            val changeId = ResourceLocation(id.namespace, id.path.substring((DIRECTORY + "/").length))
            changes[changeId] = result.result().orElseThrow().first
        }
    }
}
