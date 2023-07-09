package net.frozenblock.configurableeverything.biome.mixin;

import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.util.Pair;
import net.frozenblock.configurableeverything.biome.util.BiomeSourceExtension;
import net.frozenblock.configurableeverything.config.BiomeConfig;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtils;
import net.frozenblock.configurableeverything.biome.util.ParameterListExtension;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Services;
import net.minecraft.server.WorldStem;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(value = MinecraftServer.class, priority = 993)
public abstract class MinecraftServerMixin {

	@Shadow
	public abstract RegistryAccess.Frozen registryAccess();

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(Thread serverThread, LevelStorageSource.LevelStorageAccess storageSource, PackRepository packRepository, WorldStem worldStem, Proxy proxy, DataFixer fixerUpper, Services services, ChunkProgressListenerFactory progressListenerFactory, CallbackInfo ci) {
		if (MainConfig.get().biome) {
			var registryAccess = this.registryAccess();
			var levelStemRegistry = this.registryAccess().registryOrThrow(Registries.LEVEL_STEM);

			for (Map.Entry<ResourceKey<LevelStem>, LevelStem> entry : levelStemRegistry.entrySet()) {
				var stem = entry.getValue();
				var dimension = stem.type().unwrapKey().orElseThrow();
				var chunkGenerator = stem.generator();
				if (chunkGenerator instanceof NoiseBasedChunkGenerator noiseChunkGenerator) {
					var biomeRegistry = registryAccess.lookupOrThrow(Registries.BIOME);
					var biomeSource = noiseChunkGenerator.getBiomeSource();
					if (biomeSource instanceof MultiNoiseBiomeSource multiNoiseBiomeSource) {
						BiomeSourceExtension extended = (BiomeSourceExtension) multiNoiseBiomeSource;

						var parameters = multiNoiseBiomeSource.parameters();
						((ParameterListExtension) parameters).updateBiomesList(registryAccess, dimension);

						List<Pair<Climate.ParameterPoint, Holder<Biome>>> addedBiomes = ConfigurableEverythingUtils.biomeAdditions(biomeRegistry, dimension);
						List<Holder<Biome>> addedBiomeHolders = new ArrayList<>();
						for (Pair<Climate.ParameterPoint, Holder<Biome>> pair : addedBiomes) {
							addedBiomeHolders.add(pair.getSecond());
						}
						List<Holder<Biome>> removedBiomeHolders = new ArrayList<>();
						for (ResourceKey<Biome> biome : ConfigurableEverythingUtils.biomeRemovals(dimension)) {
							removedBiomeHolders.add(biomeRegistry.getOrThrow(biome));
						}

						extended.updateBiomesList(addedBiomeHolders, removedBiomeHolders);
					}
				}
			}
		}
	}
}
