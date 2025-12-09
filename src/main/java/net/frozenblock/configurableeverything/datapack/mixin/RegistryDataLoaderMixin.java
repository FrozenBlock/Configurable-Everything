package net.frozenblock.configurableeverything.datapack.mixin;

import com.mojang.serialization.Decoder;
import java.util.Map;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.datapack.util.DatapackUtil;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RegistryDataLoader.class)
public abstract class RegistryDataLoaderMixin {

    @Inject(method = "loadContentsFromManager", at = @At("TAIL"))
    private static <E> void loadJson5Contents(
		ResourceManager resourceManager, RegistryOps.RegistryInfoLookup infoLookup, WritableRegistry<E> registry, Decoder<E> elementDecoder, Map<ResourceKey<?>, Exception> errors, CallbackInfo ci
    ) {
        var datapack = MainConfig.get(false).datapack;
        if (datapack.json5Support) {
            ResourceKey<? extends Registry<E>> registryKey = registry.key();
			String directory = Registries.elementsDirPath(registryKey);
            DatapackUtil.loadJson5Contents(infoLookup, resourceManager, registryKey, registry, elementDecoder, errors, directory);
        }
    }
}
