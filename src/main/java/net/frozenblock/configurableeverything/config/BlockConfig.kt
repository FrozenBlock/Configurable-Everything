package net.frozenblock.configurableeverything.config

import com.mojang.serialization.Codec
import net.frozenblock.configurableeverything.block.util.DataBlock
import net.frozenblock.configurableeverything.block.util.MutableBlockSoundGroupOverwrite
import net.frozenblock.configurableeverything.block.util.MutableSoundType
import net.frozenblock.configurableeverything.util.*
import net.frozenblock.configurableeverything.util.CONFIG_FORMAT
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.vanillaId
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.xjs.XjsConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.SyncBehavior
import net.frozenblock.lib.config.api.sync.annotation.EntrySyncData
import net.frozenblock.lib.config.api.sync.annotation.UnsyncableConfig
import net.frozenblock.lib.shadow.blue.endless.jankson.annotation.SaveToggle
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.BarrelBlock
import net.minecraft.world.level.block.Blocks

private val SOUND_GROUP_OVERWRITES: TypedEntryType<MutableList<MutableBlockSoundGroupOverwrite>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        MutableBlockSoundGroupOverwrite.CODEC.mutListOf()
    )
)

private val DATA_BLOCKS: TypedEntryType<MutableList<DataBlock>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        DataBlock.CODEC.mutListOf()
    )
)

data class BlockConfig(
    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    var soundGroupOverwrites: TypedEntry<MutableList<MutableBlockSoundGroupOverwrite>> = TypedEntry.create(
        SOUND_GROUP_OVERWRITES,
        mutableListOf(
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
    ),

    @JvmField
    @SaveToggle(ENABLE_EXPERIMENTAL_FEATURES)
    var addedBlocks: TypedEntry<MutableList<DataBlock>> = TypedEntry.create(
        DATA_BLOCKS,
        mutableListOf(
            DataBlock(
                "bruh:bruh",
                Blocks.BARREL
            )
        )
    )
) {
    companion object : CEConfig<BlockConfig>(
        BlockConfig::class,
        "block"
    ) {

        init {
            ConfigRegistry.register(this)
        }

        @JvmStatic
        @JvmOverloads
        fun get(real: Boolean = false): BlockConfig = if (real) this.instance() else this.config()
    }
}
