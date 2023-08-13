package net.frozenblock.configurableeverything.biome_placement.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@ApiStatus.Internal
class BiomePlacementChangeManager implements SimpleResourceReloadListener<BiomePlacementChangeManager.BiomePlacementChangeLoader> {

	private static final Logger LOGGER = LoggerFactory.getLogger("Configurable Everything Biome Placement Change Manager");

	private static final String DIRECTORY = "biome_placement";

	public static final BiomePlacementChangeManager INSTANCE = new BiomePlacementChangeManager();

	private Map<ResourceLocation, BiomePlacementChange> changes;
	private final Map<ResourceLocation, BiomePlacementChange> queuedChanges = new Object2ObjectOpenHashMap<>();

	@Nullable
	public List<BiomePlacementChange> getChanges() {
		if (this.changes != null) {
			return this.changes.values().stream().toList();
		}
		return null;
	}

	@Nullable
	public BiomePlacementChange getChange(ResourceLocation id) {
		return this.changes.get(id);
	}

	/**
	 * Adds a biome placement change with the specified {@link ResourceLocation}
	 */
	public void addChange(ResourceLocation key, List<DimensionBiomeList> addedBiomes, List<DimensionBiomeKeyList> removedBiomes) {
		addChange(key, new BiomePlacementChange(addedBiomes, removedBiomes));
	}

	/**
	 * Adds a biome placement change with the specified {@link ResourceLocation}
	 */
	public void addChange(ResourceLocation key, BiomePlacementChange change) {
		if (key != null && change != null) {
			if (change.addedBiomes() != null && change.removedBiomes() != null) {
				this.queuedChanges.put(key, change);
			}
		}
	}

	public static ResourceLocation getPath(ResourceLocation changeId) {
		return new ResourceLocation(changeId.getNamespace(), DIRECTORY + "/" + changeId.getPath() + ".json");
	}

	@Override
	public CompletableFuture<BiomePlacementChangeLoader> load(ResourceManager manager, ProfilerFiller profiler, Executor executor) {
		return CompletableFuture.supplyAsync(() -> new BiomePlacementChangeLoader(manager, profiler), executor);
	}

	@Override
	public CompletableFuture<Void> apply(BiomePlacementChangeLoader prepared, ResourceManager manager, ProfilerFiller profiler, Executor executor) {
		this.changes = prepared.getChanges();
		this.changes.putAll(this.queuedChanges);
		return CompletableFuture.runAsync(() -> {
		});
	}

	@Override
	public ResourceLocation getFabricId() {
		return ConfigurableEverythingUtils.id("biome_placement_change_reloader");
	}

	public static class BiomePlacementChangeLoader {
		private final ResourceManager manager;
		private final ProfilerFiller profiler;
		private final Map<ResourceLocation, BiomePlacementChange> changes = new Object2ObjectOpenHashMap<>();

		public BiomePlacementChangeLoader(ResourceManager manager, ProfilerFiller profiler) {
			this.manager = manager;
			this.profiler = profiler;
			if (MainConfig.get().datapack.biome_placement) {
				this.loadPlacementChanges();
			}
		}

		private void loadPlacementChanges() {
			this.profiler.push("Load Biome Placement Changes");
			Map<ResourceLocation, Resource> resources = this.manager.listResources(DIRECTORY, id -> id.getPath().endsWith(".json"));
			var entrySet = resources.entrySet();
			for (Map.Entry<ResourceLocation, Resource> entry : entrySet) {
				this.addPlacementChange(entry.getKey(), entry.getValue());
			}
			this.profiler.pop();
		}

		private void addPlacementChange(ResourceLocation id, Resource resource) {
			BufferedReader reader;
			try {
				reader = resource.openAsReader();
			} catch (IOException e) {
				LOGGER.error(String.format("Unable to open BufferedReader for id %s", id), e);
				return;
			}

			JsonObject json = GsonHelper.parse(reader);
			DataResult<Pair<BiomePlacementChange, JsonElement>> result = BiomePlacementChange.CODEC.decode(JsonOps.INSTANCE, json);

			if (result.error().isPresent()) {
				LOGGER.error(String.format("Unable to parse biome placement change file %s. \nReason: %s", id, result.error().get().message()));
				return;
			}

			ResourceLocation changeId = new ResourceLocation(id.getNamespace(), id.getPath().substring((DIRECTORY + "/").length()));
			this.changes.put(changeId, result.result().orElseThrow().getFirst());
		}

		public Map<ResourceLocation, BiomePlacementChange> getChanges() {
			return this.changes;
		}
	}
}
