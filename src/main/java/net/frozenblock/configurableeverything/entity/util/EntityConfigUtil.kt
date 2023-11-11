package net.frozenblock.configurableeverything.entity.util

import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader
import net.frozenblock.configurableeverything.config.EntityConfig
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.lib.sound.api.FlyBySoundHub
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeInstance
import net.minecraft.world.entity.ai.attributes.AttributeMap
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.level.entity.EntityAccess

object EntityConfigUtil {

	fun init() {
        val config = EntityConfig.get()
        // only run this on client
        if (MainConfig.get().entity == true && FabricLoader.getInstance().environmentType == EnvType.CLIENT) {
            config.entityFlyBySounds?.value?.apply {
                for (sound in this) {
                    if (sound == null) continue
                    val optionalEntity = BuiltInRegistries.ENTITY_TYPE.getOptional(sound.entity)
                    if (optionalEntity.isPresent) {
                        val entity = optionalEntity.get()
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

    @JvmStatic
    fun <T : EntityAccess> addAttributeAmplifiers(entityAccess: T) {
        val config = EntityConfig.get()
        if (MainConfig.get().entity != false) return
        config.entityAttributeAmplifiers?.value()?.apply {
            val entityAttributeAmplifiers = this
            (entityAccess as? LivingEntity)?.apply {
                for (entityAttributeAmplifier in entityAttributeAmplifiers) {
                    val desiredEntity = entityAttributeAmplifier?.entity ?: continue
                    val desiredEntityName = entityAttributeAmplifier.entityName ?: continue
                    val amplifiers = entityAttributeAmplifier.amplifiers ?: continue
                    if (desiredEntity.location() != BuiltInRegistries.ENTITY_TYPE.getKey(this.type)) return
                    val desEntityNameComponent: Component = Component.literal(desiredEntityName)
                    if (desEntityNameComponent.string.isEmpty() || desEntityNameComponent == this.name) {
                        val attributes: AttributeMap = this.attributes
                        for (amplifier in amplifiers) {
                            val amplifierAttribute = amplifier?.attribute ?: continue
                            val amplifierAmplifier = amplifier.amplifier ?: continue
                            val attribute: AttributeInstance? = BuiltInRegistries.ATTRIBUTE.get(amplifierAttribute)?.let(attributes::getInstance)
                            attribute?.addTransientModifier(
                                AttributeModifier(
                                    "Configurable Everything Entity Config ${amplifierAttribute.location()} change to ${this.name}",
                                    amplifierAmplifier - 1.0,
                                    AttributeModifier.Operation.MULTIPLY_TOTAL
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
