package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.util.*
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.lib.config.v1.entry.TypedEntry
import net.frozenblock.lib.config.v1.entry.TypedEntryType
import net.frozenblock.lib.config.v1.registry.BasicConfigRegistry
import net.frozenblock.lib.config.v2.entry.ConfigEntry
import net.frozenblock.lib.config.v2.entry.EntryType
import net.frozenblock.lib.levelgen.surface.api.DimensionBoundRuleSource
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.dimension.BuiltinDimensionTypes
import net.minecraft.world.level.levelgen.SurfaceRules

private val SURFACE_RULE_LIST: EntryType<MutableList<DimensionBoundRuleSource>> = EntryType.create(
    DimensionBoundRuleSource.CODEC.mutListOf(),
    DimensionBoundRuleSource.STREAM_CODEC.apply(ByteBufCodecs.list())
)

object SurfaceRuleConfig : CEConfig("surface_rule") {
    @JvmField
    var addedSurfaceRules: ConfigEntry<MutableList<DimensionBoundRuleSource>> = this.entry("addedSurfaceRules",
        SURFACE_RULE_LIST,
        mutableListOf(
            DimensionBoundRuleSource(
                BuiltinDimensionTypes.OVERWORLD.identifier(),
                SurfaceRules.sequence(
                    SurfaceRules.ifTrue(
                        SurfaceRules.abovePreliminarySurface(),
                        SurfaceRules.state(Blocks.GRASS_BLOCK.defaultBlockState())
                    )
                )
            )
        )
    )
}
