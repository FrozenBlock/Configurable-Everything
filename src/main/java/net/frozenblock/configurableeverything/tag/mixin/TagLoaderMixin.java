package net.frozenblock.configurableeverything.tag.mixin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
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
import net.minecraft.tags.TagLoader;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.io.Reader;
import java.util.List;
import java.util.Map;

@Mixin(TagLoader.class)
public class TagLoaderMixin<T> implements TagLoaderExtension<T> {

	@Unique
	private Registry<T> registry;

	@WrapOperation(method = "load", at = @At(value = "INVOKE", target = "Lcom/google/gson/JsonParser;parseReader(Ljava/io/Reader;)Lcom/google/gson/JsonElement;"))
	private JsonElement shit(Reader jsonReader, Operation<JsonElement> original, @Local(ordinal = 1) ResourceLocation tag) {
        JsonObject json = (JsonObject) original.call(jsonReader);
		if (!ConfigurableEverythingSharedConstantsKt.ENABLE_EXPERIMENTAL_FEATURES || !MainConfig.get().tag)
			return json;

		TagConfig config = TagConfig.get();
		JsonArray values = json.getAsJsonArray("values");

		for (RegistryTagModification registryTagModification : config.tagModifications.value()) {
			if (this.registry.key().location().toString().equals(registryTagModification.registry)) {
				for (TagModification modification : registryTagModification.modifications) {
					if (tag.equals(ResourceLocation.tryParse(modification.tag))) {
						modification.removals.forEach(remove -> values.remove(new JsonPrimitive(remove)));
						modification.additions.forEach(values::add);
					}
				}
			}
		}

		return json;
    }

	@Override
	public void configurableEverything$setRegistry(@NotNull Registry<T> registry) {
		this.registry = registry;
	}
}
