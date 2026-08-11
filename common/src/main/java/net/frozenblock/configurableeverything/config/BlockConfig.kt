package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.block.util.MutableBlockSoundGroupOverwrite
import net.frozenblock.configurableeverything.block.util.MutableSoundType
import net.frozenblock.configurableeverything.util.*
import net.frozenblock.configurableeverything.util.vanillaId
import net.frozenblock.lib.config.v2.entry.ConfigEntry
import net.frozenblock.lib.config.v2.entry.EntryType
import net.minecraft.sounds.SoundEvents
import java.util.Optional

private val SOUND_GROUP_OVERWRITES: EntryType<MutableBlockSoundGroupOverwrite> = EntryType.create(
    MutableBlockSoundGroupOverwrite.CODEC,
    MutableBlockSoundGroupOverwrite.STREAM_CODEC,
)

object BlockConfig : CEConfig("block") {
    @JvmField
    var soundGroupOverwrites: ConfigEntry<MutableList<MutableBlockSoundGroupOverwrite>> = this.unsyncableEntry("soundGroupOverwrites",
        SOUND_GROUP_OVERWRITES.asList(),
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
                ),
                Optional.empty()
            )
        )
    )
}
