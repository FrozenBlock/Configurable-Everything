package net.frozenblock.configurableeverything.screenshake.mixin;

import net.frozenblock.configurableeverything.screenshake.util.ScreenShakeConfigUtil;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Level.class)
public abstract class LevelMixin implements LevelAccessor {

    @Inject(method = "playSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V", at = @At("TAIL"))
    private void playSound(Player player, double x, double y, double z, SoundEvent sound, SoundSource category, float volume, float pitch, CallbackInfo ci) {
        ScreenShakeConfigUtil.createScreenShake(Level.class.cast(this), x, y, z, sound);
    }
}
