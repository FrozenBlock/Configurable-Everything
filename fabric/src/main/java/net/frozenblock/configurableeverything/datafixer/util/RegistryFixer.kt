package net.frozenblock.configurableeverything.datafixer.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.frozenblock.configurableeverything.util.mutListOf
import net.frozenblock.configurableeverything.config.DataFixerConfig
import net.frozenblock.configurableeverything.datafixer.util.DataFixerUtil.REGISTRY_FIXERS
import net.frozenblock.configurableeverything.util.UNSTABLE_LOGGING
import net.frozenblock.configurableeverything.util.log
import net.minecraft.core.Registry
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier

data class RegistryFixer(
    @JvmField var registryKey: Identifier,
    @JvmField var fixers: MutableList<Fixer>
) {
    companion object {
        @JvmField
		val CODEC: Codec<RegistryFixer> = RecordCodecBuilder.create { instance ->
            instance.group(
                Identifier.CODEC.fieldOf("registry_key").forGetter(RegistryFixer::registryKey),
                Fixer.CODEC.mutListOf().fieldOf("fixers").forGetter(RegistryFixer::fixers)
            ).apply(instance, ::RegistryFixer)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, RegistryFixer> = StreamCodec.composite(
            Identifier.STREAM_CODEC, RegistryFixer::registryKey,
            ByteBufCodecs.list<ByteBuf, Fixer>().apply(Fixer.STREAM_CODEC), RegistryFixer::fixers,
            ::RegistryFixer
        )

        @JvmStatic
        fun getFixedValueInRegistry(registry: Registry<*>, name: Identifier?, original: Any?): Identifier? {
            if (original != null && !DataFixerConfig.overrideRealEntries.get())
                return null
            if (name == null) return null
            val registryFixers: List<RegistryFixer> = REGISTRY_FIXERS
            for (registryFixer in registryFixers) {
                if (registryFixer.registryKey == registry.key().identifier()) {
                    for (fixer in registryFixer.fixers) {
                        if (fixer.oldId == name) {
                            log(
                                "Successfully changed old ID " + name + " to new ID " + fixer.newId,
                                UNSTABLE_LOGGING
                            )
                            return fixer.newId
                        }
                    }
                }
            }
            return null
        }
    }
}
