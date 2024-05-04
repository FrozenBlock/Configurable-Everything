package net.frozenblock.configurableeverything.tag.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class RegistryTagModification(
    @JvmField var registry: String,
    @JvmField var modifications: List<TagModification>
) {
    companion object {
        @JvmField
        val CODEC: Codec<RegistryTagModification> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.STRING.fieldOf("registry").forGetter(RegistryTagModification::registry),
                TagModification.CODEC.listOf().fieldOf("modifications").forGetter(RegistryTagModification::modifications),
            ).apply(instance, ::RegistryTagModification)
        }
    }
}