/*package net.frozenblock.configurableeverything.world.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.config.WorldConfig;
import net.frozenblock.configurableeverything.world.impl.ClientLevelDataInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelTimeAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelTimeAccess.class)
public interface LevelTimeAccessMixin {

	@ModifyReturnValue(method = "getTimeOfDay", at = @At("RETURN"))
    default float getTimeOfDay(float original, float partialTick) {
		if (LevelTimeAccess.class.cast(this) instanceof ClientLevel clientLevel && MainConfig.get().world && WorldConfig.get().fixSunMoonRotating) {
			return Mth.lerp(partialTick, clientLevel.dimensionType().timeOfDay(((ClientLevelDataInterface)clientLevel.getLevelData()).configurableEverything$getPreviousDayTime()), original);
		}
		return original;
	}
}
*/
