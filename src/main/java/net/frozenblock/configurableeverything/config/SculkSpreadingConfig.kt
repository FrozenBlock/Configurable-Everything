@file:Environment(EnvType.CLIENT)

package net.frozenblock.configurableeverything.config

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.sculk_spreading.util.SculkGrowth
import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.annotation.EntrySyncData
import net.frozenblock.lib.config.api.sync.annotation.UnsyncableConfig
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SculkShriekerBlock

private val SCULK_GROWTH_LIST: TypedEntryType<List<SculkGrowth?>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        SculkGrowth.CODEC.listOf()
    )
)

@UnsyncableConfig
data class SculkSpreadingConfig(

    @JvmField
    @EntrySyncData("activators")
    @Comment("List of growth block states.")
    var growths: TypedEntry<List<SculkGrowth?>>? = TypedEntry(
        SCULK_GROWTH_LIST,
        arrayListOf(
            SculkGrowth(true, 11, Blocks.SCULK_SHRIEKER.defaultBlockState().setValue(SculkShriekerBlock.CAN_SUMMON, true)),
            SculkGrowth(false, 11, Blocks.SCULK_SHRIEKER.defaultBlockState()),
            SculkGrowth(false, 0, Blocks.SCULK_SENSOR.defaultBlockState()),
        ),
    )
) {
    companion object : JsonConfig<SculkSpreadingConfig>(
        MOD_ID,
        SculkSpreadingConfig::class.java,
        makeConfigPath("sculk_spreading"),
        CONFIG_JSONTYPE,
        null,
        null
    ) {

        init {
            ConfigRegistry.register(this)
        }

        @JvmStatic
        @JvmOverloads
        fun get(real: Boolean = false): SculkSpreadingConfig = if (real) this.instance() else this.config()
    }
}
