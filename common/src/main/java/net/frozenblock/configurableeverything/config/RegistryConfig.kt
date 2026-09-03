package net.frozenblock.configurableeverything.config

import blue.endless.jankson.Comment
import net.frozenblock.configurableeverything.registry.util.BiomeAddition
import net.frozenblock.configurableeverything.registry.util.PlacedFeatureAddition
import net.frozenblock.configurableeverything.util.CEConfig
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.mutListOf
import net.frozenblock.lib.config.v2.entry.ConfigEntry
import net.frozenblock.lib.config.v2.entry.EntryType
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.world.attribute.EnvironmentAttributes
import net.minecraft.world.level.biome.Biome.BiomeBuilder
import net.minecraft.world.level.biome.BiomeGenerationSettings
import net.minecraft.world.level.biome.BiomeSpecialEffects
import net.minecraft.world.level.biome.MobSpawnSettings

private val BIOME_ADDITIONS: EntryType<MutableList<BiomeAddition>> = EntryType.create(
    BiomeAddition.CODEC.mutListOf(),
    BiomeAddition.STREAM_CODEC.apply(ByteBufCodecs.list())
)

private val PLACED_FEATURE_ADDITIONS: EntryType<MutableList<PlacedFeatureAddition>> = EntryType.create(
    PlacedFeatureAddition.CODEC.mutListOf(),
    PlacedFeatureAddition.STREAM_CODEC.apply(ByteBufCodecs.list())
)

object RegistryConfig : CEConfig("registry") {
    @JvmField
    @Comment("Adds these biomes to the biome registry on datapack load.")
    var biomeAdditions: ConfigEntry<MutableList<BiomeAddition>> = this.entry(
        "biomeAdditions",
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
    )

    @JvmField
    @Comment("Adds these placed features to the placed feature registry on datapack load.")
    var placedFeatureAdditions: ConfigEntry<MutableList<PlacedFeatureAddition>> = this.entry("placedFeatureAdditions",
        PLACED_FEATURE_ADDITIONS,
        mutableListOf() // cant make an example bc it requires a holder and the registry is dynamic
    )
}
