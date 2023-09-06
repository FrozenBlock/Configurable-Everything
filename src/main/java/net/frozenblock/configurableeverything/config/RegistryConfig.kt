package net.frozenblock.configurableeverything.config

import blue.endless.jankson.Comment
import net.frozenblock.configurableeverything.registry.util.BiomeAddition
import net.frozenblock.configurableeverything.registry.util.PlacedFeatureAddition
import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.minecraft.world.level.biome.Biome.BiomeBuilder
import net.minecraft.world.level.biome.BiomeGenerationSettings
import net.minecraft.world.level.biome.BiomeSpecialEffects
import net.minecraft.world.level.biome.MobSpawnSettings

class RegistryConfig {
    companion object {
        private val BIOME_ADDITIONS: TypedEntryType<List<BiomeAddition?>?>? = ConfigRegistry.register(
            TypedEntryType(
                MOD_ID,
                BiomeAddition.CODEC.listOf()
            )
        )

        private val PLACED_FEATURE_ADDITIONS: TypedEntryType<List<PlacedFeatureAddition?>?>? = ConfigRegistry.register(
            TypedEntryType(
                MOD_ID,
                PlacedFeatureAddition.CODEC.listOf()
            )
        )

        @JvmField
        internal val INSTANCE: Config<RegistryConfig> = ConfigRegistry.register(
            JsonConfig(
                MOD_ID,
                RegistryConfig::class.java,
                makeConfigPath("registry"),
                CONFIG_JSONTYPE
            )
        )

        @JvmStatic
        fun get(): RegistryConfig = INSTANCE.config()
    }

    @JvmField
    @Comment("Adds these biomes to the biome registry on datapack load.")
    var biomeAdditions: TypedEntry<List<BiomeAddition?>?>? = TypedEntry(
        BIOME_ADDITIONS,
        listOf(
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
                            .fogColor(0)
                            .waterColor(0)
                            .waterFogColor(0)
                            .skyColor(0)
                            .build()
                    )
                    .mobSpawnSettings(MobSpawnSettings.EMPTY)
                    .generationSettings(BiomeGenerationSettings.EMPTY)
                    .build()
            )
        )
    )

    @JvmField
    @Comment("Adds these placed features to the placed feature registry on datapack load.")
    var placedFeatureAdditions: TypedEntry<List<PlacedFeatureAddition?>?>? = TypedEntry(
        PLACED_FEATURE_ADDITIONS,
        listOf() // cant make an example bc it requires a holder and the registry is dynamic
    )
}
