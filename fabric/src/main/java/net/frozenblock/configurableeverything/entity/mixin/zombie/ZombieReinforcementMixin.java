package net.frozenblock.configurableeverything.entity.mixin.zombie;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.frozenblock.configurableeverything.config.EntityConfig;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.monster.zombie.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Zombie.class)
public class ZombieReinforcementMixin {

	@ModifyExpressionValue(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;getDifficulty()Lnet/minecraft/world/Difficulty;"))
	public Difficulty configurableEverything$ignoreReinforcementDifficulty(Difficulty original) {
		return MainConfig.entity.get() && EntityConfig.ignoreDoorBreakDifficulty.get() ? Difficulty.HARD : original;
	}

	@ModifyExpressionValue(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/zombie/Zombie;getAttributeValue(Lnet/minecraft/core/Holder;)D"))
	public double configurableEverything$ignoreReinforcementChance(double original) {
		return MainConfig.entity.get() && EntityConfig.fullReinforcementChance.get() ? 999D : original;
	}

}
