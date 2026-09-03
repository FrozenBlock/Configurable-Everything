package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.sculk_spreading.util.SculkGrowth
import net.frozenblock.configurableeverything.util.*
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.lib.config.v1.entry.TypedEntry
import net.frozenblock.lib.config.v1.entry.TypedEntryType
import net.frozenblock.lib.config.v1.registry.BasicConfigRegistry
import blue.endless.jankson.Comment
import net.frozenblock.lib.config.v2.entry.ConfigEntry
import net.frozenblock.lib.config.v2.entry.EntryType
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SculkShriekerBlock

private val SCULK_GROWTH_LIST: EntryType<MutableList<SculkGrowth>> = EntryType.create(
    SculkGrowth.CODEC.mutListOf(),
    SculkGrowth.STREAM_CODEC.apply(ByteBufCodecs.list())
)

object SculkSpreadingConfig : CEConfig("sculk_spreading") {

    @JvmField
    @Comment("List of growth block states.")
    var growths: ConfigEntry<MutableList<SculkGrowth>> = this.entry("growths",
        SCULK_GROWTH_LIST,
        arrayListOf(
            SculkGrowth(
                true,
                11,
                Blocks.SCULK_SHRIEKER.defaultBlockState().setValue(SculkShriekerBlock.CAN_SUMMON, true)
            ),
            SculkGrowth(false, 11, Blocks.SCULK_SHRIEKER.defaultBlockState()),
            SculkGrowth(false, 0, Blocks.SCULK_SENSOR.defaultBlockState()),
        )
    )
}
