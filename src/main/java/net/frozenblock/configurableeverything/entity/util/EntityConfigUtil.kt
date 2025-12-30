package net.frozenblock.configurableeverything.entity.util

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader
import net.frozenblock.configurableeverything.config.EntityConfig
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.value
import net.frozenblock.lib.sound.client.impl.FlyBySoundHub
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeInstance
import net.minecraft.world.entity.ai.attributes.AttributeMap
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.level.entity.EntityAccess
import java.util.*
import kotlin.jvm.optionals.getOrNull

internal object EntityConfigUtil {

	internal fun init() {
        val config = EntityConfig.get()
        // only run this on client
        if (!MainConfig.get().entity || FabricLoader.getInstance().environmentType != EnvType.CLIENT) return
        for (sound in config.entityFlyBySounds.value) {
            val optionalEntity = BuiltInRegistries.ENTITY_TYPE.getOptional(sound.entity)
            if (optionalEntity.isPresent) {
                val entity = optionalEntity.get()
                val data = sound.sound
                var category: SoundSource? = null
                for (source in SoundSource.entries)
                    if (source.getName() == data.category) category = source

                val soundEvent: SoundEvent? = BuiltInRegistries.SOUND_EVENT.getValue(data.sound)
                if (category != null && soundEvent != null)
                    FlyBySoundHub.AUTO_ENTITIES_AND_SOUNDS[entity] =
                        FlyBySoundHub.FlyBySound(data.pitch, data.volume, category, soundEvent)
            }
        }
    }

    @JvmStatic
    internal fun <T : EntityAccess> addAttributeAmplifiers(entityAccess: T) {
        val config = EntityConfig.get()
        if (!MainConfig.get().entity) return
        if (entityAccess !is LivingEntity) return

        for (entityAttributeAmplifier in config.entityAttributeAmplifiers.value) {
            val desiredEntity = entityAttributeAmplifier.entity
            val desiredEntityName = entityAttributeAmplifier.entityName
            val amplifiers = entityAttributeAmplifier.amplifiers

            if (desiredEntity.location() != BuiltInRegistries.ENTITY_TYPE.getKey(entityAccess.type)) continue

            val desEntityNameComponent: Component = Component.literal(desiredEntityName)
            if (!(desEntityNameComponent.string.isEmpty() || desEntityNameComponent == entityAccess.name)) continue

            val attributes: AttributeMap = entityAccess.attributes
            for (amplifier in amplifiers) {
                val amplifierAttribute = amplifier.attribute
                val amplifierAmplifier = amplifier.amplifier
                val attributeHolder: Holder.Reference<Attribute> = BuiltInRegistries.ATTRIBUTE.get(amplifierAttribute).getOrNull() ?: continue
                val attribute: AttributeInstance? = attributeHolder.let(attributes::getInstance)
                attribute?.addTransientModifier(
                    AttributeModifier(
                        id("entity_config_change_to_${entityAccess.id}_${UUID.randomUUID().toString().replace('-', '_')}") ,
                        amplifierAmplifier - 1.0,
                        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                    )
                )
            }
        }
    }
}
