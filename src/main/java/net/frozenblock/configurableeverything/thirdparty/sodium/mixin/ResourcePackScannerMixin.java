package net.frozenblock.configurableeverything.thirdparty.sodium.mixin;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import me.jellysquid.mods.sodium.client.compatibility.checks.ResourcePackScanner;
import net.frozenblock.configurableeverything.config.thirdparty.SodiumConfig;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Objects;

@Mixin(ResourcePackScanner.class)
public abstract class ResourcePackScannerMixin {

    @Shadow
    private static String getResourcePackName(PackResources pack) {
        return null;
    }

    @Inject(method = "checkIfCoreShaderLoaded", at = @At("HEAD"), cancellable = true)
	private static void disableCompatChecks(ResourceManager manager, CallbackInfo ci) {
		SodiumConfig config = SodiumConfig.get();
		if (Boolean.TRUE.equals(config.disableCompatibilityChecks)) {
			ci.cancel();
		}
	}

	@Inject(method = "scanResources", at = @At("HEAD"))
	private static void disableCheck(PackResources resourcePack, CallbackInfoReturnable cir, @Share("disabled") LocalBooleanRef isDisabled) {
		String name = getResourcePackName(resourcePack);
		SodiumConfig config = SodiumConfig.get();
		var disabledPacks = config.ignoredResourcePacks;
		if (disabledPacks == null) return;

		for (String pack : disabledPacks) {
			if (Objects.equals(pack, name)) {
				isDisabled.set(true);
				break;
			}
		}
	}

	@WrapWithCondition(method = "scanResources", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"))
	private static boolean disableWarn(Logger instance, String s, Object name, Object ignoredShader, @Share("disabled") LocalBooleanRef isDisabled) {
        return !isDisabled.get();
    }

	@WrapWithCondition(method = "scanResources", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/PackResources;listResources(Lnet/minecraft/server/packs/PackType;Ljava/lang/String;Ljava/lang/String;Lnet/minecraft/server/packs/PackResources$ResourceOutput;)V"))
	private static boolean disableList(PackResources instance, PackType packType, String namespace, String path, PackResources.ResourceOutput resourceOutput, @Share("disabled") LocalBooleanRef isDisabled) {
		return !isDisabled.get();
	}
}
