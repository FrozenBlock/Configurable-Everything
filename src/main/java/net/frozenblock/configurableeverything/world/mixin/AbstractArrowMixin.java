package net.frozenblock.configurableeverything.world.mixin;

import net.frozenblock.configurableeverything.config.EntityConfig;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin extends Projectile {

	public AbstractArrowMixin(EntityType<? extends Projectile> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(method = "onHitBlock", at = @At("TAIL"))
	private void onHitBlock(BlockHitResult result, CallbackInfo ci) {
		if (MainConfig.get().entity && EntityConfig.get().flamingArrowsLightFire && AbstractArrow.class.cast(this).isOnFire()) {
			BlockPos blockPos = result.getBlockPos();
			BlockState blockState = this.level().getBlockState(blockPos);

			if (!CampfireBlock.canLight(blockState) && !CandleBlock.canLight(blockState) && !CandleCakeBlock.canLight(blockState)) {
				BlockPos blockPos2 = blockPos.relative(result.getDirection());
				if (BaseFireBlock.canBePlacedAt(this.level(), blockPos2, result.getDirection())) {
					BlockState blockState2 = BaseFireBlock.getState(this.level(), blockPos2);
					this.level().setBlock(blockPos2, blockState2, 11);
					this.level().gameEvent(this, GameEvent.BLOCK_PLACE, blockPos);
				}
			} else {
				this.level().setBlock(blockPos, blockState.setValue(BlockStateProperties.LIT, true), 11);
				this.level().gameEvent(this, GameEvent.BLOCK_CHANGE, blockPos);
			}
		}
	}
}
