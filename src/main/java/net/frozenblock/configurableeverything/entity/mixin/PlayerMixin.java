package net.frozenblock.configurableeverything.entity.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.frozenblock.configurableeverything.config.EntityConfig;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

	protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}

	@ModifyReturnValue(method = "getDestroySpeed", at = @At("RETURN"))
	private float getDestroySpeed(float original, BlockState state) {
		if (MainConfig.get().entity == true) {
			return (float) (original * EntityConfig.get().player.getDigSpeed());
		}
		return original;
	}
}
