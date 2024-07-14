package net.frozenblock.configurableeverything.tag.mixin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Either;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.config.TagConfig;
import net.frozenblock.configurableeverything.tag.util.RegistryTagModification;
import net.frozenblock.configurableeverything.tag.util.TagLoaderExtension;
import net.frozenblock.configurableeverything.tag.util.TagModification;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstantsKt;
import net.minecraft.core.Registry;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagLoader;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.Reader;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Mixin(TagLoader.class)
public class TagLoaderMixin<T> implements TagLoaderExtension<T> {

	@Unique
	private Registry<T> registry;

	@WrapOperation(method = "load", at = @At(value = "INVOKE", target = "Lcom/google/gson/JsonParser;parseReader(Ljava/io/Reader;)Lcom/google/gson/JsonElement;"))
	private JsonElement modifyTags(Reader jsonReader, Operation<JsonElement> original, @Local(ordinal = 1) ResourceLocation tag) {
        JsonObject json = (JsonObject) original.call(jsonReader);
		if (!MainConfig.get().tag)
			return json;

		TagConfig config = TagConfig.get();
		JsonArray values = json.getAsJsonArray("values");

		for (RegistryTagModification registryTagModification : config.tagModifications.value()) {
			if (this.registry.key().location().toString().equals(registryTagModification.registry)) {
				for (TagModification modification : registryTagModification.modifications) {
					if (tag.equals(ResourceLocation.tryParse(modification.tag))) {
						modification.removals.forEach(remove -> values.remove(new JsonPrimitive(new ResourceLocation(remove).toString())));
						modification.additions.forEach(add -> values.add(new ResourceLocation(add).toString()));
					}
				}
			}
		}

		return json;
    }

	@Inject(method = "build(Lnet/minecraft/tags/TagEntry$Lookup;Ljava/util/List;)Lcom/mojang/datafixers/util/Either;", at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z"))
	private void ignoreInvalidTags(TagEntry.Lookup<T> lookup, List<TagLoader.EntryWithSource> list, CallbackInfoReturnable<Either<Collection<TagLoader.EntryWithSource>, Collection<T>>> cir, @Local(ordinal = 1) List<TagLoader.EntryWithSource> list2) {
		if (!ConfigurableEverythingSharedConstantsKt.ENABLE_EXPERIMENTAL_FEATURES || !MainConfig.get().tag) {
			return;
		}

		if (TagConfig.get().ignoreInvalidEntries) {
			list2.clear();
		}
	}

	@Override
	public void configurableEverything$setRegistry(@NotNull Registry<T> registry) {
		this.registry = registry;
	}
}
