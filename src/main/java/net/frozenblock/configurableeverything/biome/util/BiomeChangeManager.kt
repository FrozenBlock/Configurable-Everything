package net.frozenblock.configurableeverything.biome.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApiStatus.Internal
class BiomeChangeManager implements SimpleResourceReloadListener<BiomeChangeManager.BiomeChangeLoader> {

	private static final Logger LOGGER = LoggerFactory.getLogger("Configurable Everything Biome Change Manager");

	private static final String DIRECTORY = "biome_modifications";

	public static final BiomeChangeManager INSTANCE = new BiomeChangeManager();

	private Map<ResourceLocation, BiomeChange> changes;
	private final Map<ResourceLocation, BiomeChange> queuedChanges = new Object2ObjectOpenHashMap<>();

	@Nullable
	public List<BiomeChange> getChanges() {
		if (this.changes != null) {
			return this.changes.values().stream().toList();
		}
		return null;
	}

	@Nullable
	public BiomeChange getChange(ResourceLocation id) {
		return this.changes.get(id);
	}

	/**
	 * Adds a biome change with the specified {@link ResourceLocation}
	 */
	public void addChange(ResourceLocation key, List<BiomePlacedFeatureList> addedFeatures, List<BiomePlacedFeatureList> removedFeatures, List<BiomePlacedFeatureReplacementList> replacedFeatures, List<BiomeMusic> musicReplacements) {
		addChange(key, new BiomeChange(addedFeatures, removedFeatures, replacedFeatures, musicReplacements));
	}

	/**
	 * Adds a biome change with the specified {@link ResourceLocation}
	 */
	public void addChange(ResourceLocation key, BiomeChange change) {
		if (key != null && change != null) {
			if (change.addedFeatures() != null && change.removedFeatures() != null && change.replacedFeatures() != null && change.musicReplacements() != null) {
				this.queuedChanges.put(key, change);
			}
		}
	}

	public static ResourceLocation getPath(ResourceLocation changeId) {
		return new ResourceLocation(changeId.getNamespace(), DIRECTORY + "/" + changeId.getPath() + ".json");
	}

	@Override
	public CompletableFuture<BiomeChangeLoader> load(ResourceManager manager, ProfilerFiller profiler, Executor executor) {
		return CompletableFuture.supplyAsync(() -> new BiomeChangeLoader(manager, profiler), executor);
	}

	@Override
	public CompletableFuture<Void> apply(BiomeChangeLoader prepared, ResourceManager manager, ProfilerFiller profiler, Executor executor) {
		this.changes = prepared.getChanges();
		this.changes.putAll(this.queuedChanges);
		return CompletableFuture.runAsync(() -> BiomeConfigUtil.applyModifications(this.changes.values()));
	}

	@Override
	public ResourceLocation getFabricId() {
		return ConfigurableEverythingUtils.id("biome_change_reloader");
	}

	public static class BiomeChangeLoader {
		private final ResourceManager manager;
		private final ProfilerFiller profiler;
		private final Map<ResourceLocation, BiomeChange> changes = new Object2ObjectOpenHashMap<>();

		public BiomeChangeLoader(ResourceManager manager, ProfilerFiller profiler) {
			this.manager = manager;
			this.profiler = profiler;
			if (MainConfig.get().datapack.biome) {
				this.loadChanges();
			}
		}

		private void loadChanges() {
			this.profiler.push("Load Biome Changes");
			Map<ResourceLocation, Resource> resources = this.manager.listResources(DIRECTORY, id -> id.getPath().endsWith(".json"));
			var entrySet = resources.entrySet();
			for (Map.Entry<ResourceLocation, Resource> entry : entrySet) {
				this.addBiomeChange(entry.getKey(), entry.getValue());
			}
			this.profiler.pop();
		}

		private void addBiomeChange(ResourceLocation id, Resource resource) {
			BufferedReader reader;
			try {
				reader = resource.openAsReader();
			} catch (IOException e) {
				LOGGER.error(String.format("Unable to open BufferedReader for id %s", id), e);
				return;
			}

			JsonObject json = GsonHelper.parse(reader);
			DataResult<Pair<BiomeChange, JsonElement>> result = BiomeChange.CODEC.decode(JsonOps.INSTANCE, json);

			if (result.error().isPresent()) {
				LOGGER.error(String.format("Unable to parse biome change file %s. \nReason: %s", id, result.error().get().message()));
				return;
			}

			ResourceLocation changeId = new ResourceLocation(id.getNamespace(), id.getPath().substring((DIRECTORY + "/").length()));
			this.changes.put(changeId, result.result().orElseThrow().getFirst());
		}

		public Map<ResourceLocation, BiomeChange> getChanges() {
			return this.changes;
		}
	}
}
