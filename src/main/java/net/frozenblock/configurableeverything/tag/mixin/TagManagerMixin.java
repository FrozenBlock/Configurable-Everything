package net.frozenblock.configurableeverything.tag.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.tag.util.TagLoaderExtension;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.tags.TagLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TagLoader.class)
public class TagManagerMixin {

	@Unique
	private static <T> TagLoader<Holder<T>> setRegistry(TagLoader<Holder<T>> loader, Registry<T> registry) {
		//noinspection unchecked
		TagLoaderExtension<T> extended = (TagLoaderExtension<T>) loader;
		extended.configurableEverything$setRegistry(registry);
		return loader;
	}

	@WrapOperation(method = "loadTagsForRegistry", at = @At(value = "NEW", target = "(Lnet/minecraft/tags/TagLoader$ElementLookup;Ljava/lang/String;)Lnet/minecraft/tags/TagLoader;"))
	private static <T> TagLoader<Holder<T>> setRegistry(TagLoader.ElementLookup<T> elementLookup, String dataType, Operation<TagLoader<Holder<T>>> original, @Local(argsOnly = true) WritableRegistry<T> registry) {
		TagLoader<Holder<T>> loader = original.call(elementLookup, dataType);

		if (!MainConfig.get().tag) {
			return loader;
		}

		return setRegistry(loader, registry);
	}

	@WrapOperation(method = "loadPendingTags", at = @At(value = "NEW", target = "(Lnet/minecraft/tags/TagLoader$ElementLookup;Ljava/lang/String;)Lnet/minecraft/tags/TagLoader;"))
	private static <T> TagLoader<Holder<T>> setRegistry(TagLoader.ElementLookup<T> elementLookup, String dataType, Operation<TagLoader<Holder<T>>> original, @Local(argsOnly = true) Registry<T> registry) {
		TagLoader<Holder<T>> loader = original.call(elementLookup, dataType);

		if (!MainConfig.get().tag) {
			return loader;
		}

		return setRegistry(loader, registry);
	}
}
