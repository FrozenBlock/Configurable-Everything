package net.frozenblock.configurableeverything.splash_text.mixin;

import java.util.List;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.config.SplashTextConfig;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.resources.SplashManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SplashManager.class, priority = 1) // mixin must run first to prevent removing modded splashes
@ClientOnly
public class SplashManagerMixin {

    @Inject(method = "apply(Ljava/util/List;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At("HEAD"))
    private void apply(List<String> preparations, ResourceManager manager, ProfilerFiller profiler, CallbackInfo ci) {
        if (MainConfig.splash_text.get() && SplashTextConfig.removeVanilla.get()) {
            preparations.clear();
        }
    }
}
