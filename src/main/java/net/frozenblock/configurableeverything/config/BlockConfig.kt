package net.frozenblock.configurableeverything.config

import com.mojang.serialization.Codec
import net.frozenblock.configurableeverything.block.util.MutableBlockSoundGroupOverwrite
import net.frozenblock.configurableeverything.block.util.MutableSoundType
import net.frozenblock.configurableeverything.util.CONFIG_FORMAT
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.configurableeverything.util.vanillaId
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.xjs.XjsConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.SyncBehavior
import net.frozenblock.lib.config.api.sync.annotation.EntrySyncData
import net.frozenblock.lib.config.api.sync.annotation.UnsyncableConfig
import net.minecraft.sounds.SoundEvents

private val SOUND_GROUP_OVERWRITES: TypedEntryType<List<MutableBlockSoundGroupOverwrite>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        Codec.list(MutableBlockSoundGroupOverwrite.CODEC)
    )
)

@UnsyncableConfig
data class BlockConfig(
    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    var soundGroupOverwrites: TypedEntry<List<MutableBlockSoundGroupOverwrite>> = TypedEntry.create(
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
    companion object : XjsConfig<BlockConfig>(
        MOD_ID,
        BlockConfig::class.java,
        makeConfigPath("block"),
        CONFIG_FORMAT
    ) {

        init {
            ConfigRegistry.register(this)
        }

        @JvmStatic
        @JvmOverloads
        fun get(real: Boolean = false): BlockConfig = if (real) this.instance() else this.config()
    }
}
