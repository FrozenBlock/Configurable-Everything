package net.frozenblock.configurableeverything.biome_placement.util

import com.mojang.datafixers.util.Either
import com.mojang.datafixers.util.Pair
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

object BiomePlacementUtils {

    @JvmStatic
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
    fun serverInit(registryAccess: RegistryAccess) {
        val biomeRegistry = registryAccess.lookupOrThrow(Registries.BIOME)
        val levelStemRegistry = registryAccess.registryOrThrow(Registries.LEVEL_STEM)

        if (MainConfig.get().biome_placement == true) {
            for ((_, stem) in levelStemRegistry.entrySet()) {
                val dimension = stem.type().unwrapKey().orElseThrow()
                val chunkGenerator = stem.generator()
                if (chunkGenerator is NoiseBasedChunkGenerator) {
                    val biomeSource = chunkGenerator.getBiomeSource()
                    if (biomeSource is MultiNoiseBiomeSource) {
                        val extended = biomeSource as? BiomeSourceExtension
                        val parameters: Climate.ParameterList<Holder<Biome>>? = biomeSource.parameters()
                        (parameters as? ParameterListExtension)?.updateBiomesList(registryAccess, dimension)

                        // remove biomes first to allow replacing biome parameters
                        val removedBiomeHolders: MutableList<Holder<Biome>?> = ArrayList()
                        for (biome in biomeRemovals(dimension, registryAccess)) {
                            removedBiomeHolders.add(biome?.let { biomeRegistry.getOrThrow(it) })
                        }
                        val addedBiomes: List<Pair<ParameterPoint?, Holder<Biome>>> = biomeAdditions(biomeRegistry, dimension)
                        val addedBiomeHolders: MutableList<Holder<Biome>?> = ArrayList()
                        for (pair in addedBiomes) {
                            addedBiomeHolders.add(pair.second)
                        }
                        extended?.updateBiomesList(addedBiomeHolders, removedBiomeHolders, registryAccess)

                    }
                }
            }
        }
    }

    @JvmStatic
    fun biomeAdditions(registryAccess: HolderGetter<Biome>?, dimension: ResourceKey<DimensionType?>): List<Pair<ParameterPoint?, Holder<Biome>>> {
        val biomeAdditions: MutableList<Pair<ParameterPoint?, Holder<Biome>>> = ArrayList()
        val changes: List<BiomePlacementChange?>? = BiomePlacementChanges.changes
        val addedBiomes: MutableList<DimensionBiomeList?> = ArrayList()
        changes?.forEach {
            it?.addedBiomes?.let { biomes -> addedBiomes.addAll(biomes) }
        }

        val dimensionBiomes = addedBiomes.stream().filter { list: DimensionBiomeList? -> list?.dimension == dimension }.toList()
        for (list in dimensionBiomes) {
            list?.biomes?.forEach { parameters ->
                parameters?.biome?.let { biome ->
                    parameters.parameters?.toImmutable()?.let {
                        biomeAdditions.add(
                            Pair.of(
                                it,
                                registryAccess?.getOrThrow(ResourceKey.create(Registries.BIOME, biome))
                            )
                        )
                    }
                }
            }
        }
        return biomeAdditions
    }

    @JvmStatic
    fun biomeRemovals(dimension: ResourceKey<DimensionType?>?, registryAccess: RegistryAccess?): List<ResourceKey<Biome>?> {
        val biomeRemovals: MutableList<ResourceKey<Biome>?> = ArrayList()
        val changes: List<BiomePlacementChange?>? = BiomePlacementChanges.changes
        val removedBiomes: MutableList<DimensionBiomeKeyList?> = ArrayList()
        changes?.forEach {
            it?.removedBiomes?.let { it1 -> removedBiomes.addAll(it1) }
        }

        val dimensionBiomes = removedBiomes.stream().filter { list: DimensionBiomeKeyList? -> list?.dimension == dimension }.toList()
        for (list in dimensionBiomes) {
            val biomes: List<Either<ResourceKey<Biome>?, TagKey<Biome>?>?>? = list?.biomes
            biomes?.forEach { biome ->
                biome?.ifLeft { biomeRemovals.add(it) }
                biome?.ifRight { tag ->
                    val biomeSet: HolderSet.Named<Biome>? = tag?.let {
                        registryAccess?.lookupOrThrow(Registries.BIOME)?.getOrThrow(it)
                    }
                    biomeSet?.forEach { it?.unwrapKey()?.orElseThrow()?.let { it1 -> biomeRemovals.add(it1) } }
                }
            }
        }
        return biomeRemovals
    }
}
