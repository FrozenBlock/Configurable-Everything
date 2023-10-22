package net.frozenblock.configurableeverything.screenshake.util

import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.config.ScreenShakeConfig
import net.frozenblock.lib.screenshake.api.ScreenShakeManager
import net.frozenblock.lib.screenshake.api.client.ScreenShaker
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

object ScreenShakeConfigUtil {

    @JvmStatic
    fun createScreenShake(level: Level?, x: Double?, y: Double?, z: Double?, sound: SoundEvent?) {
        val config = ScreenShakeConfig.get()
        if (MainConfig.get().screen_shake == true) {
            if (level == null || x == null || y == null || z == null || sound == null) return
            val offset = 0.001
            val entities: List<Entity>? = level.getEntities(null, AABB(x - offset, y - offset, z - offset, x + offset, y + offset, z + offset))
            config.soundScreenShakes?.value?.apply {
                for (shake in this) {
                    if (shake == null) continue
                    if (shake.sound == sound.location) {
                        if (entities?.isEmpty() == true) { // apply to position if no entity is found
                            if (level.isClientSide) {
                                val client = level as ClientLevel
                                ScreenShaker.SCREEN_SHAKES.add(
                                        ScreenShaker.ClientScreenShake(
                                                client, shake.intensity, shake.duration, shake.falloffStart, Vec3(x, y, z), shake.maxDistance, 0
                                        )
                                )
                            } else {
                                ScreenShakeManager.addScreenShake(level, shake.intensity, shake.duration, shake.falloffStart, x, y, z, shake.maxDistance)
                            }
                        } else { // find an entity to apply the screen shake to
                            val entity: Entity = entities?.stream()?.findFirst()?.get() ?: return
                            if (level.isClientSide) {
                                ScreenShaker.SCREEN_SHAKES.add(
                                        ScreenShaker.ClientEntityScreenShake(
                                                entity, shake.intensity, shake.duration, shake.falloffStart, shake.maxDistance, 0
                                        )
                                )
                            } else {
                                ScreenShakeManager.addEntityScreenShake(entity, shake.intensity, shake.duration, shake.falloffStart, shake.maxDistance)
                            }
                        }
                    }
                }
            }
        }
    }
}
