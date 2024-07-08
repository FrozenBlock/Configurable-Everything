package net.frozenblock.configurableeverything.tag.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.frozenblock.configurableeverything.tag.util.TagLoaderExtension;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagLoader;
import net.minecraft.tags.TagManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import java.util.Optional;
import java.util.function.Function;

@Mixin(TagManager.class)
public class TagManagerMixin {

	@WrapOperation(method = "createLoader", at = @At(value = "NEW", target = "(Ljava/util/function/Function;Ljava/lang/String;)Lnet/minecraft/tags/TagLoader;"))
	private <T> TagLoader<T> setRegistry(Function<ResourceLocation, Optional<? extends T>> idToValue, String directory, Operation<TagLoader<T>> original, @Local Registry<T> registry) {
		TagLoader<T> loader = original.call(idToValue, directory);
		//noinspection unchecked
		TagLoaderExtension<T> extended = (TagLoaderExtension<T>) loader;
		extended.configurableEverything$setRegistry(registry);
		return loader;
	}
}
