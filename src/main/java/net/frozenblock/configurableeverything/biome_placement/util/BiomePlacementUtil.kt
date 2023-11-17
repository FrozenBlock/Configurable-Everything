package net.frozenblock.configurableeverything.biome_placement.util

import com.mojang.datafixers.util.Either
import com.mojang.datafixers.util.Pair
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.frozenblock.configurableeverything.config.BiomePlacementConfig
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.id
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.server.packs.PackType
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.Climate
import net.minecraft.world.level.biome.Climate.ParameterPoint
import net.minecraft.world.level.biome.MultiNoiseBiomeSource
import net.minecraft.world.level.dimension.DimensionType
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator

object BiomePlacementUtil {

    fun init() {
        val config = BiomePlacementConfig.get()
        if (MainConfig.get().biome_placement == true) {
            val addedBiomes = config.addedBiomes?.value
            val removedBiomes = config.removedBiomes?.value
            if (addedBiomes != null && removedBiomes != null) {
                val placementChange = BiomePlacementChange(addedBiomes, removedBiomes)
                BiomePlacementChanges.addChange(id("config"), placementChange)
            }

            val resourceLoader = ResourceManagerHelper.get(PackType.SERVER_DATA)
            resourceLoader?.registerReloadListener(BiomePlacementChangeManager.INSTANCE)
        }
    }

    @JvmStatic
    fun serverInit(registryAccess: RegistryAccess) = runBlocking {
        if (MainConfig.get().biome_placement != true) return@runBlocking
        val biomeRegistry = registryAccess.lookupOrThrow(Registries.BIOME)
        val levelStemRegistry = registryAccess.registryOrThrow(Registries.LEVEL_STEM)

        for ((_, stem) in levelStemRegistry.entrySet()) { launch {
            val dimension = stem.type().unwrapKey().orElseThrow()
            val chunkGenerator = stem.generator()
            if (chunkGenerator !is NoiseBasedChunkGenerator) return@launch

            val biomeSource = chunkGenerator.getBiomeSource()
            if (biomeSource !is MultiNoiseBiomeSource) return@launch

            val extended = biomeSource as? BiomeSourceExtension
            val parameters: Climate.ParameterList<Holder<Biome>>? = biomeSource.parameters()
            (parameters as? ParameterListExtension)?.updateBiomesList(registryAccess, dimension)

            // remove biomes first to allow replacing biome parameters
            val removedBiomeHolders: MutableList<Holder<Biome>?> = ArrayList()
            removedBiomeHolders.addAll(biomeRemovals(registryAccess, dimension).filterNotNull().map {
                biomeRegistry.getOrThrow(it)
            })

            val addedBiomes: List<Pair<ParameterPoint?, Holder<Biome>>> = biomeAdditions(biomeRegistry, dimension)
            val addedBiomeHolders: MutableList<Holder<Biome>?> = ArrayList()
            addedBiomeHolders.addAll(addedBiomes.map { it.second })

            extended?.updateBiomesList(addedBiomeHolders, removedBiomeHolders, registryAccess)

        } }
    }

    @JvmStatic
    fun biomeAdditionsJvm(
        registryAccess: HolderGetter<Biome>?,
        dimension: ResourceKey<DimensionType>?
    ) = runBlocking {
        biomeAdditions(registryAccess, dimension)
    }

    suspend fun biomeAdditions(
        registryAccess: HolderGetter<Biome>?,
        dimension: ResourceKey<DimensionType>?
    ): List<Pair<ParameterPoint?, Holder<Biome>>> = coroutineScope {
        val biomeAdditions: MutableList<Pair<ParameterPoint?, Holder<Biome>>> = ArrayList()
        val changes: List<BiomePlacementChange?>? = BiomePlacementChanges.changes
        val addedBiomes: MutableList<DimensionBiomeList?> = ArrayList()
        changes?.forEach { launch {
            it?.addedBiomes?.apply {
                addedBiomes.addAll(this)
            }
        } }

        val dimensionBiomes = addedBiomes.stream().filter { list: DimensionBiomeList? ->
            list?.dimension == dimension
        }.toList()
        for (list in dimensionBiomes) { launch {
            list?.biomes?.forEach { parameters -> launch {
                parameters?.biome?.apply {
                    val location = this
                    parameters.parameters?.toImmutable()?.apply {
                        biomeAdditions.add(
                            Pair.of(
                                this,
                                registryAccess?.getOrThrow(ResourceKey.create(Registries.BIOME, location))
                            )
                        )
                    }
                }
            } }
        } }
        return@coroutineScope biomeAdditions
    }

    @JvmStatic
    fun biomeRemovalsJvm(
        registryAccess: RegistryAccess?,
        dimension: ResourceKey<DimensionType>?
    ): List<ResourceKey<Biome>?> = runBlocking {
        biomeRemovals(registryAccess, dimension)
    }

    suspend fun biomeRemovals(
        registryAccess: RegistryAccess?,
        dimension: ResourceKey<DimensionType>?
    ): List<ResourceKey<Biome>?> = coroutineScope {
        val biomeRemovals: MutableList<ResourceKey<Biome>?> = ArrayList()
        val changes: List<BiomePlacementChange?>? = BiomePlacementChanges.changes
        val removedBiomes: MutableList<DimensionBiomeKeyList?> = ArrayList()
        changes?.forEach { launch {
            it?.removedBiomes?.let { it1 -> removedBiomes.addAll(it1) }
        } }

        val dimensionBiomes = removedBiomes.stream().filter { list: DimensionBiomeKeyList? -> list?.dimension == dimension }.toList()
        for (list in dimensionBiomes) { launch {
            val biomes: List<Either<ResourceKey<Biome>?, TagKey<Biome>?>?> = list?.biomes ?: return@launch
            for (biome in biomes) { launch {
                biome?.ifLeft {
                    biomeRemovals.add(it)
                }
                biome?.ifRight { tag ->
                    val biomeSet: HolderSet.Named<Biome>? = tag?.let {
                        registryAccess?.lookupOrThrow(Registries.BIOME)?.getOrThrow(it)
                    }
                    biomeSet?.forEach { launch {
                        it?.unwrapKey()?.orElseThrow()?.let { it1 -> biomeRemovals.add(it1) }
                    } }
                }
            } }
        } }
        return@coroutineScope biomeRemovals
    }
}
