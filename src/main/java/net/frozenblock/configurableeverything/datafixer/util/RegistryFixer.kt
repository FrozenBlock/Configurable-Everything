package net.frozenblock.configurableeverything.datafixer.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.frozenblock.configurableeverything.config.DataFixerConfig
import net.frozenblock.configurableeverything.datafixer.util.DataFixerUtils.REGISTRY_FIXERS
import net.frozenblock.configurableeverything.util.UNSTABLE_LOGGING
import net.frozenblock.configurableeverything.util.log
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation

@JvmRecord
data class RegistryFixer(val registryKey: ResourceLocation?, val fixers: List<Fixer?>?) {
    companion object {
        @JvmField
		val CODEC: Codec<RegistryFixer> = RecordCodecBuilder.create { instance ->
            instance.group(
                ResourceLocation.CODEC.fieldOf("registry_key").forGetter(RegistryFixer::registryKey),
                Fixer.CODEC.listOf().fieldOf("fixers").forGetter(RegistryFixer::fixers)
            ).apply(instance, ::RegistryFixer)
        }

        @JvmStatic
        fun getFixedValueInRegistry(registry: Registry<*>?, name: ResourceLocation?, original: Any?): ResourceLocation? {
            if (original != null && !DataFixerConfig.get().overrideRealEntries)
                return null
            if (name != null) {
                val registryFixers: List<RegistryFixer?> = REGISTRY_FIXERS
                for (registryFixer in registryFixers) {
                    if (registryFixer?.registryKey == registry?.key()?.location()) {
                        if (registryFixer?.fixers == null) continue
                        for (fixer in registryFixer.fixers) {
                            if (fixer?.oldId == name) {
                                log(
                                    "Successfully changed old ID " + name + " to new ID " + fixer.newId,
                                    UNSTABLE_LOGGING
                                )
                                return fixer.newId
                            }
                        }
                    }
                }
            }
            return null
        }
    }
}
