package net.frozenblock.configurableeverything.entity.mixin.skeleton;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.frozenblock.configurableeverything.config.EntityConfig;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractSkeleton.class)
public class SkeletonAccuracyMixin {

	@ModifyExpressionValue(method = "performRangedAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Difficulty;getId()I"))
	public int configurableEverything$newDifficultyIdForFiring(int original) {
		var skeleton = EntityConfig.get().skeleton
		return MainConfig.get().entity == true && skeleton != null && skeleton.skeletonAccuracyIgnoresDifficulty == true
			? Difficulty.HARD.getId()
			: original;
	}

}
