package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.datagen.ConfigurableEverythingDataGenerator
import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.instance.json.JsonType
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.worldgen.surface.api.FrozenDimensionBoundRuleSource
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.dimension.BuiltinDimensionTypes
import net.minecraft.world.level.levelgen.SurfaceRules

class SurfaceRuleConfig {
    companion object {
        private val SURFACE_RULE_LIST = ConfigRegistry.register(
            TypedEntryType<List<FrozenDimensionBoundRuleSource>>(
                MOD_ID,
                FrozenDimensionBoundRuleSource.CODEC.listOf()
            )
        )

        internal val INSTANCE: Config<SurfaceRuleConfig> = ConfigRegistry.register(
            JsonConfig(
                MOD_ID,
                SurfaceRuleConfig::class.java,
                makeConfigPath("surface_rule"),
                CONFIG_JSONTYPE
            )
        )

        fun get(): SurfaceRuleConfig = INSTANCE.config()
    }

    var addedSurfaceRules: TypedEntry<List<FrozenDimensionBoundRuleSource>>? = TypedEntry(
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
}
