package net.frozenblock.configurableeverything.datagen

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.lib.datagen.api.FrozenBiomeTagProvider
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.data.worldgen.features.VegetationFeatures
import net.minecraft.data.worldgen.placement.PlacementUtils
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.Biome.BiomeBuilder
import net.minecraft.world.level.biome.BiomeGenerationSettings
import net.minecraft.world.level.biome.BiomeSpecialEffects
import net.minecraft.world.level.biome.MobSpawnSettings
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate
import net.minecraft.world.level.levelgen.placement.*
import java.util.concurrent.CompletableFuture

class ConfigurableEverythingDataGenerator : DataGeneratorEntrypoint {
    companion object {
        @JvmField
        val BLANK_BIOME: ResourceKey<Biome> = ResourceKey.create(Registries.BIOME, id("blank_biome"))
        @JvmField
        val BLANK_PLACED_FEATURE: ResourceKey<PlacedFeature> = ResourceKey.create(Registries.PLACED_FEATURE, id("blank_placed_feature"))
        @JvmField
        val BLANK_TAG: TagKey<Biome> = TagKey.create(Registries.BIOME, id("blank_tag"))
    }

    override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
        val pack = fabricDataGenerator.createPack()
        pack.addProvider { output: FabricDataOutput?, registriesFuture: CompletableFuture<HolderLookup.Provider?>? ->
            WorldgenProvider(
                output,
                registriesFuture
            )
        }
        pack.addProvider { output: FabricDataOutput?, registriesFuture: CompletableFuture<HolderLookup.Provider?>? ->
            BiomeTagProvider(
                output,
                registriesFuture
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun buildRegistry(registryBuilder: RegistrySetBuilder) {
        registryBuilder.add(Registries.BIOME) { context: BootstrapContext<Biome> ->
            context.register(
                BLANK_BIOME,
                BiomeBuilder()
                    .temperature(0.5f)
                    .downfall(0f)
                    .hasPrecipitation(false)
                    .specialEffects(
                        BiomeSpecialEffects.Builder()
                            .grassColorOverride(8421504)
                            .foliageColorOverride(8421504)
                            .fogColor(0)
                            .waterColor(0)
                            .waterFogColor(0)
                            .skyColor(0)
                            .build()
                    )
                    .mobSpawnSettings(MobSpawnSettings.EMPTY)
                    .generationSettings(BiomeGenerationSettings.EMPTY)
                    .build()
            )
        }
        registryBuilder.add(Registries.PLACED_FEATURE) { context: BootstrapContext<PlacedFeature?> ->
            PlacementUtils.register(
                context,
                BLANK_PLACED_FEATURE,
                context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(VegetationFeatures.MANGROVE_VEGETATION),
                CountPlacement.of(25),
                SurfaceWaterDepthFilter.forMaxDepth(5),
                PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                BiomeFilter.biome(),
                BlockPredicateFilter.forPredicate(
                    BlockPredicate.wouldSurvive(
                        Blocks.MANGROVE_PROPAGULE.defaultBlockState(),
                        BlockPos.ZERO
                    )
                )
            )
        }
    }

    private class WorldgenProvider(
        output: FabricDataOutput?,
        registriesFuture: CompletableFuture<HolderLookup.Provider?>?
    ) : FabricDynamicRegistryProvider(output, registriesFuture) {
        override fun configure(registries: HolderLookup.Provider, entries: Entries) {
            entries.addAll(registries.lookupOrThrow(Registries.BIOME))
            entries.addAll(registries.lookupOrThrow(Registries.PLACED_FEATURE))
        }

        override fun getName(): String {
            return "Configurable Everything Dynamic Registries"
        }
    }

    private class BiomeTagProvider(output: FabricDataOutput?, registriesFuture: CompletableFuture<*>?) : FrozenBiomeTagProvider(output, registriesFuture) {
        override fun addTags(arg: HolderLookup.Provider) {
            builder(BLANK_TAG)
        }
    }
}
