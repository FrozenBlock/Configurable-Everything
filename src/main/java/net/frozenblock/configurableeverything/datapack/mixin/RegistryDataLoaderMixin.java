package net.frozenblock.configurableeverything.datapack.mixin;

import com.mojang.serialization.Decoder;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.datapack.util.DatapackUtils;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.*;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(RegistryDataLoader.class)
public abstract class RegistryDataLoaderMixin {

    @Shadow
    private static String registryDirPath(ResourceLocation location) {
        return null;
    }

    @Inject(method = "loadRegistryContents", at = @At("TAIL"))
    private static <E> void loadJson5Contents(
            RegistryOps.RegistryInfoLookup lookup,
            ResourceManager manager,
            ResourceKey<? extends Registry<E>> registryKey,
            WritableRegistry<E> registry,
            Decoder<E> decoder,
            Map<ResourceKey<?>, Exception> exceptions,
            CallbackInfo ci
    ) {
        if (MainConfig.get().datapack.json5Support) {
            String directory = registryDirPath(registryKey.location());
            DatapackUtils.loadJsonContents(lookup, manager, registryKey, registry, decoder, exceptions, directory);
        }
    }
}
