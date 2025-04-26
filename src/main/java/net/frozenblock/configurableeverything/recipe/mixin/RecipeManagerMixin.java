package net.frozenblock.configurableeverything.recipe.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.config.RecipeConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.SortedMap;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {

	@Inject(
		method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Lnet/minecraft/world/item/crafting/RecipeMap;",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/ArrayList;<init>(I)V"
		)
	)
	private void removeRecipes(ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfoReturnable<RecipeMap> cir, @Local SortedMap<ResourceLocation, Recipe<?>> map) {
		RecipeConfig config = RecipeConfig.get();
		if (!MainConfig.get().recipe) return;

		for (ResourceLocation recipe : config.removedRecipes.value()) {
			map.remove(recipe);
		}
	}
}
