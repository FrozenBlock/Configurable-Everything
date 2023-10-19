package net.frozenblock.configurableeverything.game.mixin;

import net.frozenblock.configurableeverything.config.GameConfig;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.minecraft.DetectedVersion;
import net.minecraft.world.level.storage.DataVersion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DetectedVersion.class)
public class DetectedVersionMixin {

    @Mutable
    @Shadow @Final private DataVersion worldVersion;

    @Inject(method = "<init>*", at = @At("TAIL"))
    private void clinit(CallbackInfo ci) {
        var config = GameConfig.get(false);
        if (MainConfig.get(false).game == true) {
            var series = config.versionSeries;
            if (series != null && !series.isEmpty()) {
                this.worldVersion = new DataVersion(this.worldVersion.getVersion(), series);
            }
        }
    }
}
