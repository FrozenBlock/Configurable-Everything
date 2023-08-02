package net.frozenblock.configurableeverything.util

import com.mojang.datafixers.util.Either
import com.mojang.datafixers.util.Pair
import net.frozenblock.configurableeverything.biome_placement.util.DimensionBiomeKeyList
import net.frozenblock.configurableeverything.biome_placement.util.DimensionBiomeList
import net.frozenblock.configurableeverything.config.BiomePlacementConfig
import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstants.LOGGER
import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstants.MOD_ID
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.Climate.ParameterPoint
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.dimension.DimensionType
import java.nio.file.Path

object ConfigurableEverythingUtils {

    // CONFIG
    @JvmStatic
    fun makePath(name: String, json5: Boolean): Path {
        return Path.of("./config/" + MOD_ID + "/" + name + "." + if (json5) "json5" else "json")
    }

    // BIOME PARAMETERS
    @JvmStatic
    fun biomeAdditions(registryAccess: HolderGetter<Biome>?, dimension: ResourceKey<DimensionType?>): List<Pair<ParameterPoint, Holder<Biome>>> {
        val biomeAdditions: MutableList<Pair<ParameterPoint, Holder<Biome>>> = ArrayList()
        val addedBiomes = BiomePlacementConfig.get().addedBiomes
        if (addedBiomes?.value() != null) {
            val dimensionBiomes = addedBiomes.value()!!.stream().filter { list: DimensionBiomeList -> list.dimension == dimension }.toList()
            for (list in dimensionBiomes) {
                for (parameters in list.biomes) {
                    biomeAdditions.add(Pair.of(parameters.parameters, registryAccess?.getOrThrow(parameters.biome)))
                }
            }
        }
        return biomeAdditions
    }

    @JvmStatic
    fun biomeRemovals(dimension: ResourceKey<DimensionType?>, registryAccess: RegistryAccess?): List<ResourceKey<Biome>> {
        val biomeRemovals: MutableList<ResourceKey<Biome>> = ArrayList()
        val removedBiomes = BiomePlacementConfig.get().removedBiomes
        if (removedBiomes?.value() != null) {
            val dimensionBiomes = removedBiomes.value()!!.stream().filter { list: DimensionBiomeKeyList -> list.dimension == dimension }.toList()
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
        }
        return biomeRemovals
    }

    // LOGGING
    @JvmStatic
    fun log(string: String?, shouldLog: Boolean) {
        if (shouldLog) {
            LOGGER.info(string)
        }
    }

    @JvmStatic
    fun log(entity: Entity, string: String, shouldLog: Boolean) {
        if (shouldLog) {
            LOGGER.info(entity.toString() + " : " + string + " : " + entity.position())
        }
    }

    @JvmStatic
    fun log(block: Block, string: String, shouldLog: Boolean) {
        if (shouldLog) {
            LOGGER.info("$block : $string : ")
        }
    }

    @JvmStatic
    fun log(block: Block, pos: BlockPos, string: String, shouldLog: Boolean) {
        if (shouldLog) {
            LOGGER.info("$block : $string : $pos")
        }
    }

    @JvmStatic
    fun logMod(string: String, shouldLog: Boolean) {
        if (shouldLog) {
            LOGGER.info("$string $MOD_ID")
        }
    }

    @JvmStatic
    fun error(string: String?, shouldLog: Boolean) {
        if (shouldLog) {
            LOGGER.error(string)
        }
    }

    // MEASURING
    private val INSTANT_MAP: MutableMap<Any, Long> = HashMap()

    @JvmStatic
    fun startMeasuring(`object`: Any) {
        val started = System.nanoTime()
        val name = `object`.javaClass.name
        LOGGER.info("Started measuring {}", name.substring(name.lastIndexOf(".") + 1))
        INSTANT_MAP[`object`] = started
    }

    @JvmStatic
    fun stopMeasuring(`object`: Any) {
        if (INSTANT_MAP.containsKey(`object`)) {
            val name = `object`.javaClass.name
            LOGGER.info("{} took {} nanoseconds", name.substring(name.lastIndexOf(".") + 1), System.nanoTime() - INSTANT_MAP[`object`]!!)
            INSTANT_MAP.remove(`object`)
        }
    }

    @JvmStatic
    fun id(path: String?): ResourceLocation {
        return ResourceLocation(MOD_ID, path)
    }

    fun vanillaId(path: String?): ResourceLocation {
        return ResourceLocation(ResourceLocation.DEFAULT_NAMESPACE, path)
    }

    fun string(path: String?): String {
        return id(path).toString()
    }
}
