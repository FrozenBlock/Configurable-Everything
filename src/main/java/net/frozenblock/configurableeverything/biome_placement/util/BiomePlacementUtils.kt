package net.frozenblock.configurableeverything.biome_placement.util

import com.mojang.datafixers.util.Either
import com.mojang.datafixers.util.Pair
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.frozenblock.configurableeverything.config.BiomePlacementConfig
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtils
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
import net.minecraft.world.level.dimension.DimensionType

object BiomePlacementUtils {

    @JvmStatic
    fun init() {
        val config = BiomePlacementConfig.get()
        if (MainConfig.get().biome_placement) {
            if (config.addedBiomes?.value != null && config.removedBiomes?.value != null) {
                val placementChange = BiomePlacementChange(config.addedBiomes.value, config.removedBiomes.value)
                BiomePlacementChanges.addChange(ConfigurableEverythingUtils.id("config"), placementChange)
            }

            val resourceLoader = ResourceManagerHelper.get(PackType.SERVER_DATA)
            resourceLoader?.registerReloadListener(BiomePlacementChangeManager.INSTANCE)
        }
    }

    @JvmStatic
    fun biomeAdditions(registryAccess: HolderGetter<Biome>?, dimension: ResourceKey<DimensionType?>): List<Pair<Climate.ParameterPoint, Holder<Biome>>> {
        val biomeAdditions: MutableList<Pair<Climate.ParameterPoint, Holder<Biome>>> = ArrayList()
        val changes: MutableList<BiomePlacementChange>? = BiomePlacementChanges.getChanges()
        val addedBiomes: MutableList<DimensionBiomeList> = ArrayList()
        if (changes != null) {
            for (change in changes) {
                addedBiomes.addAll(change.addedBiomes)
            }
        }

        val dimensionBiomes = addedBiomes.stream().filter { list: DimensionBiomeList -> list.dimension == dimension }.toList()
        for (list in dimensionBiomes) {
            for (parameters in list.biomes) {
                biomeAdditions.add(Pair.of(parameters.parameters, registryAccess?.getOrThrow(parameters.biome)))
            }
        }
        return biomeAdditions
    }

    @JvmStatic
    fun biomeRemovals(dimension: ResourceKey<DimensionType?>, registryAccess: RegistryAccess?): List<ResourceKey<Biome>> {
        val biomeRemovals: MutableList<ResourceKey<Biome>> = ArrayList()
        val changes: MutableList<BiomePlacementChange>? = BiomePlacementChanges.getChanges()
        val removedBiomes: MutableList<DimensionBiomeKeyList> = ArrayList()
        if (changes != null) {
            for (change in changes) {
                removedBiomes.addAll(change.removedBiomes)
            }
        }

        val dimensionBiomes = removedBiomes.stream().filter { list: DimensionBiomeKeyList -> list.dimension == dimension }.toList()
        for (list in dimensionBiomes) {
            val biomes: List<Either<ResourceKey<Biome>, TagKey<Biome>>> = list.biomes
            for (biome in biomes) {
                biome.ifLeft { biomeRemovals.add(it) }
                biome.ifRight { tag ->
                    val biomeSet: HolderSet.Named<Biome>? = registryAccess?.lookupOrThrow(Registries.BIOME)?.getOrThrow(tag)
                    biomeSet?.forEach { it?.unwrapKey()?.orElseThrow()?.let { it1 -> biomeRemovals.add(it1) } }
                }
            }
        }
        return biomeRemovals
    }
}
