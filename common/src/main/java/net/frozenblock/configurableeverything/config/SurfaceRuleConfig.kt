package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.util.*
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.levelgen.surface.api.DimensionBoundRuleSource
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.dimension.BuiltinDimensionTypes
import net.minecraft.world.level.levelgen.SurfaceRules

private val SURFACE_RULE_LIST: TypedEntryType<MutableList<DimensionBoundRuleSource>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        DimensionBoundRuleSource.CODEC.mutListOf()
    )
)

data class SurfaceRuleConfig(
    @JvmField
    var addedSurfaceRules: TypedEntry<MutableList<DimensionBoundRuleSource>> = TypedEntry.create(
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
) {
    companion object : CESimpleConfig<SurfaceRuleConfig>(
        SurfaceRuleConfig::class,
        "surface_rule"
    ) {

        init {
            ConfigRegistry.register(this)
        }

        @JvmStatic
        @JvmOverloads
        fun get(real: Boolean = false): SurfaceRuleConfig = if (real) this.instance() else this.config()
    }
}
