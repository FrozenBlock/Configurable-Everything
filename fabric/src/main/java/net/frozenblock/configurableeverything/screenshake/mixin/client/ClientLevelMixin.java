package net.frozenblock.configurableeverything.screenshake.mixin.client;

import java.util.function.Supplier;
import net.frozenblock.configurableeverything.screenshake.util.ScreenShakeConfigUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin extends Level {

	protected ClientLevelMixin(WritableLevelData properties, ResourceKey<Level> registryKey, RegistryAccess registryManager, Holder<DimensionType> dimension, boolean isClient, boolean debugWorld, long l, int i) {
		super(properties, registryKey, registryManager, dimension, isClient, debugWorld, l, i);
	}

	@Inject(method = "playLocalSound(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V", at = @At("TAIL"))
	private void playSound(Entity entity, SoundEvent sound, SoundSource category, float volume, float pitch, CallbackInfo ci) {
		ScreenShakeConfigUtil.createScreenShake(this, entity.getX(), entity.getY(), entity.getZ(), sound);
	}

    @Inject(method = "playLocalSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V", at = @At("TAIL"))
    private void playSound(double x, double y, double z, SoundEvent sound, SoundSource category, float volume, float pitch, boolean distanceDelay, CallbackInfo ci) {
        ScreenShakeConfigUtil.createScreenShake(this, x, y, z, sound);
    }
}
