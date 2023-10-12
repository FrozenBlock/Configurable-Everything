package net.frozenblock.configurableeverything.config

import com.mojang.serialization.Codec
import net.frozenblock.configurableeverything.block.util.*
import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.configurableeverything.util.vanillaId
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.sound.api.block_sound_group.SoundCodecs
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.SoundType

data class BlockConfig(
    @JvmField
    var soundGroupOverwrites: TypedEntry<List<MutableBlockSoundGroupOverwrite?>>? = TypedEntry(
        SOUND_GROUP_OVERWRITES,
        listOf(
            MutableBlockSoundGroupOverwrite(
                vanillaId("grass_block"),
                MutableSoundType(
                    100F,
                    1F,
                    SoundEvents.HORSE_DEATH,
                    SoundEvents.HORSE_DEATH,
                    SoundEvents.HORSE_DEATH,
                    SoundEvents.HORSE_DEATH,
                    SoundEvents.HORSE_DEATH
                )
            ) { true }
        )
    )
) {
    companion object {
        private val SOUND_GROUP_OVERWRITES: TypedEntryType<List<MutableBlockSoundGroupOverwrite?>> = ConfigRegistry.register(
            TypedEntryType(
                MOD_ID,
                Codec.list(MutableBlockSoundGroupOverwrite.CODEC)
            )
        )

        @JvmField
        internal val INSTANCE: Config<BlockConfig> = ConfigRegistry.register(
            JsonConfig(
                MOD_ID,
                BlockConfig::class.java,
                makeConfigPath("block"),
                CONFIG_JSONTYPE
            )
        )

        @JvmStatic
        fun get(): BlockConfig = INSTANCE.config()
    }
}
