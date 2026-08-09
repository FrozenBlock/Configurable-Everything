package net.frozenblock.configurableeverything.block.util

import net.frozenblock.configurableeverything.config.BlockConfig
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.lib.block.impl.sound.SoundTypeOverride
import net.frozenblock.lib.registry.FrozenLibRegistries
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.BuiltInRegistries
import org.quiltmc.qsl.frozenblock.core.registry.api.event.RegistryEvents
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

object BlockConfigUtil {

    fun init() {
        RegistryEvents.DYNAMIC_REGISTRY_SETUP.register { ctx ->
            if (!MainConfig.block.get()) return@register
            for (overwrite in BlockConfig.soundGroupOverwrites.get()) {
                val registry = ctx.registryManager().lookup(FrozenLibRegistries.SOUND_TYPE_OVERRIDE).getOrNull()
                if (registry != null) {
                    val holder = BuiltInRegistries.BLOCK.get(overwrite.blockId).getOrNull()
                    if (holder != null) {
                        ctx.register(
                            registry.key(), id(overwrite.blockId.let { it.namespace + '_' + it.path }),
                            { SoundTypeOverride(
                                HolderSet.direct(holder),
                                overwrite.soundOverwrite.immutable(),
                                Optional.empty()
                            ) }
                        )
                    }
                }
            }
        }
    }
}
