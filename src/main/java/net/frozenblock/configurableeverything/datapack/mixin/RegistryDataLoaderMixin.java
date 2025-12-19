package net.frozenblock.configurableeverything.datapack.mixin;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import com.mojang.serialization.Lifecycle;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.datapack.util.DatapackUtil;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RegistryDataLoader.ResourceManagerRegistryLoadTask.class)
public abstract class RegistryDataLoaderMixin<T> extends RegistryDataLoader.RegistryLoadTask<T> {

	@Shadow
	@Final
	private ResourceManager resourceManager;

	public RegistryDataLoaderMixin(RegistryDataLoader.RegistryData<T> data, Lifecycle lifecycle, Map<ResourceKey<?>, Exception> loadingErrors) {
		super(data, lifecycle, loadingErrors);
	}

	@Inject(method = "load", at = @At("TAIL"))
    private void loadJson5Contents(
		RegistryOps.RegistryInfoLookup infoLookup, Executor executor, CallbackInfoReturnable<CompletableFuture<?>> cir
    ) {
        var datapack = MainConfig.get(false).datapack;
        if (datapack.json5Support) {
            ResourceKey<? extends Registry<T>> registryKey = this.registryKey();
			String directory = Registries.elementsDirPath(registryKey);
            DatapackUtil.loadJson5Contents(infoLookup, this.resourceManager, registryKey, this.registry, this.data.elementCodec(), this.loadingErrors, directory);
        }
    }
}
