package net.frozenblock.configurableeverything.datafixer.mixin;

import java.util.Map;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.datafixer.util.RegistryFixer;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MappedRegistry.class)
public abstract class MappedRegistryMixin<T> implements WritableRegistry<T> {

	@Shadow
	@Final
	private Map<ResourceLocation, Holder.Reference<T>> byLocation;

	@Shadow
	@Nullable
	private static <T> T getValueFromNullable(Holder.@Nullable Reference<T> reference) {
		return null;
	}

	@Inject(
			method = "get(Lnet/minecraft/resources/ResourceLocation;)Ljava/lang/Object;",
			at = @At("RETURN"),
			cancellable = true
	)
	private void fixedValue(@Nullable ResourceLocation name, CallbackInfoReturnable<@Nullable T> cir) {
		var original = cir.getReturnValue();
		var fixed = convertResourceLocation(name, original);
		if (fixed != original) {
			cir.setReturnValue(fixed);
		}
	}

	@Inject(
			method = "get(Lnet/minecraft/resources/ResourceKey;)Ljava/lang/Object;",
			at = @At("RETURN"),
			cancellable = true
	)
	private void fixedValue(@Nullable ResourceKey<T> key, CallbackInfoReturnable<@Nullable T> cir) {
		if (key != null) {
			var original = cir.getReturnValue();
			var fixed = convertResourceLocation(key.location(), original);
			if (fixed != original) {
				cir.setReturnValue(fixed);
			}
		}
	}

	@Inject(
		method = "getOrCreateHolderOrThrow",
		at = @At("RETURN"),
		cancellable = true
	)
	private void fixedHolder(ResourceKey<T> key, CallbackInfoReturnable<Holder.Reference<T>> cir) {
		if (key != null) {
			var original = cir.getReturnValue();
			var fixed = convertResourceLocationHolder(key.location(), original);
			if (fixed != original) {
				cir.setReturnValue(fixed);
			}
		}
	}

	@Nullable
	@Unique
	private T convertResourceLocation(@Nullable ResourceLocation name, @Nullable T original) {
		if (MainConfig.get(false).datafixer == true
			&& name != null) {
			var fixed = RegistryFixer.getFixedValueInRegistry(this, name, original);
			if (fixed != null) // don't override if the "fixed" version is missing
				return getValueFromNullable(this.byLocation.get(fixed));
		}
		return original;
	}

	@Nullable
	@Unique
	private Holder.Reference<T> convertResourceLocationHolder(@Nullable ResourceLocation name, @Nullable Holder.Reference<T> original) {
		if (MainConfig.get(false).datafixer == true
			&& name != null) {
			var fixed = RegistryFixer.getFixedValueInRegistry(this, name, original);
			if (fixed != null) // don't override if the "fixed" version is missing
				return this.byLocation.get(fixed);
		}
		return original;
	}
}
