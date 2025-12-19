package net.frozenblock.configurableeverything.datapack.mixin;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import com.mojang.serialization.Lifecycle;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.datapack.util.DatapackUtil;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RegistryDataLoader.ResourceManagerRegistryLoadTask.class)
public abstract class RegistryDataLoaderMixin<T> extends RegistryDataLoader.RegistryLoadTask<T> {

	@Shadow
	@Final
	private ResourceManager resourceManager;

	@Unique
	private RegistryOps.RegistryInfoLookup context;

	@Unique
	private Executor executor;

	public RegistryDataLoaderMixin(RegistryDataLoader.RegistryData<T> data, Lifecycle lifecycle, Map<ResourceKey<?>, Exception> loadingErrors) {
		super(data, lifecycle, loadingErrors);
	}

	@Inject(method = "load", at = @At("HEAD"))
	private void setContext(RegistryOps.RegistryInfoLookup context, Executor executor, CallbackInfoReturnable<CompletableFuture<?>> cir) {
		this.context = context;
		this.executor = executor;
	}

	@ModifyVariable(method = "lambda$load$3", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private Map<Identifier, RegistryDataLoader.PendingRegistration<T>> loadJson5Contents(
		Map<Identifier, RegistryDataLoader.PendingRegistration<T>> value
    ) {
        var datapack = MainConfig.get(false).datapack;
        if (datapack.moreJsonSupport) {
            ResourceKey<? extends Registry<T>> registryKey = this.registryKey();
			String directory = Registries.elementsDirPath(registryKey);
            DatapackUtil.loadJson5Contents(this.context, this.resourceManager, registryKey, value, this.data.elementCodec(), this.executor, directory);
        }
		return value;
	}

	@Inject(method = "lambda$load$3", at = @At("TAIL"))
	private void removeContext(Map loadedEntries, CallbackInfo ci) {
		this.context = null;
		this.executor = null;
	}
}
