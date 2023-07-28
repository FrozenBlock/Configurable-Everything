package net.frozenblock.configurableeverything.screenshake.util;

import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.config.ScreenShakeConfig;
import net.frozenblock.lib.screenshake.api.ScreenShakeManager;
import net.frozenblock.lib.screenshake.api.client.ScreenShaker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ScreenShakeConfigUtil {

    public static void createScreenShake(Level level, double x, double y, double z, SoundEvent sound) {
        var config = ScreenShakeConfig.get();
        if (MainConfig.get().screenShake) {
            double offset = 0.001;
            List<Entity> entities = level.getEntities(null, new AABB(x - offset, y - offset, z - offset, x + offset, y + offset, z + offset));
            if (config.soundScreenShakes != null && config.soundScreenShakes.value() != null) {
                var shakes = config.soundScreenShakes.value();
                for (SoundScreenShake shake : shakes) {
                    if (shake.sound().equals(sound.getLocation())) {
                        if (entities.isEmpty()) { // find an entity to apply the screen shake to
                            if (level.isClientSide) {
                                var client = (ClientLevel) level;
                                ScreenShaker.SCREEN_SHAKES.add(
                                        new ScreenShaker.ClientScreenShake(
                                                client, shake.intensity(), shake.duration(), shake.falloffStart(), new Vec3(x, y, z), shake.maxDistance(), 0
                                        )
                                );
                            } else {
                                ScreenShakeManager.addScreenShake(level, shake.intensity(), shake.duration(), shake.falloffStart(), x, y, z, shake.maxDistance());
                            }
                        } else { // apply the screen shake to the position instead
                            var entity = entities.stream().findFirst().get();
                            if (level.isClientSide) {
                                ScreenShaker.SCREEN_SHAKES.add(
                                        new ScreenShaker.ClientEntityScreenShake(
                                                entity, shake.intensity(), shake.duration(), shake.falloffStart(), shake.maxDistance(), 0
                                        )
                                );
                            } else {
                                ScreenShakeManager.addEntityScreenShake(entity, shake.intensity(), shake.duration(), shake.falloffStart(), shake.maxDistance());
                            }
                        }
                    }
                }
            }
        }
    }
}
