package net.frozenblock.configurableeverything.sculk_spreading.mixin;

import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.config.SculkSpreadingConfig;
import net.frozenblock.configurableeverything.sculk_spreading.util.SculkGrowth;
import net.frozenblock.configurableeverything.sculk_spreading.util.SculkSpreadingConfigUtil;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstantsKt;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtilsKt;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.SculkBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SculkBlock.class)
public class SculkBlockMixin {

	@SuppressWarnings("IfStatementWithIdenticalBranches")
    @ModifyVariable(
		method = "getRandomGrowthState",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/block/state/BlockState;hasProperty(Lnet/minecraft/world/level/block/state/properties/Property;)Z"
		),
		ordinal = 0
	)
	private BlockState growthState(BlockState original, LevelAccessor level, BlockPos pos, RandomSource random, boolean isWorldGeneration) {
		if (!ConfigurableEverythingSharedConstantsKt.ENABLE_EXPERIMENTAL_FEATURES) return original;

		return SculkSpreadingConfigUtil.INSTANCE.growthState(original, random, isWorldGeneration);
    }
}
