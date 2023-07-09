package net.frozenblock.configurableeverything.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import java.util.concurrent.CompletableFuture;

public class ConfigurableEverythingDataGenerator implements DataGeneratorEntrypoint {

	public static final ResourceKey<Biome> BLANK_BIOME = ResourceKey.create(Registries.BIOME, ConfigurableEverythingUtils.id("blank_biome"));

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		fabricDataGenerator.createPack().addProvider(WorldgenProvider::new);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(Registries.BIOME, context -> {
			context.register(
				BLANK_BIOME,
				new Biome.BiomeBuilder()
					.temperature(0f)
					.downfall(0f)
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
			);
		});
	}

	private static class WorldgenProvider extends FabricDynamicRegistryProvider {

		public WorldgenProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
			super(output, registriesFuture);
		}

		@Override
		protected void configure(HolderLookup.Provider registries, Entries entries) {
			entries.addAll(registries.lookupOrThrow(Registries.BIOME));
		}

		@Override
		public String getName() {
			return "Configurable Everything Dynamic Registries";
		}
	}
}
