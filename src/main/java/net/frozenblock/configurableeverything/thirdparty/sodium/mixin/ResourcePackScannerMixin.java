package net.frozenblock.configurableeverything.thirdparty.sodium.mixin;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
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
import java.util.Set;

@Mixin(value = ResourcePackScanner.class, remap = false)
public abstract class ResourcePackScannerMixin {

	@WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "Ljava/util/Set;of([Ljava/lang/Object;)Ljava/util/Set;", ordinal = 0))
	private static Set<String> wrapBlacklist(Object[] set, Operation<Set<String>> original) {
		SodiumConfig config = SodiumConfig.get();
		if (Boolean.TRUE.equals(config.disableCompatibilityChecks)) {
			return original.call((Object) new String[0]);
		}
		return original.call((Object) set);
	}

	@WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "Ljava/util/Set;of(Ljava/lang/Object;Ljava/lang/Object;)Ljava/util/Set;", ordinal = 0))
	private static Set<String> wrapBlackList(Object obj1, Object obj2, Operation<Set<String>> original) {
		SodiumConfig config = SodiumConfig.get();
		if (Boolean.TRUE.equals(config.disableCompatibilityChecks)) {
			return Set.of();
		}
		return original.call(obj1, obj2);
	}

    @Inject(method = "checkIfCoreShaderLoaded", at = @At("HEAD"), cancellable = true, remap = false)
	private static void disableCompatChecks(ResourceManager manager, CallbackInfo ci) {
		SodiumConfig config = SodiumConfig.get();
		if (Boolean.TRUE.equals(config.disableCompatibilityChecks)) {
			ci.cancel();
		}
	}
}
