package net.frozenblock.configurableeverything.sound.util

import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.config.SoundConfig
import net.frozenblock.lib.sound.api.FlyBySoundHub
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource

object SoundConfigUtil {

    @JvmStatic
	fun init() {
        val config = SoundConfig.get()
        // only run this on client
        if (MainConfig.get().sound && FabricLoader.getInstance().environmentType == EnvType.CLIENT) {
            if (config.entityFlyBySounds?.value != null) {
                val flyBySounds: List<EntityFlyBySound> = config.entityFlyBySounds.value
                for (sound in flyBySounds) {
                    val optionalEntity = BuiltInRegistries.ENTITY_TYPE.getOptional(sound.entity)
                    if (optionalEntity.isPresent) {
                        val entity = optionalEntity.get();
                        val data = sound.sound
                        var category: SoundSource? = null
                        for (source in SoundSource.entries) {
                            if (source.getName() == data.category) category = source
                        }
                        val soundEvent: SoundEvent? = BuiltInRegistries.SOUND_EVENT.get(data.sound)
                        if (category != null && soundEvent != null)
                            FlyBySoundHub.AUTO_ENTITIES_AND_SOUNDS[entity] =
                                FlyBySoundHub.FlyBySound(data.pitch, data.volume, category, soundEvent)
                    }
                }
            }
        }
    }
}
