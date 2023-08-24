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
		if (MainConfig.get().datafixer == true
			&& name != null) {
				var fixed = RegistryFixer.getFixedValueInRegistry(this, name, cir.getReturnValue());
				if (fixed != null) // don't override if the "fixed" version is misisng
					cir.setReturnValue(getValueFromNullable(this.byLocation.get(fixed)));
		}
	}

	@Inject(
			method = "get(Lnet/minecraft/resources/ResourceKey;)Ljava/lang/Object;",
			at = @At("RETURN"),
			cancellable = true
	)
	private void fixedValue(@Nullable ResourceKey<T> key, CallbackInfoReturnable<@Nullable T> cir) {
		if (MainConfig.get().datafixer == true
			&& key != null) {
				var fixed = RegistryFixer.getFixedValueInRegistry(this, key.location(), cir.getReturnValue());
				if (fixed != null) // don't override if the "fixed" version is missing
					cir.setReturnValue(getValueFromNullable(this.byLocation.get(fixed)));

		}
	}
}
