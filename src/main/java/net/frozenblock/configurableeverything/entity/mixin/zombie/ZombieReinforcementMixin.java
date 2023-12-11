package net.frozenblock.configurableeverything.entity.mixin.zombie;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.frozenblock.configurableeverything.config.EntityConfig;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.monster.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Zombie.class)
public class ZombieReinforcementMixin {

	@ModifyExpressionValue(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getDifficulty()Lnet/minecraft/world/Difficulty;"))
	public Difficulty configurableEverything$ignoreReinforcementDifficulty(Difficulty original) {
		var zombie = EntityConfig.get().zombie;
		return MainConfig.get().entity == true && zombie != null && zombie.ignoreDoorBreakDifficulty == true ? Difficulty.HARD : original;
	}

	@ModifyExpressionValue(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Zombie;getAttributeValue(Lnet/minecraft/world/entity/ai/attributes/Attribute;)D"))
	public double configurableEverything$ignoreReinforcementChance(double original) {
		var zombie = EntityConfig.get().zombie;
		return MainConfig.get().entity == true && zombie != null && zombie.fullReinforcementChance == true ? 999D : original;
	}

}
