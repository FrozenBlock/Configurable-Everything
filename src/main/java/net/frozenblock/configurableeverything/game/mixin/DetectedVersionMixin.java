package net.frozenblock.configurableeverything.game.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.frozenblock.configurableeverything.config.GameConfig;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.minecraft.DetectedVersion;
import net.minecraft.world.level.storage.DataVersion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DetectedVersion.class)
public class DetectedVersionMixin {

    @WrapOperation(method = "createFromJson", at = @At(value = "NEW", target = "(ILjava/lang/String;)Lnet/minecraft/world/level/storage/DataVersion;"))
    private static DataVersion createFromJson(int version, String series, Operation<DataVersion> operation) {
        var config = GameConfig.get();
		if (!MainConfig.get().game) return operation.call(version, series);
		var versionSeries = config.versionSeries;
		if (!versionSeries.isEmpty()) {
			return operation.call(version, versionSeries);
		}
		return operation.call(version, series);
    }

	@WrapOperation(method = "createBuiltIn(Ljava/lang/String;Ljava/lang/String;Z)Lnet/minecraft/WorldVersion;", at = @At(value = "NEW", target = "(ILjava/lang/String;)Lnet/minecraft/world/level/storage/DataVersion;"))
	private static DataVersion createFromConstants(int version, String series, Operation<DataVersion> operation) {
		var config = GameConfig.get();
		if (!MainConfig.get().game) return operation.call(version, series);
		var versionSeries = config.versionSeries;
		if (!versionSeries.isEmpty()) {
			return operation.call(version, versionSeries);
		}
		return operation.call(version, series);
	}
}
