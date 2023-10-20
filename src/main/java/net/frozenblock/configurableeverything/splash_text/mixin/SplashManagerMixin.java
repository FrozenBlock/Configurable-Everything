package net.frozenblock.configurableeverything.splash_text.mixin;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.config.SplashTextConfig;
import net.minecraft.client.resources.SplashManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SplashManager.class, priority = 1) // mixin must run first to prevent removing modded splashes
@Environment(EnvType.CLIENT)
public class SplashManagerMixin {

    @Inject(method = "apply(Ljava/util/List;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At("HEAD"))
    private void apply(List<String> object, ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfo ci) {
        if (MainConfig.get(false).splash_text == true && SplashTextConfig.get(false).removeVanilla == true) {
            object.clear();
        }
    }
}
