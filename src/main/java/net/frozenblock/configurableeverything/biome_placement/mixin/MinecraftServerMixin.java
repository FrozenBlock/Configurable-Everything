package net.frozenblock.configurableeverything.biome_placement.mixin;

import com.mojang.datafixers.DataFixer;
import java.net.Proxy;
import net.frozenblock.configurableeverything.biome_placement.util.BiomePlacementUtils;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Services;
import net.minecraft.server.WorldStem;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MinecraftServer.class, priority = 993)
public abstract class MinecraftServerMixin {

	@Shadow
	public abstract RegistryAccess.Frozen registryAccess();

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(Thread serverThread, LevelStorageSource.LevelStorageAccess storageSource, PackRepository packRepository, WorldStem worldStem, Proxy proxy, DataFixer fixerUpper, Services services, ChunkProgressListenerFactory progressListenerFactory, CallbackInfo ci) {
		BiomePlacementUtils.serverInit(this.registryAccess());
	}
}
