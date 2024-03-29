package net.frozenblock.configurableeverything.entity.util

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader
import net.frozenblock.configurableeverything.config.EntityConfig
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.value
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

internal object EntityConfigUtil {

	internal fun init() = runBlocking {
        val config = EntityConfig.get()
        // only run this on client
        if (MainConfig.get().entity != true || FabricLoader.getInstance().environmentType != EnvType.CLIENT) return@runBlocking
        config.entityFlyBySounds?.value?.apply {
            for (sound in this) { launch {
                if (sound == null) return@launch
                val optionalEntity = BuiltInRegistries.ENTITY_TYPE.getOptional(sound.entity)
                if (optionalEntity.isPresent) {
                    val entity = optionalEntity.get()
                    val data = sound.sound
                    var category: SoundSource? = null
                    for (source in SoundSource.entries)
                        if (source.getName() == data.category) category = source

                    val soundEvent: SoundEvent? = BuiltInRegistries.SOUND_EVENT.get(data.sound)
                    if (category != null && soundEvent != null)
                        FlyBySoundHub.AUTO_ENTITIES_AND_SOUNDS[entity] =
                            FlyBySoundHub.FlyBySound(data.pitch, data.volume, category, soundEvent)
                }
            } }
        }
    }

    @JvmStatic
    internal fun <T : EntityAccess> addAttributeAmplifiers(entityAccess: T) = runBlocking {
        val config = EntityConfig.get()
        if (MainConfig.get().entity != true) return@runBlocking
        config.entityAttributeAmplifiers?.value()?.apply {
            val entityAttributeAmplifiers = this
            (entityAccess as? LivingEntity)?.apply {
                for (entityAttributeAmplifier in entityAttributeAmplifiers) { launch {
                    val desiredEntity = entityAttributeAmplifier?.entity ?: return@launch
                    val desiredEntityName = entityAttributeAmplifier.entityName ?: return@launch
                    val amplifiers = entityAttributeAmplifier.amplifiers ?: return@launch
                    if (desiredEntity.location() != BuiltInRegistries.ENTITY_TYPE.getKey(this@apply.type)) return@launch
                    val desEntityNameComponent: Component = Component.literal(desiredEntityName)
                    if (desEntityNameComponent.string.isEmpty() || desEntityNameComponent == this@apply.name) {
                        val attributes: AttributeMap = this@apply.attributes
                        for (amplifier in amplifiers) { launch {
                            val amplifierAttribute = amplifier?.attribute ?: return@launch
                            val amplifierAmplifier = amplifier.amplifier ?: return@launch
                            val attribute: AttributeInstance? = BuiltInRegistries.ATTRIBUTE.get(amplifierAttribute)?.let(attributes::getInstance)
                            attribute?.addTransientModifier(
                                AttributeModifier(
                                    "Configurable Everything Entity Config ${amplifierAttribute.location()} change to ${this@apply.name}",
                                    amplifierAmplifier - 1.0,
                                    AttributeModifier.Operation.MULTIPLY_TOTAL
                                )
                            )
                        } }
                    }
                } }
            }
        }
    }
}
