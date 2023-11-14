package net.frozenblock.configurableeverything.screenshake.util

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.launch
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

    // params are nullable bc it's called from Java which isn't very null safe
    @JvmStatic
    fun createScreenShake(level: Level?, x: Double?, y: Double?, z: Double?, sound: SoundEvent?) = runBlocking {
        val config = ScreenShakeConfig.get()
        if (MainConfig.get().screen_shake != true) return@runBlocking
        if (level == null || x == null || y == null || z == null || sound == null) return@runBlocking
        val offset = 0.001
        val entities: List<Entity>? = level.getEntities(null, AABB(x - offset, y - offset, z - offset, x + offset, y + offset, z + offset))
        config.soundScreenShakes?.value?.apply {
            for (shake in this) {
                launch {
                    if (shake == null) return@launch
                    if (shake.sound == sound.location) {
                        if (entities?.isEmpty() != false) { // apply to position if no entity is found
                            createVecShake(level, shake, Vec3(x, y, z))
                        } else { // find an entity to apply the screen shake to
                            val entity: Entity = entities?.stream()?.findFirst()?.get() ?: return@launch
                            createEntityShake(level, entity, shake)
                        }
                    }
                }
            }
        }
    }

    @JvmStatic
    fun createVecShake(
        level: Level,
        shake: SoundScreenShake,
        pos: Vec3
    ) {
        if (level is ClientLevel) {
            ScreenShaker.SCREEN_SHAKES.add(
                ScreenShaker.ClientScreenShake(
                    level,
                    shake.intensity,
                    shake.duration,
                    shake.falloffStart,
                    pos,
                    shake.maxDistance,
                    0
                )
            )
        } else {
            ScreenShakeManager.addScreenShake(
                level,
                shake.intensity,
                shake.duration,
                shake.falloffStart,
                pos.x,
                pos.y,
                pos.z,
                shake.maxDistance
            )
        }
    }

    @JvmStatic
    fun createEntityShake(
        level: Level,
        entity: Entity,
        shake: SoundScreenShake
    ) {
        if (level is ClientLevel) {
            ScreenShaker.SCREEN_SHAKES.add(
                ScreenShaker.ClientEntityScreenShake(
                    entity,
                    shake.intensity,
                    shake.duration,
                    shake.falloffStart,
                    pos,
                    shake.maxDistance,
                    0
                )
            )
        } else {
            ScreenShakeManager.addEntityScreenShake(
                entity,
                shake.intensity,
                shake.duration,
                shake.falloffStart,
                shake.maxDistance
            )
        }
    }
}
