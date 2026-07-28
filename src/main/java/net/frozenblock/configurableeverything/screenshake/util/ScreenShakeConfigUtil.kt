package net.frozenblock.configurableeverything.screenshake.util

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.config.ScreenShakeConfig
import net.frozenblock.configurableeverything.util.value
import net.frozenblock.lib.screenshake.api.ScreenShake
import net.frozenblock.lib.screenshake.api.ScreenShakes
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import kotlin.jvm.optionals.getOrNull

object ScreenShakeConfigUtil {

    // params are nullable bc it's called from Java which isn't very null safe
    @JvmStatic
    fun createScreenShake(level: Level?, x: Double?, y: Double?, z: Double?, sound: SoundEvent?) = runBlocking {
        if (!MainConfig.screen_shake.get()) return@runBlocking
        if (level == null || x == null || y == null || z == null || sound == null) return@runBlocking
        val offset = 0.001
        val entities: List<Entity> = level.getEntities(null, AABB(x - offset, y - offset, z - offset, x + offset, y + offset, z + offset))
        ScreenShakeConfig.soundScreenShakes.get().apply {
            for (shake in this) { launch {
                if (shake.sound == sound.location) {
                    if (entities.isEmpty()) { // apply to position if no entity is found
                        createVecShake(level, shake, Vec3(x, y, z))
                    } else { // find an entity to apply the screen shake to
                        val entity: Entity = entities.stream().findFirst().getOrNull() ?: return@launch
                        createEntityShake(level, entity, shake)
                    }
                }
            } }
        }
    }

    @JvmStatic
    fun createVecShake(
        level: Level,
        shake: SoundScreenShake,
        pos: Vec3
    ) {
        ScreenShakes.add(
            level,
            ScreenShake.builder(level, Vec3(pos.x, pos.y, pos.z))
                .intensity(shake.intensity)
                .duration(shake.duration)
                .falloffStartDuration(shake.falloffStart)
                .maxDistance(shake.maxDistance)
                .build()
        )
    }

    @JvmStatic
    fun createEntityShake(
        level: Level,
        entity: Entity,
        shake: SoundScreenShake
    ) {
        ScreenShakes.add(
            entity,
            ScreenShake.builder(entity)
                .intensity(shake.intensity)
                .duration(shake.duration)
                .falloffStartDuration(shake.falloffStart)
                .maxDistance(shake.maxDistance)
                .build()
        )
    }
}
