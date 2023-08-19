package net.frozenblock.configurableeverything.datagen;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtilsKt;
import net.frozenblock.lib.datagen.api.FrozenBiomeTagProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.SurfaceWaterDepthFilter;

public class ConfigurableEverythingDataGenerator implements DataGeneratorEntrypoint {

	public static final ResourceKey<Biome> BLANK_BIOME = ResourceKey.create(Registries.BIOME, ConfigurableEverythingUtilsKt.id("blank_biome"));
	public static final ResourceKey<PlacedFeature> BLANK_PLACED_FEATURE = ResourceKey.create(Registries.PLACED_FEATURE, ConfigurableEverythingUtilsKt.id("blank_placed_feature"));

	public static final TagKey<Biome> BLANK_TAG = TagKey.create(Registries.BIOME, ConfigurableEverythingUtilsKt.id("blank_tag"));

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		var pack = fabricDataGenerator.createPack();

		pack.addProvider(WorldgenProvider::new);
		pack.addProvider(BiomeTagProvider::new);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(Registries.BIOME, context -> context.register(
			BLANK_BIOME,
			new Biome.BiomeBuilder()
				.temperature(0.0F)
				.downfall(0f)
				.hasPrecipitation(false)
				.specialEffects(
					new BiomeSpecialEffects.Builder()
						.fogColor(0)
						.waterColor(0)
						.waterFogColor(0)
						.skyColor(0)
						.build()
				)
				.mobSpawnSettings(MobSpawnSettings.EMPTY)
				.generationSettings(BiomeGenerationSettings.EMPTY)
				.build()
		));

		registryBuilder.add(Registries.PLACED_FEATURE, context -> PlacementUtils.register(
			context,
			BLANK_PLACED_FEATURE,
			context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(VegetationFeatures.MANGROVE_VEGETATION),
			CountPlacement.of(25),
			SurfaceWaterDepthFilter.forMaxDepth(5),
			PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
			BiomeFilter.biome(),
			BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(Blocks.MANGROVE_PROPAGULE.defaultBlockState(), BlockPos.ZERO))
		));
	}

	private static class WorldgenProvider extends FabricDynamicRegistryProvider {

		public WorldgenProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
			super(output, registriesFuture);
		}

		@Override
		protected void configure(HolderLookup.Provider registries, Entries entries) {
			entries.addAll(registries.lookupOrThrow(Registries.BIOME));
			entries.addAll(registries.lookupOrThrow(Registries.PLACED_FEATURE));
		}

		@Override
		public String getName() {
			return "Configurable Everything Dynamic Registries";
		}
	}

	private static class BiomeTagProvider extends FrozenBiomeTagProvider {

		public BiomeTagProvider(FabricDataOutput output, CompletableFuture registriesFuture) {
			super(output, registriesFuture);
		}

		@Override
		protected void addTags(HolderLookup.Provider arg) {
			this.getOrCreateTagBuilder(BLANK_TAG);
		}
	}
}
