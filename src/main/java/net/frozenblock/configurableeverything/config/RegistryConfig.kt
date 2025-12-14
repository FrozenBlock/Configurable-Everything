package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.registry.util.BiomeAddition
import net.frozenblock.configurableeverything.registry.util.PlacedFeatureAddition
import net.frozenblock.configurableeverything.util.*
import net.frozenblock.configurableeverything.util.CONFIG_FORMAT
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.xjs.XjsConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.annotation.EntrySyncData
import net.frozenblock.lib.config.api.sync.annotation.UnsyncableConfig
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment
import net.minecraft.world.attribute.EnvironmentAttributes
import net.minecraft.world.level.biome.Biome.BiomeBuilder
import net.minecraft.world.level.biome.BiomeGenerationSettings
import net.minecraft.world.level.biome.BiomeSpecialEffects
import net.minecraft.world.level.biome.MobSpawnSettings

private val BIOME_ADDITIONS: TypedEntryType<MutableList<BiomeAddition>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        BiomeAddition.CODEC.mutListOf()
    )
)

private val PLACED_FEATURE_ADDITIONS: TypedEntryType<MutableList<PlacedFeatureAddition>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        PlacedFeatureAddition.CODEC.mutListOf()
    )
)

data class RegistryConfig(
    @JvmField
    @EntrySyncData("biomeAdditions")
    @Comment("Adds these biomes to the biome registry on datapack load.")
    var biomeAdditions: TypedEntry<MutableList<BiomeAddition>> = TypedEntry.create(
        BIOME_ADDITIONS,
        mutableListOf(
            BiomeAddition(
                id("example"),
                // copy of blank biome
                BiomeBuilder()
                    .temperature(0.5f)
                    .downfall(0f)
                    .hasPrecipitation(false)
                    .specialEffects(
                        BiomeSpecialEffects.Builder()
                            .grassColorOverride(8421504)
                            .foliageColorOverride(8421504)
                            .waterColor(0)
                            .build()
                    )
                    .setAttribute(EnvironmentAttributes.FOG_COLOR, 0)
                    .setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, 0)
                    .setAttribute(EnvironmentAttributes.SKY_COLOR, 0)
                    .mobSpawnSettings(MobSpawnSettings.EMPTY)
                    .generationSettings(BiomeGenerationSettings.EMPTY)
                    .build()
            )
        )
    ),

    @JvmField
    @EntrySyncData("placedFeatureAdditions")
    @Comment("Adds these placed features to the placed feature registry on datapack load.")
    var placedFeatureAdditions: TypedEntry<MutableList<PlacedFeatureAddition>> = TypedEntry.create(
        PLACED_FEATURE_ADDITIONS,
        mutableListOf() // cant make an example bc it requires a holder and the registry is dynamic
    )
) {
    companion object : CEConfig<RegistryConfig>(
        RegistryConfig::class,
        "registry"
    ) {

        init {
            ConfigRegistry.register(this)
        }

        @JvmStatic
        @JvmOverloads
        fun get(real: Boolean = false): RegistryConfig = if (real) this.instance() else this.config()
    }
}
