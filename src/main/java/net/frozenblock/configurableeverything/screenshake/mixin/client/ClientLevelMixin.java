package net.frozenblock.configurableeverything.screenshake.mixin.client;

import net.frozenblock.configurableeverything.screenshake.util.ScreenShakeConfigUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin extends Level {

    protected ClientLevelMixin(WritableLevelData worldProperties, ResourceKey<Level> registryKey, RegistryAccess registryManager, Holder<DimensionType> dimension, Supplier<ProfilerFiller> profiler, boolean client, boolean debug, long seed, int maxChainedNeighborUpdates) {
        super(worldProperties, registryKey, registryManager, dimension, profiler, client, debug, seed, maxChainedNeighborUpdates);
    }

    @Inject(method = "playLocalSound", at = @At("TAIL"))
    private void playSound(double x, double y, double z, SoundEvent sound, SoundSource category, float volume, float pitch, boolean distanceDelay, CallbackInfo ci) {
        ScreenShakeConfigUtil.createScreenShake(this, x, y, z, sound);
    }
}
