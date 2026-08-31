package net.frozenblock.configurableeverything.thirdparty.sodium.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.caffeinemc.mods.sodium.client.checks.ResourcePackScanner;
import net.frozenblock.configurableeverything.config.thirdparty.SodiumConfig;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Set;

@Mixin(value = ResourcePackScanner.class, remap = false)
public abstract class ResourcePackScannerMixin {

	@WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "Ljava/util/Set;of([Ljava/lang/Object;)Ljava/util/Set;", ordinal = 0))
	private static Set<String> wrapBlacklist(Object[] set, Operation<Set<String>> original) {
		SodiumConfig config = SodiumConfig.get();
		if (config.disableResourcePackScanner) {
			return original.call((Object) new String[0]);
		}
		return original.call((Object) set);
	}

	@WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "Ljava/util/Set;of(Ljava/lang/Object;Ljava/lang/Object;)Ljava/util/Set;", ordinal = 0))
	private static Set<String> wrapBlackList(Object obj1, Object obj2, Operation<Set<String>> original) {
		SodiumConfig config = SodiumConfig.get();
		if (config.disableResourcePackScanner) {
			return Set.of();
		}
		return original.call(obj1, obj2);
	}

    @Inject(method = "checkIfCoreShaderLoaded", at = @At("HEAD"), cancellable = true, remap = false)
	private static void disableCompatChecks(ResourceManager manager, CallbackInfo ci) {
		SodiumConfig config = SodiumConfig.get();
		if (config.disableResourcePackScanner) {
			ci.cancel();
		}
	}
}
