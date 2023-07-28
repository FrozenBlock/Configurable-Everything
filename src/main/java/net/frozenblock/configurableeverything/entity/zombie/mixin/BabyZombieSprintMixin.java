package net.frozenblock.configurableeverything.entity.zombie.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.frozenblock.configurableeverything.config.EntityConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class BabyZombieSprintMixin {

	@ModifyExpressionValue(method = "canSpawnSprintParticle", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isSprinting()Z"))
	public boolean mcFixes$babyZombieSprint(boolean original) {
		return original ||
			(
				EntityConfig.get().zombie.babyZombieSprint
				&& Entity.class.cast(this) instanceof Zombie zombie
				&& zombie.isBaby()
				&& (zombie.minorHorizontalCollision || !zombie.horizontalCollision)
				&& !zombie.isFallFlying()
				&& zombie.onGround()
				&& zombie.getDeltaMovement().horizontalDistance() > 0.1
		);
	}

}
