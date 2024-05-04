package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.datagen.ConfigurableEverythingDataGenerator
import net.frozenblock.configurableeverything.util.CEConfig
import net.frozenblock.configurableeverything.util.CONFIG_FORMAT
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.xjs.XjsConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.annotation.EntrySyncData
import net.frozenblock.lib.config.api.sync.annotation.UnsyncableConfig
import net.frozenblock.lib.worldgen.surface.api.FrozenDimensionBoundRuleSource
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.dimension.BuiltinDimensionTypes
import net.minecraft.world.level.levelgen.SurfaceRules

private val SURFACE_RULE_LIST: TypedEntryType<List<FrozenDimensionBoundRuleSource>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        FrozenDimensionBoundRuleSource.CODEC.listOf()
    )
)

@UnsyncableConfig
data class SurfaceRuleConfig(
    @JvmField
    @EntrySyncData("addedSurfaceRules")
    var addedSurfaceRules: TypedEntry<List<FrozenDimensionBoundRuleSource>> = TypedEntry.create(
        SURFACE_RULE_LIST,
        listOf(
            FrozenDimensionBoundRuleSource(
                BuiltinDimensionTypes.OVERWORLD.location(),
                SurfaceRules.sequence(
                    SurfaceRules.ifTrue(
                        SurfaceRules.isBiome(ConfigurableEverythingDataGenerator.BLANK_BIOME),
                        SurfaceRules.ifTrue(
                            SurfaceRules.abovePreliminarySurface(),
                            SurfaceRules.state(Blocks.GRASS_BLOCK.defaultBlockState())
                        )
                    )
                )
            )
        )
    )
) {
    companion object : CEConfig<SurfaceRuleConfig>(
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
